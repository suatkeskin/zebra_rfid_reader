// Copyright (c) 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:pigeon/pigeon.dart';

class InitParams {
  const InitParams({
    this.autoConnect = false,
  });

  final bool autoConnect;
}

class RfidReaderDto {
  const RfidReaderDto({
    required this.name,
  });

  final String name;
}

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/messages.g.dart',
  javaOut: 'android/src/main/java/com/zebra/plugins/rfid/Messages.java',
  javaOptions: JavaOptions(package: 'com.zebra.plugins.rfid'),
  copyrightHeader: 'pigeons/copyright.txt',
))
@HostApi()
abstract class ZebraRfidReaderApi {
  void init(InitParams params);

  void connect();

  void disconnect();
}
