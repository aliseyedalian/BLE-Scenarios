package com.example.blesenarios;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    public static final String TAG = "salis";
    private static final int REQ_ENABLE_BT = 1221 ;
    private static final int REQ_PERMISSION_LOC = 3663 ;
    //For Energy efficiency stops scanning after 4 seconds.
    private static final long SCAN_PERIOD = 4000;
    private static final int REQUEST_EXTERNAL_STORAGE = 1235;
    private static String[] PERMISSIONS_STORAGE={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //actions for communication with BLEService by broadcasting
    public final static String ACTION_DISCONNECTED = "com.example.blesenarios.ACTION_DISCONNECTED";
    public final static String ACTION_CONNECTING = "com.example.blesenarios.ACTION_CONNECTING";
    public final static String ACTION_CONNECTED = "com.example.blesenarios.ACTION_CONNECTED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.blesenarios.ACTION_DATA_AVAILABLE";
    public final static String ACTION_DATA_FOR_SEND = "com.example.blesenarios.ACTION_DATA_FOR_SEND";
    private LocalBroadcastManager localBroadcastManager;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice selectedDevice;
    ArrayList<BLEDevice> discoveredDevices_properties;
    ArrayList< BluetoothDevice > discoveredDevices_objects;
    ListView listView;
    LinearLayout scannedDevicesList_layout;
    ArrayAdapter<BLEDevice> discoveredDevicesAdapter;
    List<String> org_packetsList = new ArrayList<>();
    List<String> rssiList = new ArrayList<>();
    ProgressDialog progressDialog;
    Handler handler;
    EditText input;
    TextView at_commands_tv;
    TextView scenarioInfo_tv;
    TextView connectionStatus_tv;
    TextView results_tv;
    Button rescan_btn;
    SharedPreferences pref_current_Scen_info;
    SharedPreferences pref_currentATCommands;
    DatabaseHelper databaseHelper;
    Boolean isEndReceiving;
    Boolean isFinishScan;
    String receive_Buffer;
    Intent bleService_intent;


    private void prepare_original_packetsList() {
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
        if(pref_current_Scen_info ==null){
            return;
        }
        //obtain parameters from preferences
        String rssi = pref_current_Scen_info.getString("rssi", null);
        String phoneName = pref_current_Scen_info.getString("phoneName", null);
        String phoneManufacturer = pref_current_Scen_info.getString("phoneManufacturer", null);
        String phoneBLEVersion = pref_current_Scen_info.getString("phoneBLEVersion",null);
        String distanceMin = pref_current_Scen_info.getString("distanceMin", null);
        String distanceMax = pref_current_Scen_info.getString("distanceMax", null);
        String place = pref_current_Scen_info.getString("place", null); //indoor/outdoor
        String obstacleNo = pref_current_Scen_info.getString("obstacleNo", null);
        String obstacle = pref_current_Scen_info.getString("obstacle", null);
        String wifi = pref_current_Scen_info.getString("wifi", null);
        String ipv6 = pref_current_Scen_info.getString("ipv6", null);
        String explanation = pref_current_Scen_info.getString("explanation", null);
        String humidityPercent = pref_current_Scen_info.getString("humidityPercent", null);
        String startTimeStamp = pref_current_Scen_info.getString("startTimeStamp",null);
        String endTimeStamp = pref_current_Scen_info.getString("endTimeStamp",null);
        String packetLossPercent = pref_current_Scen_info.getString("packetLossPercent",null);
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
            scenarioInfo_tv.append("humidityPercent=" + humidityPercent);
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
        String rssi = pref_current_Scen_info.getString("rssi", null);
        String phoneName = pref_current_Scen_info.getString("phoneName", null);
        String phoneManufacturer = pref_current_Scen_info.getString("phoneManufacturer", null);
        String phoneBLEVersion = pref_current_Scen_info.getString("phoneBLEVersion",null);
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
        String distanceMin = pref_current_Scen_info.getString("distanceMin", null);
        String distanceMax = pref_current_Scen_info.getString("distanceMax", null);
        String place = pref_current_Scen_info.getString("place", null); //indoor/outdoor
        String obstacleNo = pref_current_Scen_info.getString("obstacleNo", null);
        String obstacle = pref_current_Scen_info.getString("obstacle", null);
        String humidityPercent = pref_current_Scen_info.getString("humidityPercent", null);
        String wifi = pref_current_Scen_info.getString("wifi", null);
        String ipv6 = pref_current_Scen_info.getString("ipv6", null);
        String startTimeStamp = pref_current_Scen_info.getString("startTimeStamp",null);
        String endTimeStamp = pref_current_Scen_info.getString("endTimeStamp",null);
        String explanation = pref_current_Scen_info.getString("explanation", null);
        String packetLossPercent = pref_current_Scen_info.getString("packetLossPercent",null);
        //check existence of data before insertion:
        if(startTimeStamp == null || humidityPercent==null || packetLossPercent==null ||
                led == null || moduleName ==null || distanceMin==null || distanceMax==null){
            results_tv.setText("Error: Some data does not exist for saving!");
            return;
        }
        //Phone insert
        if (!databaseHelper.insertNewPhone(phoneName,phoneManufacturer,phoneBLEVersion)) {
            Log.d(TAG, "saveToDB: This Phone currently Exists in the database!");
            //results_tv.setText("This Phone currently Exists in the database!\n");
        }else {
            Log.d(TAG, "saveToDB: New Phone saved successfully!");
            //results_tv.setText("New Phone saved successfully!\n");
        }
        //Module insert
        if(!databaseHelper.insertNewModule(moduleName,moduleBLEVersion)) {
            Log.d(TAG, "saveToDB: This Module currently Exists in the database!");
            //results_tv.append("This Module currently Exists in the database!\n");
        }else {
            Log.d(TAG, "saveToDB: New Module saved successfully!");
            //results_tv.append("New Module saved successfully!\n");
        }
        //Config insert
        if(!databaseHelper.insertNewConfig(ATDEFAULT,cintMin,cintMax,rfpm,aint,ctout,led,baudRate,pm)){
            Log.d(TAG, "saveToDB: This Config currently Exists in the database!");
            //results_tv.append("This Config currently Exists in the database!\n");
        }else {
            Log.d(TAG, "saveToDB: New Config saved successfully!");
            //results_tv.append("New Config saved successfully!\n");
        }

        //Scenario insert
        // I)obtain correct configId:
        Cursor configIdCursor = databaseHelper.getConfigId(ATDEFAULT,cintMin,cintMax,rfpm,aint,ctout,led,baudRate,pm);
        if(configIdCursor.getCount()==0){
            //results_tv.append("Error: correct configId Not Found!");
            Log.e(TAG, "saveToDB: Error: correct configId Not Found!");
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
            results_tv.setText("This Scenario is saved!");
            Log.d(TAG, "saveToDB: This Scenario is saved!");
        }else {
            results_tv.setText("New Scenario saved successfully.");
            Log.d(TAG, "saveToDB: New Scenario saved successfully.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity-onCreate");
        //Prevent the keyboard from displaying on activity start:
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //progress
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminate(true);
        setContentView(R.layout.activity_main);
        //scanning progress dialog:
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        //handler used for create delay
        handler = new Handler();

        //create shared preference for saving current scenario information and at commands...
        pref_current_Scen_info = getSharedPreferences("currentScenario_info",MODE_PRIVATE);
        pref_currentATCommands = getSharedPreferences("currentATCommands",MODE_PRIVATE);

        initViews();

        //create a intent for BLEService
        bleService_intent = new Intent(this,BLEService.class);

        //discoveredDevices_properties is a array which has 'name' and 'mac address' and 'rssi' of each found device.
        discoveredDevices_properties = new ArrayList<>();
        //discoveredDevices_objects is a array which has found bluetooth devices.
        discoveredDevices_objects =new ArrayList<>();
        discoveredDevicesAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, discoveredDevices_properties);
        listView.setAdapter(discoveredDevicesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ScanLeDevice(false);
                selectedDevice = discoveredDevices_objects.get(position);
                //save rssi in scenario information
                SharedPreferences.Editor editor1 = pref_current_Scen_info.edit();
                String rssi = rssiList.get(position);
                editor1.putString("rssi",rssi+" dBm");
                editor1.apply();
                showScenarioInformation();
                //save moduleName and moduleBLEVersion in at commands
                SharedPreferences.Editor editor2 = pref_currentATCommands.edit();
                String moduleName = selectedDevice.getName();

                //show version of bluetooth according to its name. it can develop more...
                String moduleBLEVersion;
                if(moduleName!=null && moduleName.contains("42")) //HC-42
                    moduleBLEVersion = "v5.0";
                else if(moduleName!=null && moduleName.contains("8")) //HC-08
                    moduleBLEVersion = "v4.0";
                else
                    moduleBLEVersion="unKnown";
                //save moduleName and moduleBLEVersion
                editor2.putString("moduleName",moduleName);
                editor2.putString("moduleBLEVersion",moduleBLEVersion);
                editor2.apply();
                showAtCommandsParameters();

                //send selected device to BLEService for start connection
                Log.d(TAG, "MainActivity-onItemClick: selectedDevice: "+ selectedDevice);
                //send selectedDevice to service to establish connection and start service
                bleService_intent.putExtra("device", selectedDevice);
                startService(bleService_intent);
                setConnectionStatusTextView("CONNECTING...","#ffff00");
                Log.d(TAG,"MainActivity-onItemClick: BLEService start");
            }
        }); //setOnItemClickListener closed

        //use database for final saving scenario
        databaseHelper = new DatabaseHelper(this);

        //the ble module will send some strings and phone will evaluate them.so phone must know them.
        prepare_original_packetsList();

        /**registering LocalBroadcast for receiving data from BLEService: */
        IntentFilter Filter_connected = new IntentFilter(ACTION_CONNECTED);
        BroadcastReceiver receiver_connected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setConnectionStatusTextView("CONNECTED TO "+selectedDevice.getName(),"#00ff00");
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_connected,Filter_connected);

        IntentFilter Filter_connecting = new IntentFilter(ACTION_CONNECTING);
        BroadcastReceiver receiver_connecting = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setConnectionStatusTextView("CONNECTING","#ffff00");
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_connecting,Filter_connecting);

        IntentFilter Filter_disconnected = new IntentFilter(ACTION_DISCONNECTED);
        BroadcastReceiver receiver_disconnected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setConnectionStatusTextView("DISCONNECTED","#ffff00");
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_disconnected,Filter_disconnected);

        IntentFilter Filter_data = new IntentFilter(ACTION_DATA_AVAILABLE);
        BroadcastReceiver receiver_data = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
            new Thread(new Runnable() {
                public void run(){
                    Log.d(TAG,"MainActivity-Receiving from the BLE Module...\n");
                    final String data = intent.getStringExtra("data");
                    receive_Buffer += data;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            results_tv.append(data);
                        }
                    });
                    if(receive_Buffer.substring(receive_Buffer.length()-1).equals("*")){ //the * shows the end.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String endTimeStamp = getTimeStamp();
                                SharedPreferences.Editor editor = pref_current_Scen_info.edit();
                                editor.putString("endTimeStamp",endTimeStamp);
                                editor.apply();
                                showScenarioInformation();
                                calculate_plp();
                            }
                        });
                    }
                }
            }).start();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_data,Filter_data);

        /**localBroadcastManager for sending data to BLEService: */
        localBroadcastManager =  LocalBroadcastManager.getInstance(this);
        startScan();
    }

    private void initViews() {
        listView = findViewById(R.id.lv_devices);//list view which shows discovered Bluetooth Devices:
        scannedDevicesList_layout = findViewById(R.id.scannedDevicesList_layout);
        input = findViewById(R.id.input);
        Button send_btn = findViewById(R.id.btn_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //get message from input
                String message = input.getText().toString().trim();
                send(message);
            }
        });

        Button start_btn = findViewById(R.id.btn_startScenario);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send("$");
            }
        });

        at_commands_tv = findViewById(R.id.at_commands_tv);
        scenarioInfo_tv = findViewById(R.id.comm_info_tv);
        connectionStatus_tv = findViewById(R.id.connection_status);
        results_tv = findViewById(R.id.results_tv);
        Button plp_btn = findViewById(R.id.plp_btn);
        plp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate_plp();
            }
        });

        final Button clean_tv_btn = findViewById(R.id.clean_tv_btn);
        clean_tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results_tv.setText("");
                receive_Buffer ="";
            }
        });
        clean_tv_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clean();
                return false;
            }
        });
        Button saveToDB_btn = findViewById(R.id.btn_saveToDB);
        saveToDB_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });
        Button get_humidity_btn=findViewById(R.id.get_humidity_btn);
        get_humidity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHumidity();
            }
        });
        Button showBuffer_btn = findViewById(R.id.buffer_btn);
        showBuffer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results_tv.setText(receive_Buffer);
            }
        });
        Button close_scanList_btn = findViewById(R.id.close_scanList_btn);
        close_scanList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanLeDevice(false);
                setConnectionStatusTextView("DISCONNECTED","#ffff00");
            }
        });
        rescan_btn = findViewById(R.id.rescan_btn);
        rescan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanLeDevice(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity-onResume");
        //check system has bluetooth
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //ble not supported by phone!
            finish();
        }
        //check bluetooth is ON
        if(!bluetoothAdapter.isEnabled() || bluetoothAdapter == null){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQ_ENABLE_BT);
        }
        checkPermission_ACCESS_FINE_LOCATION();
        verifyStoragePermissions(this);

        //update AT Commands and Scenario Information Text Views:
        showScenarioInformation();
        showAtCommandsParameters();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity-onPause");
        progressDialog.dismiss();
        bluetoothAdapter.stopLeScan(leScanCallback);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity-onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity-onDestroy");
        disconnectClose();
    }



    public void send(String message){
        if(message.trim().isEmpty()){
            return;
        }
        results_tv.setText("");
        receive_Buffer = "";
        if(message.equals("$")){ //start scenario command
            //getting timeStamp and save it to preferences:
            Log.d(TAG, "MainActivity-send: Scenario Start");
            String timeStamp = getTimeStamp();
            SharedPreferences.Editor editor = pref_current_Scen_info.edit();
            editor.putString("startTimeStamp",timeStamp);
            editor.apply();
            showScenarioInformation();
            setProgressBarIndeterminateVisibility(true);
            isEndReceiving = false;
            broadcastMessage(message);
            Log.d(TAG, "MainActivity-send: $ sent to BLEService");
            //force terminate scenario after 15 seconds
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!isEndReceiving){
                        Log.d(TAG, "MainActivity-run: force terminate scenario after 15 seconds");
                        String timeStamp = getTimeStamp();
                        SharedPreferences.Editor editor = pref_current_Scen_info.edit();
                        editor.putString("endTimeStamp",timeStamp);
                        editor.apply();
                        showScenarioInformation();
                        calculate_plp();
                    }
                }
            },15000);
        }
        //send message
        else {
            //Update TX characteristic value.  Note the setValue overload that takes a byte array must be used.
            broadcastMessage(message);
            input.getText().clear();
        }
    }
    private void getHumidity() {
        Log.d(TAG, "getHumidity: ");
        receive_Buffer ="";
        results_tv.setText("");
        //send request for humidityPercent...
        String message = "%";
        broadcastMessage(message);
        //after a few millisecond read the humidity from buffer
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //save humidity to preference:
                String humidityPercent = receive_Buffer.trim();
                String result = humidityPercent+" % humidity";
                results_tv.setText(result);
                Log.d(TAG, "humidityPercent="+humidityPercent);
                if(!humidityPercent.isEmpty()){
                    SharedPreferences.Editor editor = pref_current_Scen_info.edit();
                    editor.putString("humidityPercent",humidityPercent+ "%");
                    editor.apply();
                    showScenarioInformation(); //update scenario info for showing Humidity
                }
            }
        },300);
    }
    private void broadcastMessage(String message) {
        Intent intent = new Intent(ACTION_DATA_FOR_SEND);
        intent.putExtra("message",message);
        localBroadcastManager.sendBroadcast(intent);
    }
    private String getTimeStamp() {
        Long tsLong = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        Date resultDate = new Date(tsLong);
        String timeStamp = sdf.format(resultDate);
        return timeStamp;
    }
    private void calculate_plp() {
        isEndReceiving=true;
        setProgressBarIndeterminateVisibility(false);
        if(receive_Buffer ==null || receive_Buffer.length()==0){
            return;
        }
        //eliminate *s from end of buffer
        while (receive_Buffer.substring(receive_Buffer.length()-1).equals("*")) {
            receive_Buffer = receive_Buffer.replace(receive_Buffer.substring(receive_Buffer.length() - 1), "");
        }
        //calculate plp:
        int correctPacket = 0;
        String[] rcv_packetsList = receive_Buffer.split("-");
        for (String s : rcv_packetsList) {
            if (org_packetsList.contains(s)) {
                correctPacket++;
            }
        }
        float pl = 2000 - correctPacket;
        double plp = (pl / 2000) * 100;
        plp = round(plp,2);

        Log.d(TAG,"cal_plp_buffer:\n"+ receive_Buffer);
        Log.d(TAG,"rcv_packetsList:\n"+ Arrays.toString(rcv_packetsList));
        Log.d(TAG,"packetLoss: "+ pl);
        Log.d(TAG,"rcv_packetsList.length: "+ rcv_packetsList.length);
        Log.d(TAG,"PacketLossPercent: "+ plp);

        //show and save result:
        String result = "Received packets number: " + correctPacket+"\nLost packets number: "+(int)pl ;
        results_tv.setText(result);
        SharedPreferences.Editor editor = pref_current_Scen_info.edit();
        if(plp == (int)plp){
            editor.putString("packetLossPercent", (int)plp+" %");
        }else {
            editor.putString("packetLossPercent", plp+" %");
        }
        editor.apply();
        showScenarioInformation();
    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    // BLE selectedDevice scanning callback.
    // run when a new selectedDevice found in scanning.
    private LeScanCallback leScanCallback = new LeScanCallback() {
        // Called when a selectedDevice is found.
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!discoveredDevices_objects.contains(bluetoothDevice)) {
                        discoveredDevices_objects.add(bluetoothDevice);
                        //BLEDevice class which have each BLE selectedDevice's name and mac address Strings
                        BLEDevice newDevice = new BLEDevice();
                        newDevice.setName(bluetoothDevice.getName());
                        newDevice.setMac(bluetoothDevice.getAddress());
                        newDevice.setRssi("RSSI: "+ rssi +"dBm");
                        //discoveredDevices_properties has selectedDevice name and address and rssi for showing
                        discoveredDevices_properties.add(newDevice);
                        discoveredDevicesAdapter.notifyDataSetChanged();
                        rssiList.add(String.valueOf(rssi));
                    }
                }
            });
        }

    };
    private void ScanLeDevice(final boolean enable) {
        if (enable) {
            isFinishScan = false;
            rescan_btn.setEnabled(false);
            disconnectClose();
            discoveredDevices_properties.clear();
            discoveredDevices_objects.clear();
            rssiList.clear();
            selectedDevice = null;
            discoveredDevicesAdapter.notifyDataSetChanged();
            scannedDevicesList_layout.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
            bluetoothAdapter.startLeScan(leScanCallback);
            setConnectionStatusTextView("SCANNING...","#ffff00");
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!isFinishScan){
                        bluetoothAdapter.stopLeScan(leScanCallback);
                        setProgressBarIndeterminateVisibility(false);
                        rescan_btn.setEnabled(true);
                        if(selectedDevice ==null){
                            //The user has not yet selected the selectedDevice from result list.
                            setConnectionStatusTextView("SCANNING FINISHED","#ffff00");
                        }
                    }
                }
            },SCAN_PERIOD); 
        }
        else {   // enable == false
            isFinishScan=true;
            scannedDevicesList_layout.setVisibility(View.INVISIBLE);
            bluetoothAdapter.stopLeScan(leScanCallback);
            setProgressBarIndeterminateVisibility(false);
            rescan_btn.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if(scannedDevicesList_layout.getVisibility()== View.VISIBLE){
            ScanLeDevice(false);
            setConnectionStatusTextView("DISCONNECTED","#ffff00");
            scannedDevicesList_layout.setVisibility(View.INVISIBLE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.start_scan:
                startScan();
                break;
            case R.id.AtCommandConfigs:
                //go to At-command setting activity if moduleName is recognized
                String moduleName = pref_currentATCommands.getString("moduleName", null);
                if(moduleName!=null){
                    startActivity(new Intent(MainActivity.this , ATCommandParametersActivity.class));
                }else {
                    results_tv.setText("Error: bluetooth Module is not recognized!\n");
                    results_tv.append("hint: First Connect to a module device.");

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
                disconnectClose();
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this , AboutActivity.class));
                break;
            case R.id.exit:
                disconnectClose();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startScan() {
        isFinishScan = false;
        disconnectClose();
        ScanLeDevice(true);
    }


    private void disconnectClose() {
        stopService(bleService_intent);
        setConnectionStatusTextView("DISCONNECTED","#ffff00");
    }
    private void clean() {
        scenarioInfo_tv.setText("");
        at_commands_tv.setText("");
        results_tv.setText("");
        SharedPreferences.Editor editor1;
        editor1 = pref_currentATCommands.edit();
        editor1.clear();
        editor1.apply();
        SharedPreferences.Editor editor2;
        editor2 = pref_current_Scen_info.edit();
        editor2.clear();
        editor2.apply();
        receive_Buffer = "";
    }
    private void setConnectionStatusTextView(String status, String color) {
        connectionStatus_tv.setText(status);
        connectionStatus_tv.setTextColor(Color.parseColor(color));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
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
