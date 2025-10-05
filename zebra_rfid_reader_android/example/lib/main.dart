import 'dart:async';

import 'package:flutter/material.dart';
import 'package:zebra_rfid_reader_android/zebra_rfid_reader_android.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: const MyPage(),
      ),
    );
  }
}

class MyPage extends StatefulWidget {
  const MyPage({super.key});

  @override
  State<MyPage> createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  final _platform = ZebraRfidReaderAndroid();
  Set<String> _tags = {};
  bool _connected = false;
  late StreamSubscription<dynamic> _tagSubscription;
  late StreamSubscription<dynamic> _statusSubscription;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      initPlatformState();
    });
  }

  @override
  void dispose() {
    _tagSubscription.cancel();
    _statusSubscription.cancel();
    super.dispose();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    _tagSubscription = _platform.onRfidReadEvent().listen((event) {
      setState(() {
        _tags.add(event.rfid.data);
      });
    });
    _statusSubscription = _platform.onScannerStatusEvent().listen((event) {
      setState(() {
        _connected = event.status.isConnected;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Padding(
          padding: const EdgeInsets.only(bottom: 60.0),
          child: ListView.builder(
            itemCount: _tags.length,
            itemBuilder: (context, index) {
              return ListTile(
                title: Text(_tags.toList()[index]),
              );
            },
          ),
        ),
        Align(
          alignment: Alignment.bottomCenter,
          child: OutlinedButton(
            onPressed: () async {
              if (_connected) {
                await _platform.disconnect();
                setState(() {
                  _tags = {};
                });
              } else {
                await _platform.connect();
                setState(() {
                  _tags = {};
                });
              }
            },
            child:
            _connected ? const Text('Disconnect') : const Text('Connect'),
          ),
        ),
      ],
    );
  }
}
