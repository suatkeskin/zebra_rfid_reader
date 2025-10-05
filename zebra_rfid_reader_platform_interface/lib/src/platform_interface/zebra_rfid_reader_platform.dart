// Copyright 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:zebra_rfid_reader_platform_interface/src/events/rfid_reader_event.dart';
import 'package:zebra_rfid_reader_platform_interface/src/types/types.dart';

/// The interface that implementations of rfid reader must implement.
///
/// Platform implementations should extend this class rather than implement it as `reader`
/// does not consider newly added methods to be breaking changes. Extending this class
/// (using `extends`) ensures that the subclass will get the default implementation, while
/// platform implementations that `implements` this interface will be broken by newly added
/// [ZebraRfidReaderPlatform] methods.
abstract class ZebraRfidReaderPlatform extends PlatformInterface {
  /// Constructs a ZebraRfidReaderPlatform.
  ZebraRfidReaderPlatform() : super(token: _token);

  static final Object _token = Object();

  static ZebraRfidReaderPlatform _instance = _PlaceholderImplementation();

  /// The default instance of [ZebraRfidReaderPlatform] to use.
  ///
  /// Defaults to [MethodChannelZebraRfidReader].
  static ZebraRfidReaderPlatform get instance => _instance;

  /// Platform-specific plugins should set this with their own platform-specific
  /// class that extends [ZebraRfidReaderPlatform] when they register themselves.
  static set instance(ZebraRfidReaderPlatform instance) {
    PlatformInterface.verify(instance, _token);
    _instance = instance;
  }

  /// Initializes the reader on the device.
  ///
  /// [autoConnect] is used to immediately connect reader after init.
  Future<void> init(RfidReaderInitParameters params) {
    throw UnimplementedError('init() has not been implemented.');
  }

  /// Connects to reader.
  Future<void> connect() {
    throw UnimplementedError('connect() has not been implemented.');
  }

  /// Disconnects from reader.
  Future<void> disconnect() {
    throw UnimplementedError('disconnect() is not implemented.');
  }

  /// The scanner status changed.
  Stream<ScannerStatusEvent> onScannerStatusEvent() {
    throw UnimplementedError('onScannerStatusEvent() is not implemented.');
  }

  /// The reader read a new rfid.
  Stream<RfidReadEvent> onRfidReadEvent() {
    throw UnimplementedError('onRfidReadEvent() is not implemented.');
  }
}

class _PlaceholderImplementation extends ZebraRfidReaderPlatform {}
