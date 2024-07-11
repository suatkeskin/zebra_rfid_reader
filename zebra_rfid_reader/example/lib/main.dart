import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:zebra_rfid_reader/zebra_rfid_reader.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late ZebraRfidReader _reader;
  bool _connected = false;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      ZebraRfidReader.getInstance().then((value) {
        _reader = value;
        initPlatformState();
      });
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    bool connected;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      connected = await _reader.connect();
    } on PlatformException {
      debugPrint('Failed to get platform version.');
      connected = false;
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _connected = connected;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: OutlinedButton(
            onPressed: () async {
              if (_connected) {
                final result = await _reader.disconnect();
                setState(() {
                  _connected = result;
                });
              } else {
                final result = await _reader.connect();
                setState(() {
                  _connected = result;
                });
              }
            },
            child: _connected
                ? const Text('Disconnected')
                : const Text('Connected'),
          ),
        ),
      ),
    );
  }
}
