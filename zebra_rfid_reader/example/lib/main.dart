import 'dart:async';

import 'package:flutter/material.dart';
import 'package:zebra_rfid_reader/zebra_rfid_reader.dart';

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
  final _reader = ZebraRfidReader();

  Set<String> _rfids = {};
  bool _connected = false;
  late StreamSubscription<dynamic> _rfidSubscription;
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
    _rfidSubscription.cancel();
    _statusSubscription.cancel();
    super.dispose();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    await _reader.init(RfidReaderInitParameters(autoConnect: true));
    _rfidSubscription = _reader.onRfidReadEvent().listen((event) {
      setState(() {
        _rfids.add(event.rfid.data);
      });
    });
    _statusSubscription = _reader.onScannerStatusEvent().listen((event) {
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
            itemCount: _rfids.length,
            itemBuilder: (context, index) {
              return ListTile(
                title: Text(_rfids.toList()[index]),
              );
            },
          ),
        ),
        Align(
          alignment: Alignment.bottomCenter,
          child: OutlinedButton(
            onPressed: () async {
              if (_connected) {
                await _reader.disconnect();
                setState(() {
                  _rfids = {};
                });
              } else {
                await _reader.connect();
                setState(() {
                  _rfids = {};
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
