// Copyright (c) 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:zebra_rfid_reader_platform_interface/zebra_rfid_reader_platform_interface.dart';

/// Connects to Zebra RFID Handheld readers and reads rfid tags asynchronously.
class ZebraRfidReader {
  static ZebraRfidReaderPlatform get _reader =>
      ZebraRfidReaderPlatform.instance;

  Future<List<RfidReader>> availableRfidReaders() {
    return _reader.availableRfidReaders();
  }

  Future<bool> connect() {
    return _reader.connect();
  }

  Future<bool> disconnect() {
    return _reader.disconnect();
  }

  Stream<HandheldTriggerPressedEvent> onHandheldTriggerPressedEvent() {
    return _reader.onHandheldTriggerPressedEvent();
  }

  Stream<HandheldTriggerReleasedEvent> onHandheldTriggerReleasedEvent() {
    return _reader.onHandheldTriggerReleasedEvent();
  }

  Stream<RfidTagReadEvent> onRfidTagReadEvent() {
    return _reader.onRfidTagReadEvent();
  }

  Stream<ReaderErrorEvent> onReaderError() {
    return _reader.onReaderError();
  }
}
