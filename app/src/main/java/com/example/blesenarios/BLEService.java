package com.example.blesenarios;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE selectedDevice.
 */
public class BLEService extends Service {
    private final static String TAG = "salis";
    private BluetoothGatt bluetoothGatt;
    private BluetoothDevice device;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;
    private LocalBroadcastManager localBroadcastManager;
    public final static String ACTION_DISCONNECTED = "com.example.blesenarios.ACTION_DISCONNECTED";
    public final static String ACTION_CONNECTING = "com.example.blesenarios.ACTION_CONNECTING";
    public final static String ACTION_CONNECTED = "com.example.blesenarios.ACTION_CONNECTED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.blesenarios.ACTION_DATA_AVAILABLE";
    public final static String ACTION_DATA_FOR_SEND = "com.example.blesenarios.ACTION_DATA_FOR_SEND";
    // UUIDs for UART service and associated characteristics.
    public final static UUID UART_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    public final static UUID TX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    public final static UUID RX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    //UUID for the BLE client characteristic,necessary for notifications:
    public final static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service-onBind");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service-onCreate");
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        localBroadcastManager =  LocalBroadcastManager.getInstance(this);
        if(!bluetoothAdapter.enable()){
            stopSelf();
        }
        IntentFilter Filter_sendMessage = new IntentFilter(ACTION_DATA_FOR_SEND);
        BroadcastReceiver receiver_sendMessage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                if(tx == null || message ==null || message.trim().isEmpty()){
                    return;
                }
                tx.setValue(message.getBytes(Charset.forName("UTF-8")));
                if(bluetoothGatt.writeCharacteristic(tx)) {
                    Log.d(TAG,"Service-receiver_sendMessage: Message sent to module");
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_sendMessage,Filter_sendMessage);
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        device = intent.getExtras().getParcelable("device");
        Log.d(TAG, "Service onStart:<Device:"+device+">");
        bluetoothGatt =  device.connectGatt(this,true,mGattCallback);
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "Service-onDestroy");
        disconnectClose();
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service-onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastAction(ACTION_CONNECTED);
                Log.d(TAG, "Service-onConnectionStateChange: Connected to GATT server.");
                // Attempts to discover services after successful connection.
                if(gatt.discoverServices()){
                    Log.d(TAG,"Service-onConnectionStateChange: Services discovered.");
                }
            }
            else if (status == BluetoothGatt.GATT_SUCCESS &&  newState == BluetoothProfile.STATE_CONNECTING) {
                Log.d(TAG, "Service-onConnectionStateChange:Connecting to GATT server...");
                broadcastAction(ACTION_CONNECTING);
            }
            else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Service-onConnectionStateChange: Disconnected from GATT server.");
                broadcastAction(ACTION_DISCONNECTED);
            }
        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Service-onServicesDiscovered: Gatt Success.");
            } else {
                Log.w(TAG, "Service-onServicesDiscovered: onServicesDiscovered received: " + status);
            }
            BluetoothGattService gattService = gatt.getService(UART_UUID);
            if (gattService != null) {
                tx = gattService.getCharacteristic(TX_UUID);
                rx = gattService.getCharacteristic(RX_UUID);
                Log.d(TAG, "Service-onServicesDiscovered-tx:"+tx);
                Log.d(TAG, "Service-onServicesDiscovered-rx:"+rx);
            }
            if (gatt.setCharacteristicNotification(rx, true)) {
                Log.d(TAG,"Service-onServicesDiscovered: Notification enabled.");
            } else {
                Log.d(TAG,"Service-onServicesDiscovered: Error(Notification Not enable!)");
            }
            // Next update the RX characteristic's client descriptor to enable notifications.
            BluetoothGattDescriptor rxGattDescriptor = rx.getDescriptor(CLIENT_UUID);
            if (rxGattDescriptor == null) {
                Log.d(TAG,"Service-onServicesDiscovered:\nError:rxGattDescriptor is null-Couldn't get RX client descriptor!");
            }
            else {
                Log.d(TAG,"Service-onServicesDiscovered: RX client descriptor is OK");
                rxGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (gatt.writeDescriptor(rxGattDescriptor)) {
                    Log.d(TAG,"Service-onServicesDiscovered: All things is OK.Can write RX client descriptor value.");
                    broadcastAction(ACTION_CONNECTED);
                } else {
                    Log.d(TAG,"Service-onServicesDiscovered: Error-Can not write RX client descriptor value!");
                }
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            new Thread(new Runnable() {
                public void run(){
                    broadcastChar(characteristic);
                    Log.d(TAG, "Service-onCharacteristicChanged: get characteristic from module");
                }
            }).start();
        }
    };

    private void broadcastAction(String action) {
        Intent intent = new Intent(action);
        localBroadcastManager.sendBroadcast(intent);
    }
    private void broadcastChar(BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(BLEService.ACTION_DATA_AVAILABLE);
        String data = characteristic.getStringValue(0);
        intent.putExtra("data", data);
        localBroadcastManager.sendBroadcast(intent);
    }
    private void disconnectClose() {
        if(bluetoothGatt!=null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
            Log.d(TAG, "Service-disconnectClose step 1");
        }
        tx = null;
        rx = null;
        device = null;
        Log.d(TAG, "Service-disconnectClose step 2");
        broadcastAction(ACTION_DISCONNECTED);
    }
}