import 'dart:async';

import 'package:flutter/foundation.dart' show visibleForTesting;
import 'package:flutter/services.dart';

import '../zebra_rfid_reader_platform_interface.dart';

/// An implementation of [ZebraRfidReaderPlatform] that uses method channels.
class MethodChannelZebraRfidReader extends ZebraRfidReaderPlatform {
  /// This is only exposed for test purposes. It shouldn't be used by clients of
  /// the plugin as it may break or change at any time.
  @visibleForTesting
  MethodChannel channel =
      const MethodChannel('plugins.flutter.io/zebra_rfid_reader');

  @override
  Future<bool> connect() async {
    return (await channel.invokeMethod<bool>('connect'))!;
  }

  @override
  Future<bool> disconnect() async {
    return (await channel.invokeMethod<bool>('disconnect'))!;
  }
}
