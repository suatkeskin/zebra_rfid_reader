package com.zebra.rfid.reader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TriggerInfo;
import com.zebra.rfid.reader.Messages.ZebraRfidReaderApi;

import java.util.List;
import java.util.concurrent.Callable;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;


/**
 * ZebraRfidReaderAndroidPlugin
 */
public class ZebraRfidReaderPlugin implements FlutterPlugin, ZebraRfidReaderApi {
    private static final String LOG_TAG = "ZEBRA_RFID_READER";
    private static final String ERROR_REASON_EXCEPTION = "exception";

    // TODO(suatkeskin): See whether this can be replaced with background channels.
    private final BackgroundTaskRunner backgroundTaskRunner = new BackgroundTaskRunner(1);
    private @Nullable BinaryMessenger messenger;
    private Readers readers;
    private RFIDReader reader;
    private RfidEventHandler eventHandler;

    @VisibleForTesting
    public void initInstance(@NonNull BinaryMessenger messenger, @NonNull Context context) {
        this.messenger = messenger;
        ZebraRfidReaderApi.setUp(messenger, this);
        readers = new Readers(context, ENUM_TRANSPORT.SERVICE_SERIAL);
    }

    private void dispose() {
        if (messenger != null) {
            ZebraRfidReaderApi.setUp(messenger, null);
            messenger = null;
        }
        if (readers != null) {
            readers = null;
        }
        if (reader != null) {
            disconnect();
            reader = null;
        }
        if (eventHandler != null) {
            eventHandler = null;
        }
    }

    private void configureReader() {
        if (reader.isConnected()) {
            TriggerInfo triggerInfo = new TriggerInfo();
            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
            try {
                // receive events from reader
                if (eventHandler == null) {
                    eventHandler = new RfidEventHandler(backgroundTaskRunner, reader);
                }
                reader.Events.addEventsListener(eventHandler);
                // HH event
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                // application will collect tag using getReadTags API
                reader.Events.setAttachTagDataWithReadEvent(false);
                // set trigger mode as rfid so scanner beam will not come
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                // set start and stop triggers
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Can not configure rfid reader...", e);
            }
        }
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        initInstance(binding.getBinaryMessenger(), binding.getApplicationContext());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        dispose();
    }

    @Override
    public void connect(@NonNull Messages.Result<Boolean> result) {
        Log.d(LOG_TAG, "Connect method called...");
        final Callable<Boolean> connectReaderTask =
                () -> {
                    Log.d(LOG_TAG, "Inside connect async method...");
                    final List<ReaderDevice> availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
                    if (availableRFIDReaderList != null && !availableRFIDReaderList.isEmpty()) {
                        // get first reader from list
                        final ReaderDevice readerDevice = availableRFIDReaderList.iterator().next();
                        reader = readerDevice.getRFIDReader();
                        if (!reader.isConnected()) {
                            // Establish connection to the RFID Reader
                            reader.connect();
                        }
                        configureReader();
                        Log.d(LOG_TAG, "Connected to " + readerDevice.getName());
                        return reader.isConnected();
                    }
                    return false;
                };

        backgroundTaskRunner.runInBackground(
                connectReaderTask,
                connectReaderFuture -> {
                    try {
                        Log.d(LOG_TAG, "Inside connect async method callback...");
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
                });
    }

    @NonNull
    @Override
    public Boolean disconnect() {
        if (reader == null || !reader.isConnected()) {
            return true;
        }

        try {
            reader.disconnect();
            return reader.isConnected();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Can not disconnect from rfid reader...", e);
            return false;
        }
    }
}
