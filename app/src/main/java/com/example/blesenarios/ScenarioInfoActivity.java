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
    String [] obstacle_list = {"LOS(No obstacle)","Glass","Wood","Metal","Concrete wall","Simple wall","Human Body"};
    EditText input_distance;
    EditText input_obstacle_count;
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
        String phoneBleVersion = phoneBleVersion_spinner.getSelectedItem().toString().trim();
        String distance = input_distance.getText().toString().trim();
        String obstacle_count = input_obstacle_count.getText().toString().trim();
        String inOut_door = indoor_outdoor_spinner.getSelectedItem().toString().trim();
        String obstacle = obstacle_spinner.getSelectedItem().toString().trim();
        String moreExplanation = input_moreExplanation.getText().toString().trim();
        String wifi_status;
        String ipv6_status;
        if(wifi_cb.isChecked()){
            wifi_status = "yes";
        } else{
            wifi_status = "no";
        }
        if(ipv6_cb.isChecked()){
            ipv6_status = "yes";
        } else{
            ipv6_status = "no";
        }

        if(isValidParameters(distance,obstacle_count)){
            SharedPreferences.Editor editor = pref_currentScenario_info.edit();
            editor.putString("phoneBleVersion",phoneBleVersion);
            editor.putString("distance",distance);
            editor.putString("obstacle_count",obstacle_count);
            editor.putString("inOut_door",inOut_door);
            editor.putString("obstacle",obstacle);
            editor.putString("wifi_status",wifi_status);
            editor.putString("ipv6_status",ipv6_status);
            editor.putString("PhoneModel",android.os.Build.MODEL);
            editor.putString("PhoneManufacturer", Build.MANUFACTURER);
            if(!moreExplanation.isEmpty()){
                editor.putString("moreExplanation",moreExplanation);
            }
            editor.apply();
            finish();
        }
    }

    private boolean isValidParameters(String distance, String obstacle_count) {
        //check validation of inputs
        if(distance.isEmpty() || obstacle_count.isEmpty()){
            return false;
        }
        return true;
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
        input_obstacle_count=findViewById(R.id.obstacle_count);
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
