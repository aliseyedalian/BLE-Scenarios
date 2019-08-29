package com.example.blesenarios;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    TextView progress_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progress_tv = findViewById(R.id.progress_tv);
        writeDot(15,100);
    }

    private void writeDot(final int number, final int delay) {
        if(number<=0){
            startActivity(new Intent(WelcomeActivity.this , MainActivity.class));
            finish();
        }
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    writeDot(number-1,delay);
                }
            },delay);
            progress_tv.append("_");
        }
    }
}
