package com.apgautomation;


import androidx.multidex.*;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;


import io.realm.Realm;
import io.realm.RealmConfiguration;


public class UILApplication extends MultiDexApplication
{
  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  @SuppressWarnings("unused")
  @Override
  public void onCreate()
  {

    if (Constants.Config.DEVELOPER_MODE
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
    {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
              .detectAll().penaltyDialog().build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
              .detectAll().penaltyDeath().build());
    }

    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());

    setLanguage();

    Realm.init(this);
    //Realm.init(this);
    RealmConfiguration config = new RealmConfiguration.Builder().name("apgversion13.realm").build();
    Realm.setDefaultConfiguration(config);

    super.onCreate();
    initImageLoader(getApplicationContext());
  }

  private void setLanguage() {
    SharedPreferences preferences=getSharedPreferences("LanguagePref",MODE_PRIVATE);
    long langId=preferences.getLong("LanguagePref",0);

  }

  public static void initImageLoader(Context context)
  {
    // This configuration tuning is custom. You can tune every option, you
    // may tune some of them,
    // or you can create default configuration by
    // ImageLoaderConfiguration.createDefault(this);
    // method.


  }

	/*
	public void clearCMSCache()
	{
		ImageLoader i = ImageLoader.getInstance();
		i.clearDiscCache();
		i.clearMemoryCache();
	}
	*/


	//No Sql
}