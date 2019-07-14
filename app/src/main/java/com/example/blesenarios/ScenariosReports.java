package com.example.blesenarios;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScenariosReports extends AppCompatActivity {
    Button showPhoneTable_btn;
    Button showModuleTable_btn;
    Button showConfigTable_btn;
    Button showScenarioTable_btn;
    DatabaseHelper databaseHelper;
    ListView listView;
    List<String> titlesList;
    List<String> contentsList;
    ArrayAdapter<String> arrayAdapter_phone;
    ArrayAdapter<String> arrayAdapter_module;
    ArrayAdapter<String> arrayAdapter_config;
    ArrayAdapter<String> arrayAdapter_scenario;

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
    }

    private void showPhoneTableInListView(){
        Cursor resultCursor = databaseHelper.getPhoneTable();
        if(resultCursor.getCount()==0){
            showDialog("Error" , "Nothing Found!");
            return;
        }
        contentsList = new ArrayList<>();
        titlesList = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("phoneName : "+resultCursor.getString(0)+"\n");
            buffer.append("phoneManufacturer : "+resultCursor.getString(1)+"\n");
            buffer.append("phoneBLEVersion : "+resultCursor.getString(2)+"\n");
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
        StringBuffer buffer = new StringBuffer();
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("moduleName : "+resultCursor.getString(0)+"\n");
            buffer.append("moduleBLEVersion : "+resultCursor.getString(1)+"\n");
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
        StringBuffer buffer = new StringBuffer();
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("configId : "+resultCursor.getString(0)+"\n");
            buffer.append("ATDEFAULT : "+resultCursor.getString(1)+"\n");
            buffer.append("cintMin : "+resultCursor.getString(2)+"\n");
            buffer.append("cintMax : "+resultCursor.getString(3)+"\n");
            buffer.append("rfpm : "+resultCursor.getString(4)+"\n");
            buffer.append("aint : "+resultCursor.getString(5)+"\n");
            buffer.append("ctout : "+resultCursor.getString(6)+"\n");
            buffer.append("led : "+resultCursor.getString(7)+"\n");
            buffer.append("baudRate : "+resultCursor.getString(8)+"\n");
            buffer.append("parity : "+resultCursor.getString(9)+"\n");
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
        StringBuffer buffer = new StringBuffer();
        //for each row in scenario resultCursor :
        // 1-get row and save in buffer_rcv
        // 2-add a key in scenariosKeyList
        // 3-add corresponding value in scenariosContentList
        while (resultCursor.moveToNext()){
            buffer.delete(0, buffer.length());
            buffer.append("scenId : "+resultCursor.getString(0)+"\n");
            buffer.append("configId : "+resultCursor.getString(1)+"\n");
            buffer.append("phoneName : "+resultCursor.getString(2)+"\n");
            buffer.append("moduleName : "+resultCursor.getString(3)+"\n");
            buffer.append("distance : "+resultCursor.getString(4)+"\n");
            buffer.append("place : "+resultCursor.getString(5)+"\n");
            buffer.append("obstacleNo : "+resultCursor.getString(6)+"\n");
            buffer.append("obstacle : "+resultCursor.getString(7)+"\n");
            buffer.append("humidityPercent : "+resultCursor.getString(8)+"\n");
            buffer.append("wifi : "+resultCursor.getString(9)+"\n");
            buffer.append("ipv6 : "+resultCursor.getString(10)+"\n");
            buffer.append("timeStamp : "+resultCursor.getString(11)+"\n");
            buffer.append("per : "+resultCursor.getString(12)+"\n");
            buffer.append("explanation : "+resultCursor.getString(13)+"\n");
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
