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
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {
    private static final String EMPTY = "";
    // UUIDs for UAT service and associated characteristics.
    public static UUID UART_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
    public static UUID TX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    public static UUID RX_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB");
    //UUID for the BLE client characteristic,necessary for notifications:
    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

    //For Energy efficiency stops scanning after 5 seconds.
    private static final long SCAN_PERIOD = 5000;
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
    private ArrayList< BluetoothDevice > discoveredBluetoothDevices;
    ListView listView;
    ArrayAdapter<BLEdevice> discoveredDevicesAdapter;
    private List<String> org_strList = new ArrayList<>();
    private List<String> rssiList = new ArrayList<>();
    private List<Integer> plp_list = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Handler handler;
    private EditText input;
    TextView at_commands_tv;
    TextView scenarioInfo_tv;
    TextView connectionStatus_tv;
    TextView sent_received_data_tv;
    Button clear_tv_btn;
    Button cal_per_btn;
    Button saveToDB_btn;
    Button get_humidity_btn;
    Button showBuffer_btn;
    SharedPreferences pref_currentScenario_info;
    SharedPreferences pref_currentATCommands;
    DatabaseHelper databaseHelper;
    Boolean isStopSendDollar;
    String inComingValue;
    String buffer_rcv;

    private void prepare_org_strList() {
        org_strList.add("salam");
        org_strList.add("morad");
        org_strList.add("hamta");
        org_strList.add("odbar");
        org_strList.add("pedro");
        org_strList.add("efghi");
        org_strList.add("idham");
        org_strList.add("oodro");
        org_strList.add("parsi");
        org_strList.add("Teran");
        org_strList.add("99883");
        org_strList.add("54321");
        org_strList.add("95195");
        org_strList.add("19123");
        org_strList.add("75532");
        org_strList.add("42311");
        org_strList.add("54362");
        org_strList.add("56654");
        org_strList.add("42314");
        org_strList.add("13451");
        org_strList.add("ebram");
        org_strList.add("quran");
        org_strList.add("Isagh");
        org_strList.add("KermR");
        org_strList.add("Risma");
        org_strList.add("oodak");
        org_strList.add("Zebra");
        org_strList.add("Havij");
        org_strList.add("DATAN");
        org_strList.add("oject");
        org_strList.add("45600");
        org_strList.add("54213");
        org_strList.add("54374");
        org_strList.add("09123");
        org_strList.add("17720");
        org_strList.add("08642");
        org_strList.add("19191");
        org_strList.add("42345");
        org_strList.add("76876");
        org_strList.add("23214");
        org_strList.add("medal");
        org_strList.add("golab");
        org_strList.add("IRANI");
        org_strList.add("Seman");
        org_strList.add("Bravo");
        org_strList.add("abcde");
        org_strList.add("mahdi");
        org_strList.add("ordoo");
        org_strList.add("FARSI");
        org_strList.add("ehran");
        org_strList.add("23421");
        org_strList.add("12345");
        org_strList.add("44444");
        org_strList.add("09123");
        org_strList.add("75532");
        org_strList.add("42311");
        org_strList.add("54362");
        org_strList.add("56654");
        org_strList.add("42314");
        org_strList.add("13451");
        org_strList.add("javad");
        org_strList.add("winow");
        org_strList.add("WIoij");
        org_strList.add("dAbir");
        org_strList.add("Jomid");
        org_strList.add("pirlo");
        org_strList.add("naldo");
        org_strList.add("messi");
        org_strList.add("tariz");
        org_strList.add("BYour");
        org_strList.add("42121");
        org_strList.add("98132");
        org_strList.add("42141");
        org_strList.add("23231");
        org_strList.add("00000");
        org_strList.add("53499");
        org_strList.add("76544");
        org_strList.add("31224");
        org_strList.add("98763");
        org_strList.add("23123");
        org_strList.add("moham");
        org_strList.add("kerem");
        org_strList.add("tibal");
        org_strList.add("farda");
        org_strList.add("krain");
        org_strList.add("majid");
        org_strList.add("wbtel");
        org_strList.add("olaie");
        org_strList.add("ASTAN");
        org_strList.add("amyab");
        org_strList.add("53912");
        org_strList.add("21310");
        org_strList.add("54374");
        org_strList.add("46782");
        org_strList.add("09390");
        org_strList.add("02323");
        org_strList.add("98765");
        org_strList.add("91452");
        org_strList.add("91352");
        org_strList.add("BYE00");
    }
    private void showScenarioInformation() {
        scenarioInfo_tv.setText("");
        if(pref_currentScenario_info==null){
            return;
        }
        //obtain parameters from preferences
        String rssi = pref_currentScenario_info.getString("rssi", null);
        String phoneName = pref_currentScenario_info.getString("phoneName", null);
        String phoneManufacturer = pref_currentScenario_info.getString("phoneManufacturer", null);
        String phoneBLEVersion = pref_currentScenario_info.getString("phoneBLEVersion",null);
        String distance = pref_currentScenario_info.getString("distance", null);
        String place = pref_currentScenario_info.getString("place", null); //indoor/outdoor
        String obstacleNo = pref_currentScenario_info.getString("obstacleNo", null);
        String obstacle = pref_currentScenario_info.getString("obstacle", null);
        String wifi = pref_currentScenario_info.getString("wifi", null);
        String ipv6 = pref_currentScenario_info.getString("ipv6", null);
        String explanation = pref_currentScenario_info.getString("explanation", null);
        String humidityPercent = pref_currentScenario_info.getString("humidityPercent", null);
        String timeStamp =pref_currentScenario_info.getString("timeStamp",null);
        String packetLossPercent = pref_currentScenario_info.getString("packetLossPercent",null);
        //show parameters in scenarioInfo textView
        if (rssi != null) {
            scenarioInfo_tv.append("rssi: "+rssi);
            scenarioInfo_tv.append("\n");
        }
        if (phoneName != null && phoneManufacturer != null && phoneBLEVersion!=null) {
            scenarioInfo_tv.append(phoneManufacturer+" "+phoneName+" "+"\nBLE Version: "+phoneBLEVersion);
            scenarioInfo_tv.append("\n");
        }
        if (distance != null) {
            scenarioInfo_tv.append("distance= " +distance);
            scenarioInfo_tv.append("\n");
        }
        if (place != null) {
            scenarioInfo_tv.append(place);
            scenarioInfo_tv.append("\n");
        }
        if (obstacleNo != null && obstacle != null) {
            scenarioInfo_tv.append(obstacleNo + "x " + obstacle);
            scenarioInfo_tv.append("\n");
        }
        if (humidityPercent != null) {
            scenarioInfo_tv.append("humidityPercent= " + humidityPercent);
            scenarioInfo_tv.append("\n");
        }
        if (wifi != null) {
            scenarioInfo_tv.append("Wi-Fi: " + wifi);
            scenarioInfo_tv.append("\n");
        }
        if (ipv6 != null) {
            scenarioInfo_tv.append("ipv6: " + ipv6);
            scenarioInfo_tv.append("\n");
        }
        if(timeStamp != null){
            scenarioInfo_tv.append("timeStamp:" + timeStamp);
            scenarioInfo_tv.append("\n");
        }
        if(packetLossPercent != null){
            scenarioInfo_tv.append("packetLoss=" + packetLossPercent);
            scenarioInfo_tv.append("\n");
        }
        if (explanation != null) {
            scenarioInfo_tv.append(explanation);
        }
    }
    private void showAtCommandsParameters() {
        at_commands_tv.setText("");
        if(pref_currentATCommands==null) {
            return;
        }
        //get at commands parameters from preference:
        String moduleName = pref_currentATCommands.getString("moduleName", null);
        String moduleBLEVersion = pref_currentATCommands.getString("moduleBLEVersion",null);
        String ATDEFAULT = pref_currentATCommands.getString("ATDEFAULT", null);
        String cintMin = pref_currentATCommands.getString("cintMin", null);
        String cintMax = pref_currentATCommands.getString("cintMax", null);
        String rfpm = pref_currentATCommands.getString("rfpm", null);
        String aint = pref_currentATCommands.getString("aint", null);
        String ctout = pref_currentATCommands.getString("ctout", null);
        String baudRate = pref_currentATCommands.getString("baudRate", null);
        String led = pref_currentATCommands.getString("led", null);

        //show at commands parameters in at_commands_tv textView
        if (moduleName != null) {
            at_commands_tv.append("moduleName: "+moduleName);
            at_commands_tv.append("\n");
        }
        if (moduleBLEVersion != null) {
            at_commands_tv.append("moduleBLEVersion: "+moduleBLEVersion);
            at_commands_tv.append("\n");
        }
        if (ATDEFAULT != null) {
            at_commands_tv.append("ATDEFAULT: "+ATDEFAULT);
            at_commands_tv.append("\n");
        }
        if (cintMin != null) {
            at_commands_tv.append("cintMin: "+cintMin);
            at_commands_tv.append("\n");
        }
        if (cintMax != null) {
            at_commands_tv.append("cintMax: "+cintMax);
            at_commands_tv.append("\n");
        }
        if (rfpm != null) {
            at_commands_tv.append("rfpm: "+rfpm);
            at_commands_tv.append("\n");
        }
        if (aint != null) {
            at_commands_tv.append("aint: "+aint);
            at_commands_tv.append("\n");
        }
        if (ctout != null) {
            at_commands_tv.append("ctout: "+ctout);
            at_commands_tv.append("\n");
        }
        if (baudRate != null) {
            at_commands_tv.append("baudRate: "+baudRate);
            at_commands_tv.append("\n");
        }
        if (led != null) {
            at_commands_tv.append("led: "+led);
            at_commands_tv.append("\n");
        }
    }
    private void saveToDB() {
        //get all String data from preferences:
        String rssi = pref_currentScenario_info.getString("rssi", null);
        String phoneName = pref_currentScenario_info.getString("phoneName", null);
        String phoneManufacturer = pref_currentScenario_info.getString("phoneManufacturer", null);
        String phoneBLEVersion = pref_currentScenario_info.getString("phoneBLEVersion",null);
        String moduleName = pref_currentATCommands.getString("moduleName", null);
        String moduleBLEVersion = pref_currentATCommands.getString("moduleBLEVersion",null);
        String ATDEFAULT = pref_currentATCommands.getString("ATDEFAULT", null);
        String cintMin =pref_currentATCommands.getString("cintMin", null);
        String cintMax = pref_currentATCommands.getString("cintMax", null);
        String rfpm =pref_currentATCommands.getString("rfpm", null);
        String aint =pref_currentATCommands.getString("aint", null);
        String ctout = pref_currentATCommands.getString("ctout", null);
        String led = pref_currentATCommands.getString("led", null);
        String baudRate = pref_currentATCommands.getString("baudRate", null);
        String distance = pref_currentScenario_info.getString("distance", null);
        String place = pref_currentScenario_info.getString("place", null); //indoor/outdoor
        String obstacleNo =pref_currentScenario_info.getString("obstacleNo", null);
        String obstacle = pref_currentScenario_info.getString("obstacle", null);
        String humidityPercent = pref_currentScenario_info.getString("humidityPercent", null);
        String wifi = pref_currentScenario_info.getString("wifi", null);
        String ipv6 = pref_currentScenario_info.getString("ipv6", null);
        String timeStamp =pref_currentScenario_info.getString("timeStamp",null);
        String explanation = pref_currentScenario_info.getString("explanation", null);
        String packetLossPercent =pref_currentScenario_info.getString("packetLossPercent",null);
        //check existence of data before insertion:
        if(timeStamp == null || humidityPercent==null || packetLossPercent==null || led == null || moduleName ==null || distance==null){
            sent_received_data_tv.setText("Error: Some data does not exist for saving!");
            return;
        }
        //Phone insert
        if (!databaseHelper.insertNewPhone(phoneName,phoneManufacturer,phoneBLEVersion)) {
            sent_received_data_tv.setText("This Phone currently Exists in the database!\n");
        }else {
            sent_received_data_tv.setText("New Phone saved successfully!\n");
        }
        //Module insert
        if(!databaseHelper.insertNewModule(moduleName,moduleBLEVersion)) {
            sent_received_data_tv.append("This Module currently Exists in the database!\n");
        }else {
            sent_received_data_tv.append("New Module saved successfully!\n");
        }
        //Config insert
        if(!databaseHelper.insertNewConfig(ATDEFAULT,cintMin,cintMax,rfpm,aint,ctout,led,baudRate)){
            sent_received_data_tv.append("This Config currently Exists in the database!\n");
        }else {
            sent_received_data_tv.append("New Config saved successfully!\n");
        }

        //Scenario insert
        // I)obtain correct configId:
        Cursor configIdCursor = databaseHelper.getConfigId(ATDEFAULT,cintMin,cintMax,rfpm,aint,ctout,led,baudRate);
        if(configIdCursor.getCount()==0){
            sent_received_data_tv.append("Error: correct configId Not Found!");
            return;
        }
        StringBuilder buffer = new StringBuilder();
        while (configIdCursor.moveToNext()){
            buffer.append(configIdCursor.getString(0));
        }
        Integer configId =  Integer.parseInt(buffer.toString());
        // II)saving:
        if(!databaseHelper.insertNewScenario(configId,phoneName,moduleName,rssi,distance,place,obstacleNo,obstacle,humidityPercent,wifi,
                ipv6,timeStamp,packetLossPercent,explanation)){
            sent_received_data_tv.append("This Scenario currently Exists in the database!");
        }else {
            sent_received_data_tv.append("New Scenario saved successfully!");
        }
        }
    // OnCreate, called once to initialize the activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //Prevent the keyboard from displaying on activity start:
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        //discoveredBluetoothDevices is a array which has found bluetooth devices.
        discoveredBluetoothDevices =new ArrayList<>();
        discoveredDevicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, discoveredDevices);
        listView.setAdapter(discoveredDevicesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ScanLeDevice(false);
                writeLine("#"+ discoveredBluetoothDevices.get(position).getName()+" scenario:");
                device = discoveredBluetoothDevices.get(position);
                //save rssi in scenario information
                SharedPreferences.Editor editor1 = pref_currentScenario_info.edit();
                String rssi = rssiList.get(position);
                editor1.putString("rssi",rssi+" dBm");
                editor1.apply();
                showScenarioInformation();
                //save moduleName and moduleBLEVersion in at commands
                SharedPreferences.Editor editor2 = pref_currentATCommands.edit();
                String moduleName = device.getName();
                String moduleBLEVersion;
                if(moduleName!=null && moduleName.contains("42")) //HC-42
                    moduleBLEVersion = "v5.0";
                else if(moduleName!=null && moduleName.contains("8")) //HC-08
                    moduleBLEVersion = "v4.0";
                else
                    moduleBLEVersion="unKnown";
                editor2.putString("moduleName",moduleName);
                editor2.putString("moduleBLEVersion",moduleBLEVersion);
                editor2.apply();
                showAtCommandsParameters();
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
            @RequiresApi(api = Build.VERSION_CODES.N)
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
        scenarioInfo_tv = findViewById(R.id.comm_info_tv);
        connectionStatus_tv = findViewById(R.id.connection_status);
        sent_received_data_tv = findViewById(R.id.recv_tv);
        clear_tv_btn = findViewById(R.id.clear_tv_btn);
        clear_tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sent_received_data_tv.setText("");
            }
        });
        clear_tv_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cleanTextViewsAndPreferences();
                return false;
            }
        });
        pref_currentScenario_info = getSharedPreferences("currentScenario_info",MODE_PRIVATE);
        pref_currentATCommands = getSharedPreferences("currentATCommands",MODE_PRIVATE);
        cal_per_btn = findViewById(R.id.cal_per_btn);
        cal_per_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate_avg_packetLossPercent(plp_list);
            }
        });
        saveToDB_btn = findViewById(R.id.btn_saveToDB);
        saveToDB_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });
        get_humidity_btn=findViewById(R.id.get_humidity_btn);
        get_humidity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHumidity();
            }
        });
        showBuffer_btn = findViewById(R.id.buffer_btn);
        showBuffer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sent_received_data_tv.setText(buffer_rcv);
            }
        });
        prepare_org_strList();
            databaseHelper = new DatabaseHelper(this);
    }


    //BluetoothGattCallback: Main BLE device callback where much of the logic occurs:
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
                    String connection_state = device.getName()+": UART service is ready";
                    connectionStatus_tv.setText(connection_state);
                }
            } else {
                writeLine("#Couldn't get RX client descriptor!");
            }
        }

        // Called when a remote characteristic changes (like the RX characteristic).
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            inComingValue = characteristic.getStringValue(0);
            buffer_rcv += inComingValue;
            //sent_received_data_tv.append(inComingValue);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendClick(){
        //get message from input
        String message = input.getText().toString();
        if (tx == null) {
            // Do nothing if there is no device or message.
            return;
        }
        if(message.isEmpty()){
            message = "$";
        }
        //clear send and received data text view and clear buffer
        sent_received_data_tv.setText("");
        buffer_rcv = "";
        if(message.trim().equals("$")){
            //getting timeStamp and save it to preferences:
            Long tsLong = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS");
            Date resultDate = new Date(tsLong);
            String timeStamp = sdf.format(resultDate);
            //String timeStamp = tsLong.toString();
            SharedPreferences.Editor editor = pref_currentScenario_info.edit();
            editor.putString("timeStamp",timeStamp);
            editor.apply();
            showScenarioInformation();
            //sent message "$" 100 times - for each time calculate packet loss percent and add it to plp_list:
            plp_list.clear();
            setProgressBarIndeterminateVisibility(true);
            isStopSendDollar = false;
            final Integer n = 10;
            sendDollar(n);
        }
        //send test input
        else {
            //Update TX characteristic value.  Note the setValue overload that takes a byte array must be used.
            tx.setValue(message.getBytes(Charset.forName("UTF-8")));
            if (bluetoothGatt.writeCharacteristic(tx)) {
                //sent_received_data_tv.append("#NEW TEST SESSION:\n\nSENT:\n{"+message+"}\n\nRECEIVED:\n");
                input.getText().clear();
            }
        }
    }
    //send n $(request data from bluetooth module) each 1 second.
    private void sendDollar(final Integer n) {
        if(n==0 || isStopSendDollar){
            setProgressBarIndeterminateVisibility(false);
            return;
        }
        buffer_rcv = "";
        tx.setValue("$".getBytes(Charset.forName("UTF-8")));
        if(bluetoothGatt.writeCharacteristic(tx)) {
            //if sent successfully, wait 1 second and then add calculated packet loss percent  to plp_list
            //then send next "$"
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //after delay calculate packetLossPercent and add it to plp_list
                    Integer plp = calculate_packetLossPercent(buffer_rcv,n);
                    plp_list.add(plp);
                    sendDollar(n-1);
                }
            },0);
        }
    }
    private Integer calculate_packetLossPercent(String buffer_rcv,Integer n) {
        //calculate packet error rate(plp) after ping by using buffer_rcv content.
        int ack = 0;
        String[] rcv_strList = buffer_rcv.split("-");
        for (String s : rcv_strList) {
            if (org_strList.contains(s)) {
                ack++;
            }
        }
        int plp = 100 - ack;
        sent_received_data_tv.append("PacketLossPercent"+n.toString()+": "+plp +"%\n");
        return plp;
    }
    private void calculate_avg_packetLossPercent(List<Integer> plp_list) {
        if(plp_list.isEmpty()){
            return;
        }
        isStopSendDollar = true;
        float sum = (float) 0;
        for (Integer plp : plp_list) {
            sum += plp;
        }
        float avg = sum / plp_list.size();
        SharedPreferences.Editor editor = pref_currentScenario_info.edit();
        editor.putString("packetLossPercent", Float.toString(avg)+" %");
        editor.apply();
        showScenarioInformation();
    }
    private void getHumidity() {
        if (tx == null) {
            return;
        }
        buffer_rcv ="";
        //send request for humidityPercent...
        String message = "%";
        tx.setValue(message.getBytes(Charset.forName("UTF-8")));
        if(bluetoothGatt.writeCharacteristic(tx)) {
            input.getText().clear();
        }
        //wait until humidity received by buffer...
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //save humidity to preference:
        String humidityPercent = buffer_rcv.trim();
        SharedPreferences.Editor editor = pref_currentScenario_info.edit();
        editor.putString("humidityPercent",humidityPercent+ " %");
        editor.apply();
        showScenarioInformation(); //update scenario info for showing Humidity
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
                    if(!discoveredBluetoothDevices.contains(bluetoothDevice)) {
                        discoveredBluetoothDevices.add(bluetoothDevice);
                        //BLEdevice class which have each BLE device's name and mac address Strings
                        BLEdevice BLEdevice = new BLEdevice();
                        BLEdevice.setName(bluetoothDevice.getName());
                        BLEdevice.setMac(bluetoothDevice.getAddress());
                        BLEdevice.setRssi("RSSI: "+ rssi +"dBm");
                        //discoveredDevices has device name and address and rssi for showing
                        discoveredDevices.add(BLEdevice);
                        discoveredDevicesAdapter.notifyDataSetChanged();
                        rssiList.add(String.valueOf(rssi));
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
                    if(discoveredBluetoothDevices.size()==0){
                        listView.setVisibility(View.INVISIBLE);
                    }
                }
            },SCAN_PERIOD);
            discoveredDevices.clear();
            discoveredBluetoothDevices.clear();
            rssiList.clear();
            device = null;
            discoveredDevicesAdapter.notifyDataSetChanged();
            listView.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
            //writeLine("#Scanning BLE Devices...");
            bluetoothAdapter.startLeScan(leScanCallback);
        }else {   // enable == false
            listView.setVisibility(View.INVISIBLE);
            //writeLine("#Scanning Stop.");
            bluetoothAdapter.stopLeScan(leScanCallback);
            setProgressBarIndeterminateVisibility(false);
        }
    }
    private void cleanTextViewsAndPreferences() {
        scenarioInfo_tv.setText("");
        at_commands_tv.setText("");
        sent_received_data_tv.setText("");
        SharedPreferences.Editor editor1;
        editor1 = pref_currentATCommands.edit();
        editor1.clear();
        editor1.apply();
        SharedPreferences.Editor editor2;
        editor2 = pref_currentScenario_info.edit();
        editor2.clear();
        editor2.apply();
        buffer_rcv = "";
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
                    isStopSendDollar = true;
                    bluetoothGatt.disconnect();
                    bluetoothGatt.close();
                    bluetoothGatt=null;
                }
                if(tx!=null || rx !=null){
                    tx = null;
                    rx = null;
                }
                connectionStatus_tv.setText("Disconnected");
                ScanLeDevice(true);
                break;
            case R.id.AtCommandConfigs:
                //go to At-command setting activity if moduleName is recognized
                String moduleName = pref_currentATCommands.getString("moduleName", null);
                if(moduleName!=null){
                    startActivity(new Intent(MainActivity.this , ATCommandParametersActivity.class));
                }else {
                    sent_received_data_tv.setText("Error: bluetooth Module is not recognized!");
                }
                break;
            case R.id.ScenarioInformation:
                //go to communication_setting activity
                startActivity(new Intent(MainActivity.this , ScenarioInformationActivity.class));
                break;
            case R.id.ScenariosReports:
                //go to report activity
                startActivity(new Intent(MainActivity.this , ScenariosReports.class));
                break;
            case R.id.disconnect:
                if(bluetoothGatt != null){
                    isStopSendDollar = true;
                    bluetoothGatt.disconnect();
                    bluetoothGatt.close();
                    bluetoothGatt = null;
                }
                if(tx!=null || rx !=null){
                    tx = null;
                    rx = null;
                }
                connectionStatus_tv.setText("Disconnected");
                break;

            case R.id.exit:
                if(bluetoothGatt!=null){
                    isStopSendDollar = true;
                    bluetoothGatt.disconnect();
                    bluetoothGatt.close();
                    bluetoothGatt=null;
                }
                if(tx!=null || rx !=null){
                    tx = null;
                    rx = null;
                }
                connectionStatus_tv.setText("Disconnected");
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
        showScenarioInformation();
        showAtCommandsParameters();
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
