package com.example.blesenarios;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends Activity {
    private static final String EMPTY = "";
    // UUIDs for UAT service and associated characteristics.
    public static UUID UART_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    public static UUID TX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    public static UUID RX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    // UUID for the BLE client characteristic which is necessary for notifications:
    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

    //DATA_PING is a STRING with length = 1000.
    //DATA_PING terminated with 01011
    public static String DATA_PING = "abcdefghijklmnopqrstuvwxyz01234567890fin";
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 7000;
    // UI elements:
    private TextView logs;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothDevice device;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;
    private static final int REQ_ENABLE_BT = 1221 ;
    private static final int REQ_PERMISSION_LOC = 3663 ;
    private ArrayList<BLEdevice> discoveredDevices;
    private ArrayList< BluetoothDevice > discoveredBluetoothDevice;
    ListView listView;
    ArrayAdapter<BLEdevice> discoveredDevicesAdapter;
    private ProgressDialog progressDialog;
    private Handler handler;
    private EditText input;
    TextView at_commands_tv;
    TextView commInfo_tv;
    TextView connectionStatus_tv;
    TextView sent_received_data_tv;
    Button clear_tv_btn;
    Button cal_BER_btn;
    SharedPreferences pref_currentScenario_info;
    SharedPreferences pref_currentATCommands;
    Boolean isReceivedDataPong;
    String DATA_PONG;
    float charLoss;
    float diff_length;
    float BER;
    Long tsLong;
    private void loadScenarioParameters() {
        commInfo_tv.setText("");
        if(pref_currentScenario_info==null){
            return;
        }
        String PhoneModel = pref_currentScenario_info.getString("PhoneModel", null);
        String PhoneManufacturer = pref_currentScenario_info.getString("PhoneManufacturer", null);
        String phoneBleVersion = pref_currentScenario_info.getString("phoneBleVersion",null);
        if (PhoneModel != null && PhoneManufacturer != null && phoneBleVersion!=null) {
            commInfo_tv.append("Phone=" + PhoneModel+" "+"BLE "+phoneBleVersion+" "+PhoneManufacturer);
            commInfo_tv.append("\n");
        }
        String distance = pref_currentScenario_info.getString("distance", null);
        if (distance != null) {
            commInfo_tv.append("Distance=" + distance);
            commInfo_tv.append("\n");
        }
        String inOut_door = pref_currentScenario_info.getString("inOut_door", null);
        if (inOut_door != null) {
            commInfo_tv.append("Place=" + inOut_door);
            commInfo_tv.append("\n");
        }
        String obstacle_count = pref_currentScenario_info.getString("obstacle_count", null);
        String obstacle = pref_currentScenario_info.getString("obstacle", null);
        if (obstacle_count != null && obstacle != null) {
            commInfo_tv.append("Obstacle: " + obstacle_count + " x " + obstacle);
            commInfo_tv.append("\n");
        }
        String humidity = pref_currentScenario_info.getString("humidity", null);
        if (humidity != null) {
            commInfo_tv.append("humidity=" + humidity);
            commInfo_tv.append("\n");
        }
        String wifi_status = pref_currentScenario_info.getString("wifi_status", null);
        if (wifi_status != null) {
            commInfo_tv.append("Wi-Fi= " + wifi_status);
            commInfo_tv.append("\n");
        }
        String ipv6_status = pref_currentScenario_info.getString("ipv6_status", null);
        if (ipv6_status != null) {
            commInfo_tv.append("IP-v6= " + ipv6_status);
            commInfo_tv.append("\n");
        }
        String moreExplanation = pref_currentScenario_info.getString("moreExplanation", null);
        if (moreExplanation != null) {
            commInfo_tv.append(moreExplanation);
            commInfo_tv.append("\n");
        }
        String BER = pref_currentScenario_info.getString("BER",null);
        if(BER != null){
            commInfo_tv.append("BER = " + BER);
            commInfo_tv.append("\n");
        }
        String TS =pref_currentScenario_info.getString("TS",null);
        if(TS != null){
            commInfo_tv.append("Send TimeStamp= " + TS);
            commInfo_tv.append("\n");
        }
    }
    private void loadAtCommandsParameters() {
        at_commands_tv.setText("");
        if(pref_currentATCommands==null) {
            return;
        }
//        String ModuleName = pref_currentATCommands.getString("ModuleName", null);
//        if (ModuleName != null) {
//            at_commands_tv.append(ModuleName);
//            at_commands_tv.append("\n");
//        }
        String CINT = pref_currentATCommands.getString("CINT", null);
        if (CINT != null) {
            at_commands_tv.append(CINT);
            at_commands_tv.append("\n");
        }
        String RFPM = pref_currentATCommands.getString("RFPM", null);
        if (RFPM != null) {
            at_commands_tv.append(RFPM);
            at_commands_tv.append("\n");
        }
        String AINT = pref_currentATCommands.getString("AINT", null);
        if (AINT != null) {
            at_commands_tv.append(AINT);
            at_commands_tv.append("\n");
        }
        String CTOUT = pref_currentATCommands.getString("CTOUT", null);
        if (CTOUT != null) {
            at_commands_tv.append(CTOUT);
            at_commands_tv.append("\n");
        }
        String Baud = pref_currentATCommands.getString("Baud", null);
        if (Baud != null) {
            at_commands_tv.append(Baud);
            at_commands_tv.append("\n");
        }
        String Uart = pref_currentATCommands.getString("Uart", null);
        if (Uart != null) {
            at_commands_tv.append(Uart);
            at_commands_tv.append("\n");
        }
        String LED = pref_currentATCommands.getString("LED", null);
        if (LED != null) {
            at_commands_tv.append(LED);
            at_commands_tv.append("\n");
        }
        String RestoreFactory = pref_currentATCommands.getString("RestoreFactory", null);
        if (RestoreFactory != null) {
            at_commands_tv.append(RestoreFactory);
            at_commands_tv.append("\n");
        }
    }



    // OnCreate, called once to initialize the activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        setProgressBarIndeterminate(true);
        //handler for scanning timing delay
        handler = new Handler();
        // Grab references to UI elements:
        logs = findViewById(R.id.messages);
        //scanning progress dialog:
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        //list view which shows discovered Bluetooth Devices:
        listView = findViewById(R.id.lv_devices);
        //discoveredDevices is a array which has found bluetooth devices 'name' and 'mac address'.
        discoveredDevices = new ArrayList<>();
        //discoveredBluetoothDevice is a array which has found bluetooth devices.
        discoveredBluetoothDevice=new ArrayList<>();
        discoveredDevicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, discoveredDevices);
        listView.setAdapter(discoveredDevicesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ScanLeDevice(false);
                writeLine("#"+discoveredBluetoothDevice.get(position).getName()+" scenario:");
                device = discoveredBluetoothDevice.get(position);
                SharedPreferences.Editor editor = pref_currentATCommands.edit();
                editor.putString("ModuleName",device.getName());
                editor.apply();
                /*
                make a connection with the device using the special LE-specific
                connectGatt() method,passing in a callback for GATT events
                */
                bluetoothGatt = device.connectGatt(MainActivity.this, true, gattCallback);
                writeLine("#Connecting to "+device.getName()+"...");
                connectionStatus_tv.setText("Connecting...");
            }
        });
        Button send_btn = findViewById(R.id.btn_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClick();
            }
        });
        input = findViewById(R.id.input);

        /*
        *Bluetooth in Android 4.3 is accessed via the BluetoothManager, rather than
        * the old static BluetoothAdapter.getInstance()
         */
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        //---get Parameters from Transmission and communication info activities and show---//
        at_commands_tv = findViewById(R.id.at_commands_tv);
        commInfo_tv = findViewById(R.id.comm_info_tv);
        connectionStatus_tv = findViewById(R.id.connection_status);
        sent_received_data_tv = findViewById(R.id.recv_tv);
        clear_tv_btn = findViewById(R.id.clear_tv_btn);
        clear_tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sent_received_data_tv.setText("");
            }
        });
        pref_currentScenario_info = getSharedPreferences("currentScenario_info",MODE_PRIVATE);
        pref_currentATCommands = getSharedPreferences("currentATCommands",MODE_PRIVATE);
        isReceivedDataPong = false;
        cal_BER_btn = findViewById(R.id.cal_BER_btn);
        cal_BER_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBER();
            }
        });
    }


    // Main BTLE device callback where much of the logic occurs.
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback()   {
        // Called whenever the device connection state changes, i.e. from disconnected to connected.

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_SUCCESS && newState== BluetoothProfile.STATE_CONNECTED) {
                writeLine("#GATT Connected.");
                //once connected, Discover all services on the device before we can read and
                //and write their characteristics.
                gatt.discoverServices();
                writeLine("#discovering services...");
                if (!gatt.discoverServices()) {
                    writeLine("#Failed to start discovering services!");
                    connectionStatus_tv.setText("Disconnected");
                }

            }else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                connectionStatus_tv.setText("Disconnected");
                writeLine("#Disconnected.");

            }else if(newState != BluetoothGatt.GATT_SUCCESS){
                connectionStatus_tv.setText("Disconnected");
                writeLine("#GATT Connecting was not Successful. Try again!");
                gatt.disconnect();
            }
        }

        // Called when services have been discovered on the remote device.
        // It seems to be necessary to wait for this discovery to occur before
        // manipulating any services or characteristics.
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                writeLine("#Service discovery completed.");
            } else {
                writeLine("#Service discovery failed with status: " + status);
            }

            BluetoothGattService gattService = gatt.getService(UART_UUID);
            // Save reference to each characteristic.
            if (gattService != null) {
                tx = gattService.getCharacteristic(TX_UUID);
                rx = gattService.getCharacteristic(RX_UUID);
            }
            if (rx == null) {
                writeLine("#RX Characteristic not found!");
            }
            if (tx == null) {
                writeLine("#TX Characteristic not found!");
            }

            /*
            Setup notifications on RX characteristic changes (i.e. data received).
            First call setCharacteristicNotification to enable notification.
            */
            gatt.setCharacteristicNotification(rx , true);
            if (gatt.setCharacteristicNotification(rx, true)) {
                writeLine("#Notification Enabled for RX characteristic.");
            } else {
                writeLine("#Couldn't set notifications for RX characteristic!");
            }
            // Next update the RX characteristic's client descriptor to enable notifications.
            BluetoothGattDescriptor rxGattDescriptor = rx.getDescriptor(CLIENT_UUID);
            if (rxGattDescriptor != null) {
                writeLine("#RX client descriptor is OK.");
                rxGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (!gatt.writeDescriptor(rxGattDescriptor)) {
                    writeLine("#Couldn't write RX client descriptor value!");
                } else {
                    writeLine("#Could write RX client descriptor value.");
                    connectionStatus_tv.setText(device.getName()+": UART service is ready");
                }
            } else {
                writeLine("#Couldn't get RX client descriptor!");
            }
        }

        // Called when a remote characteristic changes (like the RX characteristic).
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            //writeLine("#Received: " + characteristic.getStringValue(0))
            sent_received_data_tv.append(characteristic.getStringValue(0));
            DATA_PONG = DATA_PONG + characteristic.getStringValue(0);
        }
    };


    public void calculateBER(){
        if(DATA_PONG==null || DATA_PING==null){
            return;
        }
        String diff = difference(DATA_PING,DATA_PONG);
        diff_length = diff.length();
        charLoss = DATA_PING.length() - DATA_PONG.length();
        if(charLoss>0 && diff_length>0){  //maybe difference is because char loss.
            BER = (charLoss)/DATA_PING.length();
        }else{
            BER = (diff_length+charLoss)/DATA_PING.length();
        }
        //save BER in scenario preference
        String out =
                "\nDATA_PING:\n"+DATA_PING
                        +"\nDATA_PONG:\n"+DATA_PONG
                        +"\ndiff:"+ diff
                        +"\ndiff_length:"+diff_length
                        +"\ncharLoss: "+charLoss
                        +"\nBER: "+BER +"\n------------";
        sent_received_data_tv.append(out);
        SharedPreferences.Editor editor = pref_currentScenario_info.edit();
        editor.putString("BER",Float.toString(BER));
        editor.apply();
        loadScenarioParameters();
    }
    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return EMPTY; //
        }
        return str1.substring(at);
    }
    public static int indexOfDifference(String str1, String str2) {
        if (str1 == str2) {
            return -1;
        }
        if (str1 == null || str2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < str1.length() && i < str2.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;//i found
            }
        }
        if (i < str2.length() || i < str1.length()) {
            return i;
        }
        return -1;
    }


    // Handler for mouse click on the send button.
    public void sendClick(){
        String message = input.getText().toString();
        if (tx == null || message.isEmpty()) {
            // Do nothing if there is no device or message to send.
            return;
        }
        //send DATA_STRING_PING
        if(message.trim().equals("ping")){
            tsLong = System.currentTimeMillis()/1000;
            //get timeStamp
            String ts = tsLong.toString();
            //writeLine("#Sending DATA_PING...");
            String out = "#("+ts+")DATA_PING SESSION:\n";
            sent_received_data_tv.setText(out);
            DATA_PONG="";
            tx.setValue(DATA_PING.getBytes(Charset.forName("UTF-8")));
            //tx.setValue(DATA_PING);
            if(bluetoothGatt.writeCharacteristic(tx)) {
                //writeLine("#At "+ts+" DATA_PING Sent");
                //sent_received_data_tv.append(ts+":  DATA_PING Sent\nRECEIVED:\n");
                input.getText().clear();
            }
            //save ts timestamp
            SharedPreferences.Editor editor = pref_currentScenario_info.edit();
            editor.putString("TS",ts);
            editor.apply();
            loadScenarioParameters();
        }
        //send test input
        else {
            // Update TX characteristic value.  Note the setValue overload that takes a byte array must be used.
            tx.setValue(message.getBytes(Charset.forName("UTF-8")));
            if (bluetoothGatt.writeCharacteristic(tx)) {
                writeLine("#Sent: " + message);
                sent_received_data_tv.append("\n------------\n" +
                        "#NEW TEST SESSION:\n\nSENT:\n{"+message+"}\n\nRECEIVED:\n");
                input.getText().clear();
            }
            else {
                writeLine("#Couldn't write TX characteristic!");
                bluetoothGatt.disconnect();
            }
        }
    }


    
    // BLE device scanning callback.
    // run when a new device found in scanning.
    private LeScanCallback leScanCallback = new LeScanCallback() {
        // Called when a device is found.
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!discoveredBluetoothDevice.contains(bluetoothDevice)){
                        discoveredBluetoothDevice.add(bluetoothDevice);
                        //BLEdevice class which have each BLE device's name and mac address Strings
                        BLEdevice BLEdevice = new BLEdevice();
                        BLEdevice.setName(bluetoothDevice.getName());
                        BLEdevice.setMac(bluetoothDevice.getAddress());
                        BLEdevice.setRssi("RSSI: "+String.valueOf(rssi));
                        //discoveredDevices has device name and address and rssi
                        discoveredDevices.add(BLEdevice);
                        discoveredDevicesAdapter.notifyDataSetChanged();
                    }
                }
            });

        }

    };

    private void ScanLeDevice(final boolean enable) {

        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    setProgressBarIndeterminateVisibility(false);
                    if(device == null){
                        writeLine("Scanning finished.");
                    }
                    if(discoveredBluetoothDevice.size()==0){
                        listView.setVisibility(View.INVISIBLE);
                    }
                }
            },SCAN_PERIOD);
            discoveredDevices.clear();
            discoveredBluetoothDevice.clear();
            device = null;
            discoveredDevicesAdapter.notifyDataSetChanged();
            listView.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
            writeLine("#Scanning BLE Devices...");
            bluetoothAdapter.startLeScan(leScanCallback);
        }else {   // enable == false
            listView.setVisibility(View.INVISIBLE);
            writeLine("#Scanning Stop.");
            bluetoothAdapter.stopLeScan(leScanCallback);
            setProgressBarIndeterminateVisibility(false);
        }
    }

    // Write some text to the messages text view.
    private void writeLine(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logs.append(text);
                logs.append("\n");
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_scan:
                if(bluetoothGatt!=null){
                    bluetoothGatt.disconnect();
                    bluetoothGatt.close();
                    bluetoothGatt=null;
                }
                ScanLeDevice(true);
                return true;
            case R.id.Transmission_setting:
                //go to At-command setting activity
                startActivity(new Intent(MainActivity.this , ATCommandParametersActivity.class));
                break;
            case R.id.Environmental_setting:
                //go to communication_setting activity
                startActivity(new Intent(MainActivity.this , ScenarioInfoActivity.class));
                break;
            case R.id.disconnect:
                bluetoothGatt.disconnect();
                bluetoothGatt.close();
                bluetoothGatt = null;
                tx = null;
                rx = null;
                connectionStatus_tv.setText("Disconnected");

                break;
            case R.id.exit:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        checkPermission_ACCESS_FINE_LOCATION();
        if(!bluetoothAdapter.isEnabled() || bluetoothAdapter == null){  //is BT off
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQ_ENABLE_BT);
        }
        loadScenarioParameters();
        loadAtCommandsParameters();
        if(device != null){
            bluetoothGatt = device.connectGatt(MainActivity.this, true, gattCallback);
            writeLine("#Connecting to "+device.getName()+"...");
            connectionStatus_tv.setText("Connecting...");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
        bluetoothAdapter.stopLeScan(leScanCallback);
    }
    // OnStop, called right before the activity loses foreground focus.  Close the BTLE connection.
    @Override
    protected void onStop() {
        super.onStop();
        if (bluetoothGatt != null) {
            // For better reliability be careful to disconnect and close the connection.
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
            tx = null;
            rx = null;
        }
    }
    private void checkPermission_ACCESS_FINE_LOCATION() {
        //for some versions needs to get ACCESS_FINE_LOCATION Permission to show devices.
        //so it is vital to request for  ACCESS_FINE_LOCATION Permission:
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //permissionCheck();
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_PERMISSION_LOC);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //wait for result <--> enabling bluetooth.
        if(requestCode==REQ_ENABLE_BT){
            if(resultCode != RESULT_OK){
                Toast.makeText(this, "Bluetooth is not Enable.Closing app...", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_PERMISSION_LOC){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Denied! Closing application ...", Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

}
