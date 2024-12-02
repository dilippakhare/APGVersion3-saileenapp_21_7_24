package com.example.agecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2,ed3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent();
        intent.setClass(this,BootReciever.class);
        sendBroadcast(intent);
        ed1=findViewById(R.id.ed1);
        ed2=findViewById(R.id.ed2);
        ed3=findViewById(R.id.ed3);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh=getSharedPreferences("b",MODE_PRIVATE);
                sh.edit().putString("y",ed3.getText().toString())
                        .putString("m",ed2.getText().toString())
                        .putString("d",ed1.getText().toString()).commit();

                Intent intent=new Intent();
                intent.setClass(MainActivity.this,BootReciever.class);
                sendBroadcast(intent);
            }
        });

        SharedPreferences sh=this.getSharedPreferences("b", this.MODE_PRIVATE);
        try
        {
            ed1.setText(sh.getString("d",""));
            ed2.setText(sh.getString("m",""));
            ed3.setText(sh.getString("y",""));
        }
        catch (Exception ex){}

    }
}