// Copyright (c) 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:zebra_rfid_reader_platform_interface/zebra_rfid_reader_platform_interface.dart';

/// Connects to Zebra Rfid Handheld readers and reads rfid tags asynchronously.
class ZebraRfidReader {
  static ZebraRfidReaderPlatform get _reader =>
      ZebraRfidReaderPlatform.instance;

  Future<void> init(RfidReaderInitParameters params) {
    return _reader.init(params);
  }

  Future<void> connect() {
    return _reader.connect();
  }

  Future<void> disconnect() {
    return _reader.disconnect();
  }

  Stream<ScannerStatusEvent> onScannerStatusEvent() {
    return _reader.onScannerStatusEvent();
  }

  Stream<RfidReadEvent> onRfidReadEvent() {
    return _reader.onRfidReadEvent();
  }
}
