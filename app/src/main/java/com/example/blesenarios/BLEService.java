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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE selectedDevice.
 */
public class BLEService extends Service {
    private final static String TAG = "salis";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothGatt bluetoothGatt;
    private BluetoothDevice device;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;
    public final static String ACTION_GATT_CONNECTED = "com.example.blesenarios.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_CONNECTING = "com.example.blesenarios.ACTION_GATT_CONNECTING";
    public static final String ACTION_CONNECTED_READY_TO_USE = "com.example.blesenarios.ACTION_CONNECTED_READY_TO_USE";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.blesenarios.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED=
            "com.example.blesenarios.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.blesenarios.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.blesenarios.EXTRA_DATA";
    public static final String ACTION_SHOWSCENINFO = "com.example.blesenarios.ACTION_SHOWSCENINFO";
    public static final String ACTION_END_RECEIVING = "com.example.blesenarios.ACTION_END_RECEIVING";
    public static final String ACTION_SHOW_RESULT = "com.example.blesenarios.ACTION_SHOW_RESULT";
    public static final String ACTION_FINISH_ACTIVITY = "com.example.blesenarios.ACTION_FINISH_ACTIVITY";
    public static final String ACTION_REQ_ENABLE_BT = "com.example.blesenarios.ACTION_REQ_ENABLE_BT";

    // UUIDs for UART service and associated characteristics.
    public final static UUID UART_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    public final static UUID TX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    public final static UUID RX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    //UUID for the BLE client characteristic,necessary for notifications:
    public final static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");
    LocalBroadcastManager localBroadcastManager;


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
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        localBroadcastManager =  LocalBroadcastManager.getInstance(this);
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
                broadcastUpdate(ACTION_GATT_CONNECTED,null,null);
                Log.d(TAG, "Service-onConnectionStateChange: Connected to GATT server.");
                // Attempts to discover services after successful connection.
                if(gatt.discoverServices()){
                    Log.d(TAG,"Service-onConnectionStateChange: Services discovered.");
                }
            }
            else if (status == BluetoothGatt.GATT_SUCCESS &&  newState == BluetoothProfile.STATE_CONNECTING) {
                Log.d(TAG, "Service-onConnectionStateChange:Connecting to GATT server...");
                broadcastUpdate(ACTION_GATT_CONNECTING,null,null);
            }
            else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Service-onConnectionStateChange: Disconnected from GATT server.");
                broadcastUpdate(ACTION_GATT_DISCONNECTED,null,null);
            }
        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED,null,null);
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
                    broadcastUpdate(ACTION_CONNECTED_READY_TO_USE,null,null);
                } else {
                    Log.d(TAG,"Service-onServicesDiscovered: Error-Can not write RX client descriptor value!");
                }
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            new Thread(new Runnable() {
                public void run(){
                    broadcastUpdate(characteristic);
                    Log.d(TAG, "Service-onCharacteristicChanged-run get characteristic");
                }
            }).start();
        }
    };

    private void broadcastUpdate(String action ,@Nullable String extraName,@Nullable String extra) {
        Intent intent = new Intent(action);
        if(extraName!=null && extra!=null){
            intent.putExtra(extraName,extra);
        }
        localBroadcastManager.sendBroadcast(intent);
    }
    private void broadcastUpdate(BluetoothGattCharacteristic characteristic) {
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
        broadcastUpdate(ACTION_GATT_DISCONNECTED,null,null);
    }
}