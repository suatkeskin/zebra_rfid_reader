// Copyright 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

/// A class that represents the data of a rfid.
///
/// This class holds the information related to a scanned rfid,
/// including the data contained in the rfid and the type of rfid.
class RfidData {
  /// Creates a new instance of [RfidData] with the provided [data] and [type].
  ///
  /// Both [data] and [type] are required fields and cannot be null.
  RfidData({
    required this.data,
    required this.type,
  });

  /// The data contained in the Rfid.
  late final String data;

  /// The type of the Rfid (e.g., QR code, Code128, etc.).
  late final String type;

  /// Creates a new instance of [RfidData] from a JSON object.
  ///
  /// The [json] parameter must include the keys 'data' and 'type'.
  RfidData.fromJson(Map<String, dynamic> json) {
    data = json['data'];
    type = json['type'];
  }

  /// Converts the [RfidData] instance to a JSON object.
  ///
  /// Returns a Map with 'data' and 'type' keys containing the respective values.
  Map<String, dynamic> toJson() {
    final jsonData = <String, dynamic>{};
    jsonData['data'] = data;
    jsonData['type'] = type;
    return jsonData;
  }

  /// Compares this [RfidData] instance with another object.
  ///
  /// Returns `true` if the other object is a [RfidData] instance
  /// and both [data] and [type] are equal, otherwise returns `false`.
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is RfidData &&
          runtimeType == other.runtimeType &&
          type == other.type &&
          data == other.data;

  /// Generates a hash code for this [RfidData] instance.
  ///
  /// The hash code is based on the [data] and [type] properties.
  @override
  int get hashCode => type.hashCode ^ data.hashCode;

  /// Returns a string representation of this [RfidData] instance.
  ///
  /// The string includes the [data] and [type] properties.
  @override
  String toString() {
    return 'RfidData{data: $data, type: $type}';
  }
}
