import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:zebra_rfid_reader_platform_interface/src/method_channel_zebra_rfid_reader.dart';
import 'package:zebra_rfid_reader_platform_interface/zebra_rfid_reader_platform_interface.dart';

void main() {
  // Store the initial instance before any tests change it.
  final ZebraRfidReaderPlatform initialInstance =
      ZebraRfidReaderPlatform.instance;

  group('$ZebraRfidReaderPlatform', () {
    test('$MethodChannelZebraRfidReader is the default instance', () {
      expect(initialInstance, isA<MethodChannelZebraRfidReader>());
    });

    test('Cannot be implemented with `implements`', () {
      expect(() {
        ZebraRfidReaderPlatform.instance = ImplementsZebraRfidReaderPlatform();
        // In versions of `package:plugin_platform_interface` prior to fixing
        // https://github.com/flutter/flutter/issues/109339, an attempt to
        // implement a platform interface using `implements` would sometimes
        // throw a `NoSuchMethodError` and other times throw an
        // `AssertionError`.  After the issue is fixed, an `AssertionError` will
        // always be thrown.  For the purpose of this test, we don't really care
        // what exception is thrown, so just allow any exception.
      }, throwsA(anything));
    });

    test('Can be extended', () {
      ZebraRfidReaderPlatform.instance = ExtendsZebraRfidReaderPlatform();
    });

    test('Can be mocked with `implements`', () {
      ZebraRfidReaderPlatform.instance = ModernMockImplementation();
    });

    test('still supports legacy isMock', () {
      ZebraRfidReaderPlatform.instance = LegacyIsMockImplementation();
    });
  });
}

class LegacyIsMockImplementation extends Mock
    implements ZebraRfidReaderPlatform {
  @override
  bool get isMock => true;
}

class ModernMockImplementation extends Mock
    with MockPlatformInterfaceMixin
    implements ZebraRfidReaderPlatform {
  @override
  bool get isMock => false;
}

class ImplementsZebraRfidReaderPlatform extends Mock
    implements ZebraRfidReaderPlatform {}

class ExtendsZebraRfidReaderPlatform extends ZebraRfidReaderPlatform {}
