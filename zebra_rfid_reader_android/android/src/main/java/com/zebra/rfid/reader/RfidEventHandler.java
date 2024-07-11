// Copyright 2024, Suat Keskin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.zebra.rfid.reader;

import android.util.Log;

import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.TagData;

/**
 * Read/Status Notify handler
 * Implement the RfidEventsLister class to receive event notifications
 */
public class RfidEventHandler implements RfidEventsListener {
    private static final String LOG_TAG = "ZEBRA_RFID_READER";
    private final BackgroundTaskRunner backgroundTaskRunner;
    private final RFIDReader reader;

    public RfidEventHandler(BackgroundTaskRunner backgroundTaskRunner, RFIDReader reader) {
        this.backgroundTaskRunner = backgroundTaskRunner;
        this.reader = reader;
    }

    // Read Event Notification
    public void eventReadNotify(RfidReadEvents e) {
        // Recommended to use new method getReadTagsEx for better performance in case of large tag population
        TagData[] myTags = reader.Actions.getReadTags(100);
        if (myTags != null) {
            for (TagData myTag : myTags) {
                Log.d(LOG_TAG, "Tag ID " + myTag.getTagID());
                if (myTag.getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                        myTag.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                    if (!myTag.getMemoryBankData().isEmpty()) {
                        Log.d(LOG_TAG, " Mem Bank Data " + myTag.getMemoryBankData());
                    }
                }
            }
        }
    }

    // Status Event Notification
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
        Log.d(LOG_TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
        if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                backgroundTaskRunner.runInBackground(() -> {
                    try {
                        reader.Actions.Inventory.perform();
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Can not perform handheld trigger press...", e);
                    }
                    return null;
                });
            }
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                backgroundTaskRunner.runInBackground(() -> {
                    try {
                        reader.Actions.Inventory.stop();
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Can not stop handheld trigger release...", e);
                    }
                    return null;
                });
            }
        }
    }
}
