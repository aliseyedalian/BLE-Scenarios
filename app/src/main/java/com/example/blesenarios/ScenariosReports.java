package com.example.blesenarios;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ScenariosReports extends AppCompatActivity {
    Button showPhoneTable_btn;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_reports);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        databaseHelper = new DatabaseHelper(this);
        showPhoneTable_btn =findViewById(R.id.phoneTable_btn);
        showPhoneTable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhoneTable();
            }
        });
    }

    private void showPhoneTable() {
        Cursor resultCursor = databaseHelper.getPhoneTable();
        if(resultCursor.getCount()==0){
            showMessageDialog("Error" , "Nothing Found in Database!");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (resultCursor.moveToNext()){
            buffer.append("phoneName : "+resultCursor.getString(0)+"\n");
            buffer.append("phoneManufacturer : "+resultCursor.getString(1)+"\n");
            buffer.append("phoneBLEVersion : "+resultCursor.getString(2)+"\n");
        }
        //show all data
        showMessageDialog("Phone Table" , buffer.toString());
    }
    private void showMessageDialog(String title, String message) {
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
