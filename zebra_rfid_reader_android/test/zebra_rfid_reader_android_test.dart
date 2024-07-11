// Copyright (c) 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/annotations.dart';
import 'package:mockito/mockito.dart';
import 'package:zebra_rfid_reader_android/src/messages.g.dart';
import 'package:zebra_rfid_reader_android/zebra_rfid_reader_android.dart';
import 'package:zebra_rfid_reader_platform_interface/zebra_rfid_reader_platform_interface.dart';

import 'zebra_rfid_reader_android_test.mocks.dart';

@GenerateMocks(<Type>[ZebraRfidReaderApi])
void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  late ZebraRfidReaderAndroid rfidReader;
  late MockZebraRfidReaderApi api;

  setUp(() {
    api = MockZebraRfidReaderApi();
    rfidReader = ZebraRfidReaderAndroid(api: api);
  });

  test('registered instance', () {
    ZebraRfidReaderAndroid.registerWith();
    expect(ZebraRfidReaderPlatform.instance, isA<ZebraRfidReaderAndroid>());
  });

  test('connect calls through', () async {
    await rfidReader.connect();

    verify(api.connect());
  });

  test('disconnect calls through', () async {
    await rfidReader.disconnect();

    verify(api.disconnect());
  });
}
