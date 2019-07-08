package com.example.blesenarios;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class ScenarioInfoActivity extends AppCompatActivity {
    Spinner phoneBleVersion_spinner;
    ArrayAdapter<String> phoneBleVersion_adapter;
    String [] phoneBleVersion_list = {"v4.0","v4.1","v4.2","v5.0"};
    Spinner indoor_outdoor_spinner;
    ArrayAdapter<String> indoor_outdoor_adapter;
    String [] indoor_outdoor_list = {"Indoor","Outdoor"};
    Spinner obstacle_spinner;
    ArrayAdapter<String> obstacle_adapter;
    String [] obstacle_list = {"LOS(Without Obstacles)","Glass","Wood","Metal","Brick","Concrete","Body"};
    EditText input_distance;
    EditText input_obstacleNo;
    CheckBox wifi_cb;
    CheckBox ipv6_cb;
    Button save_btn;
    EditText input_moreExplanation;
    SharedPreferences pref_currentScenario_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_info);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        init();
        pref_currentScenario_info = getSharedPreferences("currentScenario_info",MODE_PRIVATE);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveParameters();
            }
        });
    }

    private void saveParameters() {
        //save all input data in string data type:
        String phoneBleVersion = phoneBleVersion_spinner.getSelectedItem().toString().trim();
        String distance = input_distance.getText().toString().trim();
        String obstacleNo = input_obstacleNo.getText().toString().trim();
        String obstacle = obstacle_spinner.getSelectedItem().toString().trim();
        String place = indoor_outdoor_spinner.getSelectedItem().toString().trim();
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
        if(obstacle == "LOS(Without Obstacles)"){
            obstacleNo="0";
        }

        if(isNotEmptyParameters(distance,obstacleNo,obstacle)){
            SharedPreferences.Editor editor = pref_currentScenario_info.edit();

            editor.putString("phoneName",android.os.Build.MODEL);
            editor.putString("phoneManufacturer", Build.MANUFACTURER);
            editor.putString("phoneBleVersion",phoneBleVersion);
            editor.putString("distance",distance);
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

    private boolean isNotEmptyParameters(String distance, String obstacle_count,String obstacle) {
        //check validation of inputs
        if(distance.isEmpty()){
            input_distance.requestFocus();
            return false;
        }
        else if(obstacle_count.isEmpty() && !obstacle.equals("LOS(Without Obstacles)")){
            input_obstacleNo.requestFocus();
            return false;
        }else {
            return true;
        }
    }
    private void init() {
        phoneBleVersion_spinner = findViewById(R.id.phoneBleVersion);
        phoneBleVersion_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phoneBleVersion_list);
        phoneBleVersion_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneBleVersion_spinner.setAdapter(phoneBleVersion_adapter);

        indoor_outdoor_spinner = findViewById(R.id.indoor_outdoor);
        indoor_outdoor_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, indoor_outdoor_list);
        indoor_outdoor_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indoor_outdoor_spinner.setAdapter(indoor_outdoor_adapter);

        obstacle_spinner = findViewById(R.id.obstacle);
        obstacle_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, obstacle_list);
        obstacle_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        obstacle_spinner.setAdapter(obstacle_adapter);


        input_distance = findViewById(R.id.distance);
        input_obstacleNo =findViewById(R.id.obstacle_count);
        input_moreExplanation = findViewById(R.id.moreExplanation);
        wifi_cb =findViewById(R.id.wifi_cb);
        ipv6_cb =findViewById(R.id.ipv6_cb);
        save_btn = findViewById(R.id.btn_Save);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
