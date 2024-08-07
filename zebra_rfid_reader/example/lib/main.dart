import 'dart:async';

import 'package:flutter/material.dart';
import 'package:zebra_rfid_reader/zebra_rfid_reader.dart';
import 'package:zebra_rfid_reader_example/src/util/permission_manager.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  PermissionManager.instance.checkAndRequestBluetoothPermission();
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

  Set<String> _tags = {};
  bool _connected = false;
  late StreamSubscription<dynamic> _tagSubscription;
  late StreamSubscription<dynamic> _triggerPressSubscription;
  late StreamSubscription<dynamic> _errorSubscription;

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
    _triggerPressSubscription.cancel();
    _errorSubscription.cancel();
    super.dispose();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    _tagSubscription = _reader.onRfidTagReadEvent().listen((event) {
      setState(() {
        _tags.add(event.tagId);
      });
    });
    _triggerPressSubscription =
        _reader.onHandheldTriggerPressedEvent().listen((event) {
      setState(() {
        _tags = {};
      });
    });
    _errorSubscription = _reader.onReaderError().listen((event) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text(event.message),
      ));
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
                final result = await _reader.disconnect();
                setState(() {
                  _connected = result;
                  _tags = {};
                });
              } else {
                final result = await _reader.connect();
                setState(() {
                  _connected = result;
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
