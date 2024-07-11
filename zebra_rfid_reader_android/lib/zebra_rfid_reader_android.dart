// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/foundation.dart' show visibleForTesting;
import 'package:zebra_rfid_reader_platform_interface/zebra_rfid_reader_platform_interface.dart';

import 'src/messages.g.dart';

/// Android implementation of [ZebraRfidReaderPlatform].
class ZebraRfidReaderAndroid extends ZebraRfidReaderPlatform {
  /// Creates a new plugin implementation instance.
  ZebraRfidReaderAndroid({
    @visibleForTesting ZebraRfidReaderApi? api,
  }) : _api = api ?? ZebraRfidReaderApi();

  final ZebraRfidReaderApi _api;

  /// Registers this class as the default instance of [ZebraRfidReaderPlatform].
  static void registerWith() {
    ZebraRfidReaderPlatform.instance = ZebraRfidReaderAndroid();
  }

  @override
  Future<bool> connect() async {
    return _api.connect();
  }

  @override
  Future<bool> disconnect() async {
    return _api.disconnect();
  }
}
