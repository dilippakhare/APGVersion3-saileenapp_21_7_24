package com.apgautomation.controller;

import android.content.Context;

import com.apgautomation.model.LeaveDetails;
import com.apgautomation.model.LeaveModel;
import com.apgautomation.model.database.ItemDAOLeaveMaster;
import com.apgautomation.utility.serverutility.DownloadUtility;

public class LeaveController implements DownloadUtility
{
    Context context;
    public LeaveController(Context context)
    {
        this.context=context;
    }
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

    }

    public void saveLocalModel(LeaveModel model)
    {
        ItemDAOLeaveMaster itemDAOLeaveMaster=new ItemDAOLeaveMaster(context);
        itemDAOLeaveMaster.insertRecord(model);
    }

    public void insertLeaveDetailsRecord(LeaveDetails mObj) {
        ItemDAOLeaveMaster itemDAOLeaveMaster=new ItemDAOLeaveMaster(context);
        itemDAOLeaveMaster.insertLeaveDetailsRecord(mObj);
    }
}
