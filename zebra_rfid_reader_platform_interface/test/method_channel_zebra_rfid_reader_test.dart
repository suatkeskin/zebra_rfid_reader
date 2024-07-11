import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:zebra_rfid_reader_platform_interface/src/method_channel_zebra_rfid_reader.dart';

const Map<String, dynamic> kDefaultResponses = <String, dynamic>{};

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('$MethodChannelZebraRfidReader', () {
    final MethodChannelZebraRfidReader rfidReader =
        MethodChannelZebraRfidReader();
    final MethodChannel channel = rfidReader.channel;

    final List<MethodCall> log = <MethodCall>[];
    late Map<String, dynamic>
        responses; // Some tests mutate some kDefaultResponses

    setUp(() {
      responses = Map<String, dynamic>.from(kDefaultResponses);
      TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
          .setMockMethodCallHandler(channel, (MethodCall methodCall) {
        log.add(methodCall);
        final dynamic response = responses[methodCall.method];
        if (response != null && response is Exception) {
          return Future<dynamic>.error('$response');
        }
        return Future<dynamic>.value(response);
      });
      log.clear();
    });

    test('Other functions pass through arguments to the channel', () async {
      final Map<void Function(), Matcher> tests = <void Function(), Matcher>{
        rfidReader.connect: isMethodCall('connect', arguments: null),
        rfidReader.disconnect: isMethodCall('disconnect', arguments: null),
      };

      for (final void Function() f in tests.keys) {
        f();
      }

      expect(log, tests.values);
    });
  });
}
