// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

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
  Future<List<RfidReader>> availableRfidReaders() async {
    return _api.availableReaders().then(_mapRfidReader);
  }

  @override
  Future<bool> connect() async {
    return _api.connect();
  }

  @override
  Future<bool> disconnect() async {
    return _api.disconnect();
  }

  @override
  Stream<HandheldTriggerPressedEvent> onHandheldTriggerPressedEvent() {
    return _readerEvents().whereType<HandheldTriggerPressedEvent>();
  }

  @override
  Stream<HandheldTriggerReleasedEvent> onHandheldTriggerReleasedEvent() {
    return _readerEvents().whereType<HandheldTriggerReleasedEvent>();
  }

  @override
  Stream<RfidTagReadEvent> onRfidTagReadEvent() {
    return _readerEvents().whereType<RfidTagReadEvent>();
  }

  @override
  Stream<ReaderErrorEvent> onReaderError() {
    return _readerEvents().whereType<ReaderErrorEvent>();
  }

  @visibleForTesting
  Future<dynamic> handleReaderMethodCall(MethodCall call) async {
    final Map<String, dynamic> arguments = _getArgumentDictionary(call);
    switch (call.method) {
      case 'error':
        _readerEventStreamController.add(ReaderErrorEvent(
          code: arguments['code'],
          message: arguments['message'],
        ));
      case 'handheld_trigger_pressed':
        _readerEventStreamController.add(HandheldTriggerPressedEvent());
      case 'handheld_trigger_released':
        _readerEventStreamController.add(HandheldTriggerReleasedEvent());
      case 'rfid_tag_read':
        _readerEventStreamController.add(RfidTagReadEvent(
          tagId: arguments['tagId'],
        ));
      default:
        throw MissingPluginException();
    }
  }

  Map<String, Object?> _getArgumentDictionary(MethodCall call) {
    return (call.arguments as Map<Object?, Object?>).cast<String, Object?>();
  }

  List<RfidReader> _mapRfidReader(List<dynamic> data) {
    return data.map((d) => RfidReader(name: d['name']! as String)).toList();
  }
}
