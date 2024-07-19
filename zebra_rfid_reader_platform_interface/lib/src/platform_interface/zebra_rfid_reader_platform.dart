// Copyright 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:zebra_rfid_reader_platform_interface/src/events/rfid_reader_event.dart';
import 'package:zebra_rfid_reader_platform_interface/src/types/types.dart';

abstract class ZebraRfidReaderPlatform extends PlatformInterface {
  ZebraRfidReaderPlatform() : super(token: _token);

  static final Object _token = Object();

  static ZebraRfidReaderPlatform _instance = _PlaceholderImplementation();

  static ZebraRfidReaderPlatform get instance => _instance;

  static set instance(ZebraRfidReaderPlatform instance) {
    PlatformInterface.verify(instance, _token);
    _instance = instance;
  }

  Future<List<RfidReader>> availableRfidReaders() {
    throw UnimplementedError('availableReaders() is not implemented.');
  }

  Future<bool> connect() {
    throw UnimplementedError('connect() has not been implemented.');
  }

  Future<bool> disconnect() {
    throw UnimplementedError('disconnect() is not implemented.');
  }

  Stream<HandheldTriggerPressedEvent> onHandheldTriggerPressedEvent() {
    throw UnimplementedError(
        'onReaderHandheldTriggerPressedEvent() is not implemented.');
  }

  Stream<HandheldTriggerReleasedEvent> onHandheldTriggerReleasedEvent() {
    throw UnimplementedError(
        'onHandheldTriggerReleasedEvent() is not implemented.');
  }

  Stream<RfidTagReadEvent> onRfidTagReadEvent() {
    throw UnimplementedError('onTagRead() is not implemented.');
  }

  Stream<ReaderErrorEvent> onReaderError() {
    throw UnimplementedError('onReaderError() is not implemented.');
  }
}

class _PlaceholderImplementation extends ZebraRfidReaderPlatform {}
