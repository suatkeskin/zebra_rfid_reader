package com.zebra.plugins.rfid;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zebra.plugins.rfid.Messages.ZebraRfidReaderApi;
import com.zebra.rfid.api3.ReaderDevice;

import java.util.ArrayList;
import java.util.List;

import io.flutter.embedding.engine.plugins.FlutterPlugin;

/**
 * ZebraRfidReaderAndroidPlugin
 */
public class ZebraRfidReaderPlugin implements FlutterPlugin, ZebraRfidReaderApi {
    private static final String LOG_TAG = "ZEBRA_RFID_READER";
    private static final String ERROR_REASON_EXCEPTION = "exception";
    private final BackgroundTaskRunner backgroundTaskRunner = new BackgroundTaskRunner(1);
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
    public void availableReaders(@NonNull Messages.Result<List<Messages.RfidReaderDto>> result) {
        backgroundTaskRunner.runInBackground(
                () -> rfidReaderDelegate.getAvailableReaders(),
                future -> {
                    try {
                        final List<Messages.RfidReaderDto> availableReaders = new ArrayList<>();
                        final List<ReaderDevice> readerDeviceList = future.get();
                        if (readerDeviceList != null && !readerDeviceList.isEmpty()) {
                            for (ReaderDevice readerDevice : readerDeviceList) {
                                final Messages.RfidReaderDto readerDescription = new Messages.RfidReaderDto();
                                readerDescription.setName(readerDevice.getName());
                                availableReaders.add(readerDescription);
                            }
                        }
                        result.success(availableReaders);
                    } catch (InterruptedException e) {
                        Log.e(LOG_TAG, "Reader connection interrupted... ", e);
                        result.error(new Messages.FlutterError(ERROR_REASON_EXCEPTION, e.getMessage(), null));
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Reader connection failed... ", e);
                        @Nullable Throwable cause = e.getCause();
                        result.error(new Messages.FlutterError(ERROR_REASON_EXCEPTION, cause == null ? null : cause.getMessage(), null));
                    }
                }
        );
    }

    @Override
    public void connect(@NonNull Messages.Result<Boolean> result) {
        backgroundTaskRunner.runInBackground(
                () -> rfidReaderDelegate.connect(),
                connectReaderFuture -> {
                    try {
                        result.success(connectReaderFuture.get());
                    } catch (InterruptedException e) {
                        Log.e(LOG_TAG, "Reader connection interrupted... ", e);
                        result.error(new Messages.FlutterError(ERROR_REASON_EXCEPTION, e.getMessage(), null));
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Reader connection failed... ", e);
                        @Nullable Throwable cause = e.getCause();
                        result.error(new Messages.FlutterError(ERROR_REASON_EXCEPTION, cause == null ? null : cause.getMessage(), null));
                    }
                }
        );
    }

    @Override
    public void disconnect(@NonNull Messages.Result<Boolean> result) {
        backgroundTaskRunner.runInBackground(
                () -> rfidReaderDelegate.disposeReader(),
                connectReaderFuture -> {
                    try {
                        result.success(connectReaderFuture.get());
                    } catch (InterruptedException e) {
                        Log.e(LOG_TAG, "Reader disconnection interrupted... ", e);
                        result.error(new Messages.FlutterError(ERROR_REASON_EXCEPTION, e.getMessage(), null));
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Reader connection failed... ", e);
                        @Nullable Throwable cause = e.getCause();
                        result.error(new Messages.FlutterError(ERROR_REASON_EXCEPTION, cause == null ? null : cause.getMessage(), null));
                    }
                }
        );
    }
}
