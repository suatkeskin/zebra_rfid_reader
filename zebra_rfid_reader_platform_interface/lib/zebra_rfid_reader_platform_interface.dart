import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'src/method_channel_zebra_rfid_reader.dart';

/// The interface that implementations of zebra_rfid_reader must implement.
///
/// Platform implementations should extend this class rather than implement it as `zebra_rfid_reader`
/// does not consider newly added methods to be breaking changes. Extending this class
/// (using `extends`) ensures that the subclass will get the default implementation, while
/// platform implementations that `implements` this interface will be broken by newly added
/// [ZebraRfidReaderPlatform] methods.
abstract class ZebraRfidReaderPlatform extends PlatformInterface {
  /// Constructs a ZebraRfidReaderPlatform.
  ZebraRfidReaderPlatform() : super(token: _token);

  static final Object _token = Object();

  /// The default instance of [ZebraRfidReaderPlatform] to use.
  ///
  /// Defaults to [MethodChannelZebraRfidReader].
  static ZebraRfidReaderPlatform get instance => _instance;

  /// Platform-specific plugins should set this with their own platform-specific
  /// class that extends [ZebraRfidReaderPlatform] when they register themselves.
  static set instance(ZebraRfidReaderPlatform instance) {
    if (!instance.isMock) {
      PlatformInterface.verify(instance, _token);
    }
    _instance = instance;
  }

  static ZebraRfidReaderPlatform _instance = MethodChannelZebraRfidReader();

  /// Only mock implementations should set this to true.
  ///
  /// Mockito mocks are implementing this class with `implements` which is forbidden for anything
  /// other than mocks (see class docs). This property provides a backdoor for mockito mocks to
  /// skip the verification that the class isn't implemented with `implements`.
  @visibleForTesting
  @Deprecated('Use MockPlatformInterfaceMixin instead')
  bool get isMock => false;

  /// Connects the RFID reader.
  Future<bool> connect() async {
    throw UnimplementedError('disconnect() has not been implemented.');
  }

  /// Disconnects RFID reader.
  Future<bool> disconnect() async {
    throw UnimplementedError('disconnect() has not been implemented.');
  }
}
