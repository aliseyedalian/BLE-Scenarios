package com.example.blesenarios;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ATCommandParametersActivity extends AppCompatActivity {
    Spinner baud_rate_spinner;
    Spinner pm_spinner;
    Spinner rfpm_spinner;
    ArrayAdapter<String> baud_rate_adapter;
    ArrayAdapter<String> pm_adapter;
    ArrayAdapter<String> rfpm_adapter;
    String [] baud_rate_list = {"9600","1200","2400","4800","19200","38400","57600","115200"};
    String [] pm_list = {"0","1"};
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
    TextView moduleName_tv;
    String moduleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atcommand_parameters);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Prevent the keyboard from displaying on activity start:
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pref_currentATCommands = getSharedPreferences("currentATCommands",MODE_PRIVATE);
        init();
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_parameters();
            }
        });
        if(moduleName.equals("HC-42")){
            pm_spinner.setEnabled(true);
            input_CINT_MIN.setEnabled(false);
            input_CINT_MAX.setEnabled(false);
            input_CTOUT.setEnabled(false);
            TextView aint_text = findViewById(R.id.aint_text);
            aint_text.setText("Advertising interval:\n([20~10000] ms)");
        }else {
            pm_spinner.setEnabled(false);
        }
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
                editor.putString("pm","not defined");
            }
            if(moduleName.equals("HC-42")){
                //save defaults for hc-42 to preference
                editor.putString("rfpm","0 dBm");
                editor.putString("cintMin","not defined");
                editor.putString("cintMax","not defined");
                editor.putString("aint","200 ms");
                editor.putString("ctout","not defined");
                editor.putString("baudRate","9600 bps");
                editor.putString("led","OFF");
                editor.putString("pm","0");
            }
            //send restore DEFAULT setting --> AT+DEFAULT
            editor.apply();
            finish();
        }
        else {
            //not at+default ,obtaining new parameters
            String cintMin = input_CINT_MIN.getText().toString().trim();
            String cintMax = input_CINT_MAX.getText().toString().trim();
            String rfpm = rfpm_spinner.getSelectedItem().toString().trim();
            String aint = input_AINT.getText().toString().trim();
            String ctout= input_CTOUT.getText().toString().trim();
            String baudRate = baud_rate_spinner.getSelectedItem().toString().trim();
            String led = cb_LED.isChecked() ? "ON" : "OFF";
            String pm ;
            if(moduleName.equals("HC-42")){
                pm = pm_spinner.getSelectedItem().toString().trim();
            }else {
                pm = "not defined";
            }

            //send new parameters
            SharedPreferences.Editor editor = pref_currentATCommands.edit();
            if(cintMin.isEmpty()){
                if(moduleName.equals("HC-08")){
                    input_CINT_MIN.requestFocus();
                    showDialog("Error","CINT Min is required!");
                    return;
                }
                if(moduleName.equals("HC-42")){
                    cintMin = "not defined";
                    editor.putString("cintMin", cintMin);
                }
            }else{
                double cintMin_float = Float.parseFloat(cintMin) * 1.25 ;
                editor.putString("cintMin", cintMin_float +" ms");
            }
            if(cintMax.isEmpty()){
                if(moduleName.equals("HC-08")){
                    input_CINT_MAX.requestFocus();
                    showDialog("Error","CINT Max is required!");
                    return;
                }
                if(moduleName.equals("HC-42")){
                    cintMax = "not defined";
                    editor.putString("cintMax", cintMax);
                }
            }else{
                double cintMax_float = Float.parseFloat(cintMax) * 1.25 ;
                editor.putString("cintMax", cintMax_float +" ms");
            }
            if(ctout.isEmpty()){
                if(moduleName.equals("HC-08")){
                    input_CTOUT.requestFocus();
                    showDialog("Error","CTOUT is required!");
                    return;
                }
                if(moduleName.equals("HC-42")){
                    ctout = "not defined";
                    editor.putString("ctout", ctout);
                }
            }else{
                int ctout_int = Integer.parseInt(ctout)*10;
                editor.putString("ctout",ctout_int+" ms");
            }
            if(aint.isEmpty()){
                input_AINT.requestFocus();
                showDialog("Error","AINT is required!");
                return;
            }
            editor.putString("ATDEFAULT","No");
            editor.putString("rfpm",rfpm);
            editor.putString("aint",aint+" ms");
            editor.putString("baudRate",baudRate+" bps");
            editor.putString("led",led);
            editor.putString("pm",pm);
            editor.apply();
            finish();
        }
    }

    private void init() {
        input_CINT_MIN = findViewById(R.id.cintMin);
        input_CINT_MAX = findViewById(R.id.cintMax);
        input_AINT = findViewById(R.id.aint);
        input_CTOUT = findViewById(R.id.ctout);
        moduleName_tv = findViewById(R.id.deviceName_tv);
        moduleName = pref_currentATCommands.getString("moduleName",null);
        moduleName_tv.setText(moduleName);

        rfpm_spinner = findViewById(R.id.rfpm);
        rfpm_adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rfpm_list);
        rfpm_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rfpm_spinner.setAdapter(rfpm_adapter);


        baud_rate_spinner = findViewById(R.id.baud_rate);
        baud_rate_adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, baud_rate_list);
        baud_rate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baud_rate_spinner.setAdapter(baud_rate_adapter);

        pm_spinner = findViewById(R.id.pm);
        pm_adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pm_list);
        pm_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pm_spinner.setAdapter(pm_adapter);

        cb_LED = findViewById(R.id.LED);
        cb_AT_DEFAULT = findViewById(R.id.checkbox_defaultSetting);
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
                    pm_spinner.setEnabled(false);
                    cb_LED.setEnabled(false);
                }else {
                    //enable input interfaces:
                    rfpm_spinner.setEnabled(true);
                    input_CINT_MIN.setEnabled(true);
                    input_CINT_MAX.setEnabled(true);
                    input_AINT.setEnabled(true);
                    input_CTOUT.setEnabled(true);
                    baud_rate_spinner.setEnabled(true);
                    if(moduleName.equals("HC-42")){
                        pm_spinner.setEnabled(true);
                    }
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
    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }
}
