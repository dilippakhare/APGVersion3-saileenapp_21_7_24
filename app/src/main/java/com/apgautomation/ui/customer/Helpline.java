package com.apgautomation.ui.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.apgautomation.R;

public class Helpline extends AppCompatActivity {

    TextView txtCustomerSupport,txtAppSupport,txtCustomerSupportNumber,txtAppSupportNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        findViewById(R.id.l1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getString(R.string.app_name).contains("APG")) {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9130098109"));
                    startActivity(i);
                }
                if(getString(R.string.app_name).contains("Saileen"))
                {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9552502975"));
                    startActivity(i);
                }
            }
        });

        findViewById(R.id.l2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(getString(R.string.app_name).contains("APG")) {
                    Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9011044811"));
                    startActivity(i);
                }
                if(getString(R.string.app_name).contains("Saileen"))
                {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9822033433"));
                    startActivity(i);
                }
            }
        });
        getSupportActionBar().setTitle("Helpline");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtCustomerSupport=findViewById(R.id.txtCustomerSupport);
        txtAppSupport=findViewById(R.id.txtAppSupport);
        txtCustomerSupportNumber=findViewById(R.id.txtCustomerSupportNumber);
        txtAppSupportNumber=findViewById(R.id.txtAppSupportNumber);
        if(getString(R.string.app_name).contains("Saileen"))
        {
            txtAppSupport.setText("Pune Office");
            txtAppSupportNumber.setText("9822033433");
            txtCustomerSupport.setText("Laxman Kawale");
            txtCustomerSupportNumber.setText("9552502975");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
