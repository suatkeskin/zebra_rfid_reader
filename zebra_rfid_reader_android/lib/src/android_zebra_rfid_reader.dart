// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';
import 'dart:convert';

import 'package:flutter/foundation.dart' show visibleForTesting;
import 'package:flutter/services.dart';
import 'package:stream_transform/stream_transform.dart';
import 'package:zebra_rfid_reader_platform_interface/zebra_rfid_reader_platform_interface.dart';

import 'messages.g.dart';

class ZebraRfidReaderAndroid extends ZebraRfidReaderPlatform {
  ZebraRfidReaderAndroid({
    @visibleForTesting ZebraRfidReaderApi? api,
  }) : _api = api ?? ZebraRfidReaderApi();

  final ZebraRfidReaderApi _api;

  static void registerWith() {
    ZebraRfidReaderPlatform.instance = ZebraRfidReaderAndroid();
  }

  late final StreamController<ReaderEvent> _readerEventStreamController =
  _createDeviceEventStreamController();

  StreamController<ReaderEvent> _createDeviceEventStreamController() {
    const MethodChannel channel = MethodChannel("plugins.zebra.com/rfid");
    channel.setMethodCallHandler(handleReaderMethodCall);
    return StreamController<ReaderEvent>.broadcast();
  }

  Stream<ReaderEvent> _readerEvents() => _readerEventStreamController.stream;

  @override
  Future<void> init(RfidReaderInitParameters params) {
    return _api.init(InitParams(
      autoConnect: params.autoConnect,
    ));
  }

  @override
  Future<void> connect() {
    return _api.connect();
  }

  @override
  Future<void> disconnect() {
    return _api.disconnect();
  }

  @override
  Stream<ScannerStatusEvent> onScannerStatusEvent() {
    return _readerEvents().whereType<ScannerStatusEvent>();
  }

  @override
  Stream<RfidReadEvent> onRfidReadEvent() {
    return _readerEvents().whereType<RfidReadEvent>();
  }

  @visibleForTesting
  Future<dynamic> handleReaderMethodCall(MethodCall call) async {
    final Map<String, dynamic> arguments = _getArgumentDictionary(call);
    switch (call.method) {
      case 'scanner_status':
        _readerEventStreamController.add(
          ScannerStatusEvent.fromJson(arguments),
        );
      case 'rfid_read':
        _readerEventStreamController.add(
          RfidReadEvent.fromJson(arguments),
        );
      default:
        throw MissingPluginException();
    }
  }

  Map<String, dynamic> _getArgumentDictionary(MethodCall call) {
    return jsonDecode(jsonEncode(call.arguments));
  }
}
