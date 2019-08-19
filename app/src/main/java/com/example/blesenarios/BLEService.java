package com.example.blesenarios;

import android.annotation.SuppressLint;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BLEService extends Service {
    private final static String TAG = "BLEService";
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String bluetoothDeviceAddress;
    private BluetoothGatt bluetoothGatt;
    private BluetoothDevice device;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;
    public final static String ACTION_GATT_CONNECTED =
            "com.example.blesenarios.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_CONNECTING =
            "com.example.blesenarios.ACTION_GATT_CONNECTING";
    public static final String ACTION_CONNECTED_READY_TO_USE =
            "com.example.blesenarios.ACTION_CONNECTED_READY_TO_USE";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.blesenarios.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.blesenarios.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.blesenarios.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.blesenarios.EXTRA_DATA";
    public static final String ACTION_SHOWSCENINFO =
            "com.example.blesenarios.ACTION_SHOWSCENINFO";
    public static final String ACTION_END_RECEIVING =
            "com.example.blesenarios.ACTION_END_RECEIVING";
    public static final String ACTION_SHOW_RESULT =
            "com.example.blesenarios.ACTION_SHOW_RESULT";

    // UUIDs for UART service and associated characteristics.
    public final static UUID UART_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    public final static UUID TX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    public final static UUID RX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    //UUID for the BLE client characteristic,necessary for notifications:
    public final static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");
    public String buffer_rcv;
    private List<String> org_packetsList = new ArrayList<>();
    SharedPreferences pref_BLEService = getSharedPreferences("pref_BLEService",MODE_PRIVATE);
    SharedPreferences pref_currentScenario_info = getSharedPreferences("pref_currentScenario_info",MODE_PRIVATE);
    LocalBroadcastManager localBroadcastManager =  LocalBroadcastManager.getInstance(this);


    @Override
    public void onCreate() {
        super.onCreate();
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return;
            }
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        disconnectClose();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastUpdate(ACTION_GATT_CONNECTED,null,null);
                Log.d(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                if(gatt.discoverServices()){
                    Log.d(TAG,"Services discovered.");
                }
            }
            else if (status == BluetoothGatt.GATT_SUCCESS &&  newState == BluetoothProfile.STATE_CONNECTING) {
                Log.i(TAG, "Attempting to connect to GATT server...");
                broadcastUpdate(ACTION_GATT_CONNECTING,null,null);
            }
            else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Disconnected from GATT server.");
                broadcastUpdate(ACTION_GATT_DISCONNECTED,null,null);
            }
        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED,null,null);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
            BluetoothGattService gattService = gatt.getService(UART_UUID);
            if (gattService != null) {
                tx = gattService.getCharacteristic(TX_UUID);
                rx = gattService.getCharacteristic(RX_UUID);
            }
            if (gatt.setCharacteristicNotification(rx, true)) {
                Log.d(TAG,"gatt.setCharacteristicNotification(rx, true) --> OK");
            } else {
                Log.d(TAG,"gatt.setCharacteristicNotification(rx, true) --> FAILED!");
            }
            // Next update the RX characteristic's client descriptor to enable notifications.
            BluetoothGattDescriptor rxGattDescriptor = rx.getDescriptor(CLIENT_UUID);
            if (rxGattDescriptor == null) {
                Log.d(TAG,"rxGattDescriptor is null: Couldn't get RX client descriptor!");
            }
            else {
                Log.d(TAG,"RX client descriptor is OK");
                rxGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (gatt.writeDescriptor(rxGattDescriptor)) {
                    Log.d(TAG,"#Could write RX client descriptor value.");
                    broadcastUpdate(ACTION_CONNECTED_READY_TO_USE,null,null);
                } else {
                    Log.d("gattCallback","##Could not write RX client descriptor value!");
                }
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            new Thread(new Runnable() {
                public void run(){
                    broadcastUpdate(characteristic);
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
        }
        tx = null;
        rx = null;
        device = null;
        broadcastUpdate(ACTION_GATT_DISCONNECTED,null,null);
    }




    public boolean connect(final String address) {
        if (bluetoothAdapter == null || address == null) {
            Log.d(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (address.equals(bluetoothDeviceAddress) && bluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing bluetoothGatt for connection.");
            if (bluetoothGatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        device = bluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.d(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        bluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        bluetoothDeviceAddress = address;
        return true;
    }
    private String getTimeStamp() {
        Long tsLong = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        Date resultDate = new Date(tsLong);
        String timeStamp = sdf.format(resultDate);
        return timeStamp;
    }
    private void calculate_plp() {
        broadcastUpdate(ACTION_END_RECEIVING,null,null);
        //isEndReceiving=true;
        //setProgressBarIndeterminateVisibility(false);
        if(buffer_rcv==null || buffer_rcv.length()==0){
            return;
        }
        //eliminate *s from end of buffer
        while (buffer_rcv.substring(buffer_rcv.length()-1).equals("*")) {
            buffer_rcv = buffer_rcv.replace(buffer_rcv.substring(buffer_rcv.length() - 1), "");
        }
        //calculate plp:
        int correctPacket = 0;
        String[] rcv_packetsList = buffer_rcv.split("-");
        for (String s : rcv_packetsList) {
            if (org_packetsList.contains(s)) {
                correctPacket++;
            }
        }
        float pl = 2000 - correctPacket;
        double plp = (pl / 2000) * 100;
        plp = round(plp);

        Log.d(TAG,"cal_plp_buffer:\n"+buffer_rcv);
        Log.d(TAG,"rcv_packetsList:\n"+ Arrays.toString(rcv_packetsList));
        Log.d(TAG,"packetLoss: "+ pl);
        Log.d(TAG,"rcv_packetsList.length: "+ rcv_packetsList.length);
        Log.d(TAG,"PacketLossPercent: "+ plp);

        //show and save result:
        String result = "Received packets number: " + correctPacket+"\nLost packets number: "+pl;
        broadcastUpdate(ACTION_SHOW_RESULT,"result",result);
        //results_tv.setText("Received packets number: " + correctPacket+"\nLost packets number: "+pl);
        SharedPreferences.Editor editor = pref_currentScenario_info.edit();
        editor.putString("packetLossPercent", plp+" %");
        editor.apply();
        broadcastUpdate(ACTION_SHOWSCENINFO,null,null);
        //showScenarioInformation();
    }
    private static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private void prepare_org_packetsList() {
        org_packetsList.add("salam");
        org_packetsList.add("morad");
        org_packetsList.add("hamta");
        org_packetsList.add("odbar");
        org_packetsList.add("pedro");
        org_packetsList.add("efghi");
        org_packetsList.add("idham");
        org_packetsList.add("oodro");
        org_packetsList.add("parsi");
        org_packetsList.add("Teran");
        org_packetsList.add("99883");
        org_packetsList.add("54321");
        org_packetsList.add("95195");
        org_packetsList.add("19123");
        org_packetsList.add("75532");
        org_packetsList.add("42311");
        org_packetsList.add("54362");
        org_packetsList.add("56654");
        org_packetsList.add("42314");
        org_packetsList.add("13451");
        org_packetsList.add("ebram");
        org_packetsList.add("quran");
        org_packetsList.add("Isagh");
        org_packetsList.add("KermR");
        org_packetsList.add("Risma");
        org_packetsList.add("oodak");
        org_packetsList.add("Zebra");
        org_packetsList.add("Havij");
        org_packetsList.add("DATAN");
        org_packetsList.add("oject");
        org_packetsList.add("45600");
        org_packetsList.add("54213");
        org_packetsList.add("54374");
        org_packetsList.add("09123");
        org_packetsList.add("17720");
        org_packetsList.add("08642");
        org_packetsList.add("19191");
        org_packetsList.add("42345");
        org_packetsList.add("76876");
        org_packetsList.add("23214");
        org_packetsList.add("medal");
        org_packetsList.add("golab");
        org_packetsList.add("IRANI");
        org_packetsList.add("Seman");
        org_packetsList.add("Bravo");
        org_packetsList.add("abcde");
        org_packetsList.add("mahdi");
        org_packetsList.add("ordoo");
        org_packetsList.add("FARSI");
        org_packetsList.add("ehran");
        org_packetsList.add("23421");
        org_packetsList.add("12345");
        org_packetsList.add("44444");
        org_packetsList.add("09123");
        org_packetsList.add("75532");
        org_packetsList.add("42311");
        org_packetsList.add("54362");
        org_packetsList.add("56654");
        org_packetsList.add("42314");
        org_packetsList.add("13451");
        org_packetsList.add("javad");
        org_packetsList.add("winow");
        org_packetsList.add("WIoij");
        org_packetsList.add("dAbir");
        org_packetsList.add("Jomid");
        org_packetsList.add("pirlo");
        org_packetsList.add("naldo");
        org_packetsList.add("messi");
        org_packetsList.add("tariz");
        org_packetsList.add("BYour");
        org_packetsList.add("42121");
        org_packetsList.add("98132");
        org_packetsList.add("42141");
        org_packetsList.add("23231");
        org_packetsList.add("00000");
        org_packetsList.add("53499");
        org_packetsList.add("76544");
        org_packetsList.add("31224");
        org_packetsList.add("98763");
        org_packetsList.add("23123");
        org_packetsList.add("moham");
        org_packetsList.add("kerem");
        org_packetsList.add("tibal");
        org_packetsList.add("farda");
        org_packetsList.add("krain");
        org_packetsList.add("majid");
        org_packetsList.add("wbtel");
        org_packetsList.add("olaie");
        org_packetsList.add("ASTAN");
        org_packetsList.add("amyab");
        org_packetsList.add("53912");
        org_packetsList.add("21310");
        org_packetsList.add("54374");
        org_packetsList.add("46782");
        org_packetsList.add("09390");
        org_packetsList.add("02323");
        org_packetsList.add("98765");
        org_packetsList.add("91452");
        org_packetsList.add("91352");
        org_packetsList.add("BYE00");
    }
}