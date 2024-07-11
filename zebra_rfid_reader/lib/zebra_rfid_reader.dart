// Copyright (c) 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/foundation.dart' show visibleForTesting;
import 'package:zebra_rfid_reader_platform_interface/zebra_rfid_reader_platform_interface.dart';

/// Connects to Zebra RFID Handheld readers and reads rfid tags asynchronously.
class ZebraRfidReader {
  ZebraRfidReader._();

  static Completer<ZebraRfidReader>? _completer;

  static ZebraRfidReaderPlatform get _reader =>
      ZebraRfidReaderPlatform.instance;

  /// Resets class's static values to allow for testing of setPrefix flow.
  @visibleForTesting
  static void resetStatic() {
    _completer = null;
  }

  /// Loads and parses the [SharedPreferences] for this app from disk.
  ///
  /// Because this is reading from disk, it shouldn't be awaited in
  /// performance-sensitive blocks.
  static Future<ZebraRfidReader> getInstance() async {
    if (_completer == null) {
      final Completer<ZebraRfidReader> completer = Completer<ZebraRfidReader>();
      _completer = completer;
      try {
        completer.complete(ZebraRfidReader._());
      } catch (e) {
        // If there's an error, explicitly return the future with an error.
        // then set the completer to null so we can retry.
        completer.completeError(e);
        final Future<ZebraRfidReader> sharedPrefsFuture = completer.future;
        _completer = null;
        return sharedPrefsFuture;
      }
    }
    return _completer!.future;
  }

  Future<bool> connect() {
    return _reader.connect();
  }

  Future<bool> disconnect() {
    return _reader.disconnect();
  }
}
