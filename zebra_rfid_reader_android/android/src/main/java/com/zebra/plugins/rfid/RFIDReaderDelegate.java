package com.zebra.plugins.rfid;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.zebra.plugins.rfid.R;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TriggerInfo;

import java.util.Collections;
import java.util.List;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodChannel;

public class RFIDReaderDelegate implements RfidEventsListener {
    private static final String LOG_TAG = "ZEBRA_RFID_READER";
    private final BackgroundTaskRunner backgroundTaskRunner = new BackgroundTaskRunner(1);
    private DartMessenger dartMessenger;
    private Readers readers;
    private RFIDReader reader;

    public RFIDReaderDelegate(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
        final MethodChannel methodChannel = new MethodChannel(binding.getBinaryMessenger(), "plugins.zebra.com/rfid");
        this.dartMessenger = new DartMessenger(binding.getApplicationContext(), methodChannel, new Handler(Looper.getMainLooper()));
        this.readers = new Readers(binding.getApplicationContext(), ENUM_TRANSPORT.SERVICE_SERIAL);
    }

    public void dispose() {
        if (dartMessenger != null) {
            dartMessenger = null;
        }
        disposeReader();
        readers.Dispose();
        readers = null;
    }

    public boolean disposeReader() {
        if (reader == null || !reader.isConnected()) {
            return true;
        }

        try {
            reader.Events.removeEventsListener(this);
            reader.disconnect();
            return reader.isConnected();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Can not disconnect from reader...", e);
            dartMessenger.sendReaderErrorEvent(R.string.rfid_reader_disconnect_failed);
            return false;
        }
    }

    public List<ReaderDevice> getAvailableReaders() throws InvalidUsageException {
        final List<ReaderDevice> readers = this.readers.GetAvailableRFIDReaderList();
        return readers == null ? Collections.emptyList() : readers;
    }

    public boolean connect() throws InvalidUsageException, OperationFailureException {
        Log.d(LOG_TAG, "Inside connect 1...");
        if (reader != null && reader.isConnected()) {
            Log.d(LOG_TAG, "Inside connect 2...");
            return true;
        }

        Log.d(LOG_TAG, "Inside connect 3...");
        final List<ReaderDevice> availableReaders = getAvailableReaders();
        if (availableReaders.isEmpty()) {
            Log.d(LOG_TAG, "Inside connect 4...");
            disposeReader();
            return false;
        }

        Log.d(LOG_TAG, "Inside connect 5...");
        final ReaderDevice device = availableReaders.iterator().next();
        reader = device.getRFIDReader();
        if (!reader.isConnected()) {
            reader.connect();
        }
        configureReader();
        return true;
    }

    @Override
    public void eventReadNotify(RfidReadEvents e) {
        dartMessenger.sendRfidTagReadEvent(e.getReadEventData().tagData.getTagID());
    }

    @Override
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
        Log.d(LOG_TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
        if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                dartMessenger.sendHandheldTriggerPressedEvent();
                backgroundTaskRunner.runInBackground(() -> {
                    try {
                        reader.Actions.Inventory.perform();
                        Log.d(LOG_TAG, "Perform reader inventory action...");
                    } catch (InvalidUsageException e) {
                        Log.e(LOG_TAG, "Can not perform reader inventory action...", e);
                        dartMessenger.sendReaderErrorEvent(String.format("%s%s", e.getInfo(), e.getVendorMessage()));
                    } catch (OperationFailureException e) {
                        Log.d(LOG_TAG, String.format("OperationFailureException occurred. %d: %s", e.getResults().getValue(), e.getVendorMessage()));
                        dartMessenger.sendReaderErrorEvent(e.getResults().getValue());
                    }
                    return null;
                });
            }
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                dartMessenger.sendHandheldTriggerReleasedEvent();
                backgroundTaskRunner.runInBackground(() -> {
                    try {
                        reader.Actions.Inventory.stop();
                        Log.d(LOG_TAG, "Stop reader inventory action...");
                    } catch (InvalidUsageException e) {
                        Log.e(LOG_TAG, "Can not stop reader inventory action...", e);
                        dartMessenger.sendReaderErrorEvent(String.format("%s%s", e.getInfo(), e.getVendorMessage()));
                    } catch (OperationFailureException e) {
                        Log.d(LOG_TAG, String.format("OperationFailureException occurred. %d: %s", e.getResults().getValue(), e.getVendorMessage()));
                        dartMessenger.sendReaderErrorEvent(e.getResults().getValue());
                    }
                    return null;
                });
            }
        }
    }

    private void configureReader() {
        if (reader == null || !reader.isConnected()) {
            return;
        }

        final TriggerInfo triggerInfo = new TriggerInfo();
        triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
        triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
        try {
            reader.Events.setBatchModeEvent(true);
            reader.Events.setReaderDisconnectEvent(true);
            reader.Events.setInventoryStartEvent(true);
            reader.Events.setInventoryStopEvent(true);
            reader.Events.setTagReadEvent(true);
            reader.Events.setBatteryEvent(true);
            reader.Events.setPowerEvent(true);
            reader.Events.setOperationEndSummaryEvent(true);
            reader.Events.setWPAEvent(true);
            reader.Events.setScanDataEvent(true);
            reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, false);
            reader.Config.setLedBlinkEnable(false);

            /*
            if (reader.ReaderCapabilities.SupportedRegions.length() > 0) {
                final RegionInfo regionInfo = reader.ReaderCapabilities.SupportedRegions.getRegionInfo(52);
                Log.d(LOG_TAG, "Region name: " + regionInfo.getName());
                final RegulatoryConfig regulatoryConfig = reader.Config.getRegulatoryConfig();
                regulatoryConfig.setRegion(regionInfo.getRegionCode());
                regulatoryConfig.setIsHoppingOn(regionInfo.isHoppingConfigurable());
                regulatoryConfig.setEnabledChannels(regionInfo.getSupportedChannels());
                reader.Config.setRegulatoryConfig(regulatoryConfig);
            }
             */


            reader.Events.addEventsListener(this);
            Log.d(LOG_TAG, "Add event listener...");

            // HH event
            reader.Events.setHandheldEvent(true);
            // tag event with tag data
            reader.Events.setTagReadEvent(true);
            // application will collect tag using getReadTags API
            reader.Events.setAttachTagDataWithReadEvent(true);
            // set trigger mode as rfid so scanner beam will not come
            reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
            // set start and stop triggers
            reader.Config.setStartTrigger(triggerInfo.StartTrigger);
            reader.Config.setStopTrigger(triggerInfo.StopTrigger);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Can not configure rfid reader...", e);
            dartMessenger.sendReaderErrorEvent(R.string.rfid_reader_config_error);
        }
    }
}
