package com.zebra.rfid.reader;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

/**
 * This demonstrates a simple unit test of the Java portion of this plugin's implementation.
 * <p>
 * Once you have built the plugin's example app, you can run these tests from the command
 * line by running `./gradlew testDebugUnitTest` in the `example/android/` directory, or
 * you can run them directly from IDEs that support JUnit such as Android Studio.
 */

public class ZebraRfidReaderPluginTest {

    @Mock
    private BinaryMessenger mockMessenger;

    @Mock
    private FlutterPlugin.FlutterPluginBinding flutterPluginBinding;

    private ZebraRfidReaderPlugin plugin;

    @Before
    public void before() {
        final Context context = Mockito.mock(Context.class);

        flutterPluginBinding = Mockito.mock(FlutterPlugin.FlutterPluginBinding.class);

        Mockito.when(flutterPluginBinding.getBinaryMessenger()).thenReturn(mockMessenger);
        Mockito.when(flutterPluginBinding.getApplicationContext()).thenReturn(context);

        plugin = new ZebraRfidReaderPlugin();
        plugin.onAttachedToEngine(flutterPluginBinding);
    }

    @Test
    public void testConnect() {
        assertTrue(plugin.connect());
    }

    @Test
    public void testDisconnect() {
        assertTrue(plugin.disconnect());
    }
}
