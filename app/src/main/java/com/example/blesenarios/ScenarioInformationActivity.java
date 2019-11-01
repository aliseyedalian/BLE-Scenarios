package com.example.blesenarios;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class ScenarioInformationActivity extends AppCompatActivity {
    Spinner phoneBleVersion_spinner;
    ArrayAdapter<String> phoneBleVersion_adapter;
    String [] phoneBleVersion_list = {"v4.0","v4.1","v4.2","v5.0","v5.1"};
    Spinner place_spinner;
    ArrayAdapter<String> place_adapter;
    String [] indoor_outdoor_list = {"Indoor","Outdoor"};
    Spinner obstacle_spinner;
    ArrayAdapter<String> obstacle_adapter;
    String [] obstacle_list = {"LOS","Glass","Wood","Metal","Brick","Concrete","Human Body"};
    EditText input_distanceMin;
    EditText input_distanceMax;
    EditText input_obstacleNo;
    CheckBox wifi_cb;
    CheckBox ipv6_cb;
    Button save_btn;
    EditText input_moreExplanation;
    SharedPreferences pref_currentScenario_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_information);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Prevent the keyboard from displaying on activity start:
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        init();
        pref_currentScenario_info = getSharedPreferences("currentScenario_info",MODE_PRIVATE);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveParameters();
            }
        });
        //load the last scenario information
        loadLastScenInfo();
    }

    private void loadLastScenInfo() {
        //obtain parameters from preferences
        String phoneBLEVersion = pref_currentScenario_info.getString("phoneBLEVersion",null);
        String distanceMin = pref_currentScenario_info.getString("distanceMin", null);
        String distanceMax = pref_currentScenario_info.getString("distanceMax", null);
        String place = pref_currentScenario_info.getString("place", null); //indoor/outdoor
        String obstacleNo = pref_currentScenario_info.getString("obstacleNo", null);
        String obstacle = pref_currentScenario_info.getString("obstacle", null);
        String wifi = pref_currentScenario_info.getString("wifi", null);
        String ipv6 = pref_currentScenario_info.getString("ipv6", null);

        if(phoneBLEVersion != null){
            int spinnerPosition = phoneBleVersion_adapter.getPosition(phoneBLEVersion);
            phoneBleVersion_spinner.setSelection(spinnerPosition);
        }
        if(distanceMin != null){
            input_distanceMin.setText(distanceMin);
        }
        if(distanceMax != null){
            input_distanceMax.setText(distanceMax);
        }
        if(place != null){
            int spinnerPosition = place_adapter.getPosition(place);
            place_spinner.setSelection(spinnerPosition);
        }
        if(obstacleNo != null){
            input_obstacleNo.setText(obstacleNo);
        }
        if(obstacle != null){
            int spinnerPosition = obstacle_adapter.getPosition(obstacle);
            obstacle_spinner.setSelection(spinnerPosition);
        }
        if(wifi!=null && wifi.equals("Yes")){
            wifi_cb.setChecked(true);
        }
        if(ipv6!=null && ipv6.equals("Yes")){
            ipv6_cb.setChecked(true);
        }

    }


    private void saveParameters() {
        //save all input data in string data type:
        String phoneBLEVersion = phoneBleVersion_spinner.getSelectedItem().toString().trim();
        String distanceMin = input_distanceMin.getText().toString().trim();
        String distanceMax = input_distanceMax.getText().toString().trim();
        String obstacleNo = input_obstacleNo.getText().toString().trim();
        String obstacle = obstacle_spinner.getSelectedItem().toString().trim();
        String place = place_spinner.getSelectedItem().toString().trim();
        String explanation = input_moreExplanation.getText().toString().trim();
        String wifi;
        String ipv6;
        if(wifi_cb.isChecked()){
            wifi = "Yes";
        } else{
            wifi = "No";
        }
        if(ipv6_cb.isChecked()){
            ipv6 = "Yes";
        } else{
            ipv6 = "No";
        }
        if(obstacle.equals("LOS")){
            obstacleNo="0";
        }

        if(isValidParameters(distanceMin,distanceMax,obstacleNo,obstacle)){
            SharedPreferences.Editor editor = pref_currentScenario_info.edit();
            editor.putString("phoneName",android.os.Build.MODEL);
            editor.putString("phoneManufacturer", Build.MANUFACTURER);
            editor.putString("phoneBLEVersion",phoneBLEVersion);
            editor.putString("distanceMin",distanceMin);
            editor.putString("distanceMax",distanceMax);
            editor.putString("place",place);
            editor.putString("obstacleNo",obstacleNo);
            editor.putString("obstacle",obstacle);
            editor.putString("wifi",wifi);
            editor.putString("ipv6",ipv6);
            editor.putString("explanation",explanation);

            editor.apply();
            finish();
        }
    }

    private boolean isValidParameters(String distanceMin, String distanceMax, String obstacle_count, String obstacle) {
        //check validation of inputs
        if(distanceMin.isEmpty()){
            input_distanceMin.requestFocus();
            return false;
        }
        if(distanceMax.isEmpty()){
            input_distanceMax.requestFocus();
            return false;
        }
        if(Float.parseFloat(distanceMin) > Float.parseFloat(distanceMax)){
            showDialog("Minimum distance can not be greater than Maximum distance!");
            input_distanceMin.requestFocus();
            return false;
        }
        if((obstacle_count.isEmpty()||Integer.parseInt(obstacle_count) == 0)
                && !obstacle.equals("LOS")){
            showDialog("'Number of obstacles' is invalid!");
            input_obstacleNo.requestFocus();
            return false;
        }
        return true;
    }
    private void init() {
        phoneBleVersion_spinner = findViewById(R.id.phoneBleVersion);
        phoneBleVersion_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phoneBleVersion_list);
        phoneBleVersion_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneBleVersion_spinner.setAdapter(phoneBleVersion_adapter);

        place_spinner = findViewById(R.id.indoor_outdoor);
        place_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, indoor_outdoor_list);
        place_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place_spinner.setAdapter(place_adapter);

        obstacle_spinner = findViewById(R.id.obstacle);
        obstacle_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, obstacle_list);
        obstacle_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        obstacle_spinner.setAdapter(obstacle_adapter);


        input_distanceMin = findViewById(R.id.distanceMin);
        input_distanceMax = findViewById(R.id.distanceMax);
        input_obstacleNo =findViewById(R.id.obstacle_count);
        input_moreExplanation = findViewById(R.id.moreExplanation);
        wifi_cb =findViewById(R.id.wifi_cb);
        ipv6_cb =findViewById(R.id.ipv6_cb);
        save_btn = findViewById(R.id.btn_Save);
    }
    //return Arrow:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }
}
