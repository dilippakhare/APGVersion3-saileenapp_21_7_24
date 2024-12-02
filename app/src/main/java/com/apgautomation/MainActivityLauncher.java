package com.apgautomation;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.apgautomation.ui.LoginActivity;
import com.apgautomation.ui.customer.CustomerActivity;
import com.apgautomation.utility.CommonShare;
import com.otaliastudios.cameraview.PictureResult;

public class MainActivityLauncher extends AppCompatActivity {
  long millisecond=2000;
  public static PictureResult pictureResult;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(CommonShare.getUserId(this)==0)
      millisecond=5000;
    setContentView(R.layout.activity_launch); new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(millisecond);
        }
        catch (Exception ex){}
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            checkLogin();
          }
        });


      }
    }).start();
  }
  void checkLogin()
  {

    finish();
    if(CommonShare.getUserId(this)>0)
    {
      if(CommonShare.getRole(this).equalsIgnoreCase("Customer"))
      {
        startActivity(new Intent(this, CustomerActivity.class));
      }
      else
      {
        startActivity(new Intent(this, HomePage.class));
      }
    }
    else
      startActivity(new Intent(this, LoginActivity.class));
  }
}
