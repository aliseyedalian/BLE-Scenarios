package com.example.blesenarios;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScenariosReports extends AppCompatActivity {
    Button showPhoneTable_btn;
    Button showModuleTable_btn;
    Button showConfigTable_btn;
    Button showScenarioTable_btn;
    Button exportCSV_btn;
    DatabaseHelper databaseHelper;
    ListView listView;
    List<String> titlesList;
    List<String> contentsList;
    ArrayAdapter<String> arrayAdapter_phone;
    ArrayAdapter<String> arrayAdapter_module;
    ArrayAdapter<String> arrayAdapter_config;
    ArrayAdapter<String> arrayAdapter_scenario;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_reports);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        listView = findViewById(R.id.report_lv);
        databaseHelper = new DatabaseHelper(this);
        showPhoneTable_btn =findViewById(R.id.phoneTable_btn);
        showPhoneTable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhoneTableInListView();
            }
        });
        showModuleTable_btn=findViewById(R.id.ModuleTable_btn);
        showModuleTable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModuleTableInListView();
            }
        });
        showConfigTable_btn = findViewById(R.id.ConfigTable_btn);
        showConfigTable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfigTableInListView();
            }
        });
        showScenarioTable_btn= findViewById(R.id.ScenarioTable_btn);
        showScenarioTable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showScenarioTableInListView();
            }
        });
        exportCSV_btn = findViewById(R.id.btn_export);
        exportCSV_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(ScenariosReports.this);
                new ExportDatabaseCSVTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    private void showPhoneTableInListView(){
        Cursor resultCursor = databaseHelper.getPhoneTable();
        if(resultCursor.getCount()==0){
            showDialog("Error" , "Nothing Found!");
            return;
        }
        contentsList = new ArrayList<>();
        titlesList = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("phoneName : ").append(resultCursor.getString(0)).append("\n");
            buffer.append("phoneManufacturer : ").append(resultCursor.getString(1)).append("\n");
            buffer.append("phoneBLEVersion : ").append(resultCursor.getString(2)).append("\n");
            titlesList.add(resultCursor.getString(0));
            contentsList.add(buffer.toString());
        }
        arrayAdapter_phone = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titlesList);
        listView.setAdapter(arrayAdapter_phone);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(titlesList.get(i), contentsList.get(i));
            }
        });
    }
    private void showModuleTableInListView() {
        Cursor resultCursor = databaseHelper.getModuleTable();
        if(resultCursor.getCount()==0){
            showDialog("Error" , "Nothing Found!");
            return;
        }
        contentsList = new ArrayList<>();
        titlesList = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("moduleName : ").append(resultCursor.getString(0)).append("\n");
            buffer.append("moduleBLEVersion : ").append(resultCursor.getString(1)).append("\n");
            titlesList.add(resultCursor.getString(0));
            contentsList.add(buffer.toString());
        }
        arrayAdapter_module = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titlesList);
        listView.setAdapter(arrayAdapter_module);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(titlesList.get(i),contentsList.get(i));
            }
        });
    }
    private void showConfigTableInListView() {
        Cursor resultCursor = databaseHelper.getConfigTable();
        if(resultCursor.getCount()==0){
            showDialog("Error" , "Nothing Found!");
            return;
        }
        contentsList = new ArrayList<>();
        titlesList = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("configId : ").append(resultCursor.getString(0)).append("\n");
            buffer.append("ATDEFAULT : ").append(resultCursor.getString(1)).append("\n");
            buffer.append("cintMin : ").append(resultCursor.getString(2)).append("\n");
            buffer.append("cintMax : ").append(resultCursor.getString(3)).append("\n");
            buffer.append("rfpm : ").append(resultCursor.getString(4)).append("\n");
            buffer.append("aint : ").append(resultCursor.getString(5)).append("\n");
            buffer.append("ctout : ").append(resultCursor.getString(6)).append("\n");
            buffer.append("led : ").append(resultCursor.getString(7)).append("\n");
            buffer.append("baudRate : ").append(resultCursor.getString(8)).append("\n");
            buffer.append("pm : ").append(resultCursor.getString(9)).append("\n");
            titlesList.add("Config "+resultCursor.getString(0));
            contentsList.add(buffer.toString());
        }
        arrayAdapter_config = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titlesList);
        listView.setAdapter(arrayAdapter_config);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(titlesList.get(i),contentsList.get(i));
            }
        });
    }
    private void showScenarioTableInListView() {
        Cursor resultCursor = databaseHelper.getScenarioTable();
        if(resultCursor.getCount()==0){
            showDialog("Error" , "Nothing Found!");
            return;
        }
        contentsList = new ArrayList<>();
        titlesList = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        //for each row in scenario resultCursor :
        // 1-get row and save in receive_Buffer
        // 2-add a key in scenariosKeyList
        // 3-add corresponding value in scenariosContentList
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("scenId : ").append(resultCursor.getString(0)).append("\n");
            buffer.append("configId : ").append(resultCursor.getString(1)).append("\n");
            buffer.append("phoneName : ").append(resultCursor.getString(2)).append("\n");
            buffer.append("moduleName : ").append(resultCursor.getString(3)).append("\n");
            buffer.append("rssi : ").append(resultCursor.getString(4)).append("\n");
            buffer.append("distanceMin : ").append(resultCursor.getString(5)).append("\n");
            buffer.append("distanceMax : ").append(resultCursor.getString(6)).append("\n");
            buffer.append("place : ").append(resultCursor.getString(7)).append("\n");
            buffer.append("obstacleNo : ").append(resultCursor.getString(8)).append("\n");
            buffer.append("obstacle : ").append(resultCursor.getString(9)).append("\n");
            buffer.append("humidityPercent : ").append(resultCursor.getString(10)).append("\n");
            buffer.append("wifi : ").append(resultCursor.getString(11)).append("\n");
            buffer.append("ipv6 : ").append(resultCursor.getString(12)).append("\n");
            buffer.append("startTimeStamp : ").append(resultCursor.getString(13)).append("\n");
            buffer.append("endTimeStamp : ").append(resultCursor.getString(14)).append("\n");
            buffer.append("packetLossPercent : ").append(resultCursor.getString(15)).append("\n");
            buffer.append("explanation : ").append(resultCursor.getString(16)).append("\n");
            titlesList.add("Scenario "+resultCursor.getString(0));
            contentsList.add(buffer.toString());
        }
        arrayAdapter_scenario = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titlesList);
        listView.setAdapter(arrayAdapter_scenario);
        //set on item click listener:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(titlesList.get(i),contentsList.get(i));
            }
        });
    }




    @SuppressLint("StaticFieldLeak")
    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(ScenariosReports.this);
        DatabaseHelper databaseHelper;
        @Override
        protected void onPreExecute() {
            databaseHelper = new DatabaseHelper(ScenariosReports.this);
        }

        protected Boolean doInBackground(final String... args) {
            File exportDir = new File(Environment.getExternalStorageDirectory(), "/scens");
            if (!exportDir.exists()) {
                Log.d("salis","exportDir not exists");
                if(exportDir.mkdirs()){
                    Log.d("salis","exportDir.mkdirs()");
                }
            }

            File file = new File(exportDir, "scenarios.csv");
            Log.d("salis","file: "+file);
            try {
                if(file.createNewFile()){
                    Log.d("salis","file.createNewFile()");
                }
                Log.d("salis","new file created");
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                Cursor curCSV = databaseHelper.exportScenario();
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext()) {
                    String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                    for(int i=0;i<curCSV.getColumnNames().length;i++)
                    {
                        mySecondStringArray[i] =curCSV.getString(i);
                    }
                    csvWrite.writeNext(mySecondStringArray);
                }
                csvWrite.close();
                curCSV.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                showDialog("Export Done","Scenarios were saved in this address:\n/storage/emulated/0/scens/scenarios.csv");
            } else {
                showDialog("Error","Export to excel file failed!");
            }
        }
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
    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
