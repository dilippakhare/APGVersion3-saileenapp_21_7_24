package com.example.agecalculator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BootReciever extends BroadcastReceiver {

    private PeriodicWorkRequest periodicWorkRequest;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        PeriodicWorkRequest unused = BootReciever.this.periodicWorkRequest = (PeriodicWorkRequest) new PeriodicWorkRequest.Builder((Class<? extends ListenableWorker>) MyWorker.class, 1, TimeUnit.HOURS).build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("My Work", ExistingPeriodicWorkPolicy.REPLACE, BootReciever.this.periodicWorkRequest);
    }
}