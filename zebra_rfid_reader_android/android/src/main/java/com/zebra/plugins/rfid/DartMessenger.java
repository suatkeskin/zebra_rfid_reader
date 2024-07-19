// Copyright 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.zebra.plugins.rfid;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

/**
 * Utility class that facilitates communication to the Flutter client
 */
public class DartMessenger {
    @NonNull
    private final MethodChannel readerChannel;
    @NonNull
    private final Handler handler;
    @NonNull
    private final Context context;

    /**
     * Specifies the different reader related message types.
     */
    enum ReaderEventType {
        /**
         * Indicates that an error occurred while interacting with the reader.
         */
        ERROR("error"),
        /**
         * Indicates that the handheld trigger pressed.
         */
        HANDHELD_TRIGGER_PRESSED("handheld_trigger_pressed"),
        /**
         * Indicates that the handheld trigger released.
         */
        HANDHELD_TRIGGER_RELEASED("handheld_trigger_released"),
        /**
         * Indicates that the new rfid tag read occurred.
         */
        RFID_TAG_READ("rfid_tag_read");

        final String method;

        /**
         * Converts the supplied method name to the matching {@link ReaderEventType}.
         *
         * @param method name to be converted into a {@link ReaderEventType}.
         */
        ReaderEventType(String method) {
            this.method = method;
        }
    }

    /**
     * Creates a new instance of the {@link DartMessenger} class.
     *
     * @param messenger is the {@link BinaryMessenger} that is used to communicate with Flutter.
     */
    DartMessenger(@NonNull Context context, @NonNull MethodChannel readerChannel, @NonNull Handler handler) {
        this.context = context;
        this.readerChannel = readerChannel;
        this.handler = handler;
    }

    /**
     * Sends a message to the Flutter client informing that the handheld trigger pressed.
     */
    public void sendHandheldTriggerPressedEvent() {
        this.send(ReaderEventType.HANDHELD_TRIGGER_PRESSED);
    }

    /**
     * Sends a message to the Flutter client informing that the handheld trigger released.
     */
    public void sendHandheldTriggerReleasedEvent() {
        this.send(ReaderEventType.HANDHELD_TRIGGER_RELEASED);
    }

    /**
     * Sends a message to the Flutter client informing that the new tag read.
     */
    public void sendRfidTagReadEvent(String tagId) {
        assert (tagId != null);
        send(ReaderEventType.RFID_TAG_READ,
                new HashMap<String, Object>() {
                    {
                        put("tagId", tagId);
                    }
                }
        );
    }

    /**
     * Sends a message to the Flutter client informing that an error occurred while interacting with
     * the reader.
     *
     * @param code contains details regarding the error that occurred.
     */
    public void sendReaderErrorEvent(int code) {
        this.send(
                ReaderEventType.ERROR,
                new HashMap<String, Object>() {
                    {
                        put("code", code);
                        put("message", ErrorUtils.getErrorMessage(context, code));
                    }
                }
        );
    }

    /**
     * Sends a message to the Flutter client informing that an error occurred while interacting with
     * the reader.
     *
     * @param description contains details regarding the error that occurred.
     */
    public void sendReaderErrorEvent(@Nullable String description) {
        this.send(
                ReaderEventType.ERROR,
                new HashMap<String, Object>() {
                    {
                        if (!TextUtils.isEmpty(description)) put("description", description);
                    }
                }
        );
    }

    private void send(ReaderEventType eventType) {
        send(eventType, new HashMap<>());
    }

    private void send(ReaderEventType eventType, Map<String, Object> args) {
        handler.post(() -> readerChannel.invokeMethod(eventType.method, args));
    }
}
