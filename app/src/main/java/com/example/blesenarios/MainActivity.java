package com.example.blesenarios;
import android.Manifest;
import android.annotation.SuppressLint;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import java.util.Arrays;
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
    private List<String> org_packetsList = new ArrayList<>();
    private List<String> rssiList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Handler handler;
    private EditText input;
    TextView at_commands_tv;
    TextView scenarioInfo_tv;
    TextView connectionStatus_tv;
    TextView sent_received_data_tv;
    Button clear_tv_btn;
    Button cal_plp_btn;
    Button saveToDB_btn;
    Button get_humidity_btn;
    Button showBuffer_btn;
    SharedPreferences pref_currentScenario_info;
    SharedPreferences pref_currentATCommands;
    DatabaseHelper databaseHelper;
    Boolean isEndReceiving;
    String inComingValue;
    String buffer_rcv;

    private void prepare_org_strList() {
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
        String distanceMin = pref_currentScenario_info.getString("distanceMin", null);
        String distanceMax = pref_currentScenario_info.getString("distanceMax", null);
        String place = pref_currentScenario_info.getString("place", null); //indoor/outdoor
        String obstacleNo = pref_currentScenario_info.getString("obstacleNo", null);
        String obstacle = pref_currentScenario_info.getString("obstacle", null);
        String wifi = pref_currentScenario_info.getString("wifi", null);
        String ipv6 = pref_currentScenario_info.getString("ipv6", null);
        String explanation = pref_currentScenario_info.getString("explanation", null);
        String humidityPercent = pref_currentScenario_info.getString("humidityPercent", null);
        String startTimeStamp =pref_currentScenario_info.getString("startTimeStamp",null);
        String endTimeStamp =pref_currentScenario_info.getString("endTimeStamp",null);
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
        if (distanceMin != null) {
            scenarioInfo_tv.append("distanceMin= " +distanceMin+" m");
            scenarioInfo_tv.append("\n");
        }
        if (distanceMax != null) {
            scenarioInfo_tv.append("distanceMax= " +distanceMax+" m");
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
        if(startTimeStamp != null){
            scenarioInfo_tv.append("startTimeStamp:" + startTimeStamp);
            scenarioInfo_tv.append("\n");
        }
        if(packetLossPercent != null){
            scenarioInfo_tv.append("packetLoss=" + packetLossPercent);
            scenarioInfo_tv.append("\n");
        }
        if(endTimeStamp != null){
            scenarioInfo_tv.append("endTimeStamp:" + endTimeStamp);
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
        String pm = pref_currentATCommands.getString("pm", null);
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
        if (pm != null) {
            at_commands_tv.append("pm: "+pm);
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
        String pm = pref_currentATCommands.getString("pm", null);
        String distanceMin = pref_currentScenario_info.getString("distanceMin", null);
        String distanceMax = pref_currentScenario_info.getString("distanceMax", null);
        String place = pref_currentScenario_info.getString("place", null); //indoor/outdoor
        String obstacleNo =pref_currentScenario_info.getString("obstacleNo", null);
        String obstacle = pref_currentScenario_info.getString("obstacle", null);
        String humidityPercent = pref_currentScenario_info.getString("humidityPercent", null);
        String wifi = pref_currentScenario_info.getString("wifi", null);
        String ipv6 = pref_currentScenario_info.getString("ipv6", null);
        String startTimeStamp =pref_currentScenario_info.getString("startTimeStamp",null);
        String endTimeStamp =pref_currentScenario_info.getString("endTimeStamp",null);
        String explanation = pref_currentScenario_info.getString("explanation", null);
        String packetLossPercent =pref_currentScenario_info.getString("packetLossPercent",null);
        //check existence of data before insertion:
        if(startTimeStamp == null || humidityPercent==null || packetLossPercent==null ||
                led == null || moduleName ==null || distanceMin==null || distanceMax==null){
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
        if(!databaseHelper.insertNewConfig(ATDEFAULT,cintMin,cintMax,rfpm,aint,ctout,led,baudRate,pm)){
            sent_received_data_tv.append("This Config currently Exists in the database!\n");
        }else {
            sent_received_data_tv.append("New Config saved successfully!\n");
        }

        //Scenario insert
        // I)obtain correct configId:
        Cursor configIdCursor = databaseHelper.getConfigId(ATDEFAULT,cintMin,cintMax,rfpm,aint,ctout,led,baudRate,pm);
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
        if(!databaseHelper.insertNewScenario(configId,phoneName,moduleName,rssi,distanceMin,distanceMax,place,obstacleNo,obstacle,humidityPercent,wifi,
                ipv6,startTimeStamp,endTimeStamp,packetLossPercent,explanation)){
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
            @SuppressLint("SetTextI18n")
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
                buffer_rcv="";
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
        cal_plp_btn = findViewById(R.id.cal_plp_btn);
        cal_plp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal_plp();
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
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            new Thread(new Runnable() {
                public void run(){
                    inComingValue = characteristic.getStringValue(0);
                    Log.d("salis",".\n");
                    buffer_rcv += inComingValue;
                    if(buffer_rcv.substring(buffer_rcv.length()-1).equals("*")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String timeStamp = getTimeStamp();
                                SharedPreferences.Editor editor = pref_currentScenario_info.edit();
                                editor.putString("endTimeStamp",timeStamp);
                                editor.apply();
                                showScenarioInformation();
                                cal_plp();
                            }
                        });
                    }
                }
            }).start();
        }
    };



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
            String timeStamp = getTimeStamp();
            SharedPreferences.Editor editor = pref_currentScenario_info.edit();
            editor.putString("startTimeStamp",timeStamp);
            editor.apply();
            showScenarioInformation();
            setProgressBarIndeterminateVisibility(true);
            isEndReceiving = false;
            buffer_rcv = "";
            tx.setValue("$".getBytes(Charset.forName("UTF-8")));
            if(bluetoothGatt.writeCharacteristic(tx)) {
                Log.d("salis","$ sent");
            }
            //terminate scenario after 15 s
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!isEndReceiving){
                        String timeStamp = getTimeStamp();
                        SharedPreferences.Editor editor = pref_currentScenario_info.edit();
                        editor.putString("endTimeStamp",timeStamp);
                        editor.apply();
                        showScenarioInformation();
                        cal_plp();
                    }
                }
            },15000);
        }
        //send test input
        else {
            //Update TX characteristic value.  Note the setValue overload that takes a byte array must be used.
            tx.setValue(message.getBytes(Charset.forName("UTF-8")));
            if (bluetoothGatt.writeCharacteristic(tx)) {
                input.getText().clear();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sent_received_data_tv.append(buffer_rcv);
                    }
                },message.length()*200);
            }
        }
    }

    private String getTimeStamp() {
        Long tsLong = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        Date resultDate = new Date(tsLong);
        String timeStamp = sdf.format(resultDate);
        return timeStamp;
    }

    private void cal_plp() {
        isEndReceiving=true;
        setProgressBarIndeterminateVisibility(false);
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
        plp = round(plp,2);

        Log.d("salis","cal_plp_buffer:\n"+buffer_rcv);
        Log.d("salis","rcv_packetsList:\n"+ Arrays.toString(rcv_packetsList));
        Log.d("salis","packetLoss: "+ pl);
        Log.d("salis","rcv_packetsList.length: "+ rcv_packetsList.length);
        Log.d("salis","PacketLossPercent: "+ plp);

        //show and save result:
        sent_received_data_tv.setText("Received packet number: " + correctPacket);
        sent_received_data_tv.append("\nLost packet number: "+pl);
        SharedPreferences.Editor editor = pref_currentScenario_info.edit();
        editor.putString("packetLossPercent", plp+" %");
        editor.apply();
        showScenarioInformation();


    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
                    isEndReceiving = true;
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
                disconnect();
                break;

            case R.id.exit:
                disconnect();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void disconnect() {
        if(bluetoothGatt!=null){
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt=null;
            tx = null;
            rx = null;
        }
        setProgressBarIndeterminateVisibility(false);
        isEndReceiving = true;
        connectionStatus_tv.setText("Disconnected");
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
