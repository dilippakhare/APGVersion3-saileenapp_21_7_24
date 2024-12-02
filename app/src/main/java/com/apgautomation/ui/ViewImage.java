package com.apgautomation.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.apgautomation.R;
import com.apgautomation.TouchImageView;
import com.squareup.picasso.Picasso;

public class ViewImage extends AppCompatActivity {

    public  static String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        TouchImageView img=findViewById(R.id.img);
        try
        {
            Picasso.get().load(  url).placeholder(R.drawable.loading).into(img);
        }
        catch (Exception ex){}


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Image");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
