package com.example.blesenarios;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ATCommandParametersActivity extends AppCompatActivity {
    Spinner baud_rate_spinner;
    Spinner rfpm_spinner;
    ArrayAdapter<String> baud_rate_adapter;
    ArrayAdapter<String> rfpm_adapter;
    String [] baud_rate_list = {"9600","1200","2400","4800","19200","38400","57600","115200"};
    String [] rfpm_list = {"4 dBm","3 dBm","0 dBm",
            "-4 dBm","-6 dBm","-8 dBm","-12 dBm",
            "-16 dBm","-20 dBm","-23 dBm","-40 dBm"};
    Button btn_Save;
    EditText input_CINT_MIN;
    EditText input_CINT_MAX;
    EditText input_AINT;
    EditText input_CTOUT;
    CheckBox cb_LED;
    CheckBox cb_AT_DEFAULT;
    SharedPreferences pref_currentATCommands;
    TextView deviceName_tv;
    String moduleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atcommand_parameters);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pref_currentATCommands = getSharedPreferences("currentATCommands",MODE_PRIVATE);
        init();
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_parameters();
            }
        });
        loadConfig();
    }

    private void loadConfig() {

    }

    private void save_parameters() {
        if (cb_AT_DEFAULT.isChecked()) {
            SharedPreferences.Editor editor = pref_currentATCommands.edit();
            editor.putString("ATDEFAULT","Yes");
            if(moduleName.equals("HC-08")){
                //save defaults for hc-08 to preference
                editor.putString("rfpm","4 dBm");
                editor.putString("cintMin","7.5 ms");
                editor.putString("cintMax","15 ms");
                editor.putString("aint","320 ms");
                editor.putString("ctout","2000 ms");
                editor.putString("baudRate","9600 bps");
                editor.putString("led","ON");
                loadConfig();
            }
            if(moduleName.equals("HC-42")){
                //save defaults for hc-42 to preference
                editor.putString("rfpm","0 dBm");
                editor.putString("cintMin","None");
                editor.putString("cintMax","None");
                editor.putString("aint","200 ms");
                editor.putString("ctout","None");
                editor.putString("baudRate","9600 bps");
                editor.putString("led","OFF");
                loadConfig();
            }
            //send restore DEFAULT setting --> AT+DEFAULT
            editor.apply();
            finish();
        }
        else {
            //no default parameters , obtain new parameters
            String cintMin = input_CINT_MIN.getText().toString().trim();
            String cintMax = input_CINT_MAX.getText().toString().trim();
            String rfpm = rfpm_spinner.getSelectedItem().toString().trim();
            String aint = input_AINT.getText().toString().trim();
            String ctout= input_CTOUT.getText().toString().trim();
            String baudRate = baud_rate_spinner.getSelectedItem().toString().trim();
            String led = cb_LED.isChecked() ? "ON" : "OFF";
            //send new parameters
            if(isValidParameters(cintMin,cintMax,aint,ctout)){
                SharedPreferences.Editor editor = pref_currentATCommands.edit();
                editor.putString("ATDEFAULT","No");
                editor.putString("rfpm",rfpm);
                editor.putString("cintMin",cintMin);
                editor.putString("cintMax",cintMax);
                editor.putString("aint",aint);
                editor.putString("ctout",ctout);
                editor.putString("baudRate",baudRate);
                editor.putString("led",led);
                editor.apply();
                finish();
            }
        }


    }

    private boolean isValidParameters(String cint_min,String cint_max,String aint, String ctout) {
        //check validation of inputs
        if(cint_min.isEmpty() || cint_max.isEmpty() ||
            aint.isEmpty() || ctout.isEmpty()) return false;
        int CINT_MIN = Integer.parseInt(cint_min);
        int CINT_MAX = Integer.parseInt(cint_max);
        int AINT = Integer.parseInt(aint);
        int CTOUT = Integer.parseInt(ctout);
        if((CINT_MIN<6) || (CINT_MIN >3200)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong Connection Interval value!");
            builder.setMessage("CINT value must be between 6 and 3200. ");
            builder.setCancelable(false);
            builder.setPositiveButton("OK",null);
            builder.show();
            input_CINT_MIN.requestFocus();
            return false;
        }
        if( (CINT_MAX<6) || (CINT_MAX >3200)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong Connection Interval value!");
            builder.setMessage("CINT value must be between 6 and 3200. ");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", null);
            builder.show();
            input_CINT_MAX.requestFocus();
            return false;
        }

        if(CINT_MIN > CINT_MAX){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong Connection Interval value!");
            builder.setMessage("CINT_MIN value must be less than CINT_MAX.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK",null);
            builder.show();
            input_CINT_MAX.requestFocus();
            return false;
        }

        if(AINT<32 || AINT>16000){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong Advertising Interval value!");
            builder.setMessage("AINT value must be between 32 and 16000.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK",null);
            input_AINT.requestFocus();
            builder.show();
            return false;
        }
        if(CTOUT< 10 || CTOUT>3200){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wrong Connection Supervision Timeout!");
            builder.setMessage("CTOUT value must be between 10 and 3200.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK",null);
            builder.show();
            input_CTOUT.requestFocus();
            return false;
        }
        return true;
    }
    private void init() {
        input_CINT_MIN = findViewById(R.id.cintMin);
        input_CINT_MAX = findViewById(R.id.cintMax);
        input_AINT = findViewById(R.id.aint);
        input_CTOUT = findViewById(R.id.ctout);
        deviceName_tv = findViewById(R.id.deviceName_tv);
        moduleName = pref_currentATCommands.getString("moduleName",null);
        deviceName_tv.setText(moduleName);

        rfpm_spinner = findViewById(R.id.rfpm);
        rfpm_adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rfpm_list);
        rfpm_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rfpm_spinner.setAdapter(rfpm_adapter);

        baud_rate_spinner = findViewById(R.id.baud_rate);
        baud_rate_adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, baud_rate_list);
        baud_rate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baud_rate_spinner.setAdapter(baud_rate_adapter);

        cb_LED = findViewById(R.id.LED);
        cb_AT_DEFAULT = findViewById(R.id.checkbox_factorySetting);
        cb_AT_DEFAULT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //disable input interfaces:
                    rfpm_spinner.setEnabled(false);
                    input_CINT_MIN.setEnabled(false);
                    input_CINT_MAX.setEnabled(false);
                    input_AINT.setEnabled(false);
                    input_CTOUT.setEnabled(false);
                    baud_rate_spinner.setEnabled(false);
                    cb_LED.setEnabled(false);
                }else {
                    //enable input interfaces:
                    rfpm_spinner.setEnabled(true);
                    input_CINT_MIN.setEnabled(true);
                    input_CINT_MAX.setEnabled(true);
                    input_AINT.setEnabled(true);
                    input_CTOUT.setEnabled(true);
                    baud_rate_spinner.setEnabled(true);
                    cb_LED.setEnabled(true);
                }
            }
        });
        btn_Save = findViewById(R.id.btn_Save_Transmission_parameter);
    }

    @Override //back barrow
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
