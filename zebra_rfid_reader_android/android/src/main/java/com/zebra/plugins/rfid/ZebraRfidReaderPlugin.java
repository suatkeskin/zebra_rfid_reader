package com.zebra.plugins.rfid;

import androidx.annotation.NonNull;

import com.zebra.plugins.rfid.Messages.ZebraRfidReaderApi;

import io.flutter.embedding.engine.plugins.FlutterPlugin;

/**
 * ZebraRfidReaderAndroidPlugin
 */
public class ZebraRfidReaderPlugin implements FlutterPlugin, ZebraRfidReaderApi {
    private RFIDReaderDelegate rfidReaderDelegate;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        this.rfidReaderDelegate = new RFIDReaderDelegate(binding);
        ZebraRfidReaderApi.setUp(binding.getBinaryMessenger(), this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        ZebraRfidReaderApi.setUp(binding.getBinaryMessenger(), null);
        rfidReaderDelegate.dispose();
    }

    @Override
    public void init(@NonNull Messages.InitParams params) {
        if (params.getAutoConnect()) {
            rfidReaderDelegate.connect();
        }
    }

    @Override
    public void connect() {
        rfidReaderDelegate.connect();
    }

    @Override
    public void disconnect() {
        rfidReaderDelegate.disposeReader();
    }
}
