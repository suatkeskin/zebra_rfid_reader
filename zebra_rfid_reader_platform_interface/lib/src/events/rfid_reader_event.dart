// Copyright 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter/foundation.dart';

@immutable
abstract class ReaderEvent {}

class HandheldTriggerPressedEvent extends ReaderEvent {}

class HandheldTriggerReleasedEvent extends ReaderEvent {}

class RfidTagReadEvent extends ReaderEvent {
  RfidTagReadEvent({
    required this.tagId,
  });

  late final String tagId;

  RfidTagReadEvent.fromJson(Map<String, dynamic> json) {
    tagId = json['tagId'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['tagId'] = tagId;
    return _data;
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      super == other &&
          other is RfidTagReadEvent &&
          runtimeType == other.runtimeType &&
          tagId == other.tagId;

  @override
  int get hashCode => tagId.hashCode;
}

class ReaderErrorEvent extends ReaderEvent {
  ReaderErrorEvent({
    required this.code,
    required this.message,
  });

  late final int code;
  late final String message;

  ReaderErrorEvent.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    message = json['message'];
  }

  Map<String, dynamic> toJson() {
    final data = <String, dynamic>{};
    data['code'] = code;
    data['message'] = message;
    return data;
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      super == other &&
          other is ReaderErrorEvent &&
          runtimeType == other.runtimeType &&
          message == other.message;

  @override
  int get hashCode => Object.hash(super.hashCode, message);
}
