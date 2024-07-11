// Copyright (c) 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/messages.g.dart',
  javaOut: 'android/src/main/java/com/zebra/rfid/reader/Messages.java',
  javaOptions: JavaOptions(package: 'com.zebra.rfid.reader'),
  copyrightHeader: 'pigeons/copyright.txt',
))
@HostApi()
abstract class ZebraRfidReaderApi {
  @async
  bool connect();

  @TaskQueue(type: TaskQueueType.serialBackgroundThread)
  bool disconnect();
}
