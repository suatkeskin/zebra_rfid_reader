package com.zebra.plugins.rfid;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

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
import com.zebra.rfid.api3.TagData;
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

    public void disposeReader() {
        if (reader == null || !reader.isConnected()) {
            dartMessenger.sendScannerStatusEvent(ScannerStatus.DISCONNECTED);

        }

        try {
            reader.Events.removeEventsListener(this);
            reader.disconnect();
            if (reader.isConnected()) {
                dartMessenger.sendScannerStatusEvent(ScannerStatus.CONNECTED);
            } else {
                reader = null;
                dartMessenger.sendScannerStatusEvent(ScannerStatus.DISCONNECTED);
            }
        } catch (InvalidUsageException | OperationFailureException e) {
            Log.e(LOG_TAG, "Can not disconnect from reader...", e);
            dartMessenger.sendReaderErrorEvent(R.string.rfid_reader_disconnect_failed);
        }
    }

    public List<ReaderDevice> getAvailableReaders() {
        final List<ReaderDevice> readers;
        try {
            readers = this.readers.GetAvailableRFIDReaderList();
        } catch (InvalidUsageException e) {
            Log.e(LOG_TAG, "Could not get available readers...", e);
            return Collections.emptyList();
        }
        return readers == null ? Collections.emptyList() : readers;
    }

    public void connect() {
        if (reader != null && reader.isConnected()) {
            dartMessenger.sendScannerStatusEvent(ScannerStatus.CONNECTED);
            return;
        }

        final List<ReaderDevice> availableReaders = getAvailableReaders();
        if (availableReaders.isEmpty()) {
            disposeReader();
            dartMessenger.sendScannerStatusEvent(ScannerStatus.DISABLED);
        }

        final ReaderDevice device = availableReaders.iterator().next();
        reader = device.getRFIDReader();
        if (!reader.isConnected()) {
            try {
                reader.connect();
            } catch (InvalidUsageException | OperationFailureException e) {
                Log.e(LOG_TAG, "Could not connect reader...", e);
                throw new RuntimeException(e);
            }
        }
        configureReader();
        dartMessenger.sendScannerStatusEvent(ScannerStatus.CONNECTED);
    }

    @Override
    public void eventReadNotify(RfidReadEvents e) {
        final TagData tagData = e.getReadEventData().tagData;
        Log.d(LOG_TAG, String.format("Tag Id: %s", tagData.getTagID()));
        dartMessenger.sendRfidReadEvent("", tagData.getTagID());
    }

    @Override
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
        Log.d(LOG_TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
        if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                dartMessenger.sendScannerStatusEvent(ScannerStatus.SCANNING);
                backgroundTaskRunner.runInBackground(() -> {
                    try {
                        reader.Actions.Inventory.perform();
                    } catch (InvalidUsageException e) {
                        Log.e(LOG_TAG, "Can not perform reader inventory action...", e);
                        dartMessenger.sendReaderErrorEvent(String.format("%s%s", e.getInfo(), e.getVendorMessage()));
                    } catch (OperationFailureException e) {
                        Log.e(LOG_TAG, String.format("OperationFailureException occurred. %d: %s", e.getResults().getValue(), e.getVendorMessage()), e);
                        dartMessenger.sendReaderErrorEvent(e.getResults().getValue());
                    }
                    return null;
                });
            }
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                dartMessenger.sendScannerStatusEvent(ScannerStatus.IDLE);
                backgroundTaskRunner.runInBackground(() -> {
                    try {
                        reader.Actions.Inventory.stop();
                    } catch (InvalidUsageException e) {
                        Log.e(LOG_TAG, "Can not stop reader inventory action...", e);
                        dartMessenger.sendReaderErrorEvent(String.format("%s%s", e.getInfo(), e.getVendorMessage()));
                    } catch (OperationFailureException e) {
                        Log.e(LOG_TAG, String.format("OperationFailureException occurred. %d: %s", e.getResults().getValue(), e.getVendorMessage()), e);
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
