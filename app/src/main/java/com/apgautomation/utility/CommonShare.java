package com.apgautomation.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.apgautomation.model.AreaMaster;
import com.apgautomation.model.AttendanceStatusBean;
import com.apgautomation.model.ComplaintTypeModel;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonComplaintStatus;
import com.apgautomation.model.GsonEquipment;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.GsonSalesVisitType;
import com.apgautomation.model.GsonVisitType;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

public class CommonShare
{
    public static String attendanceSync="attendanceSync";
    public static String todoSync="todoSync";
    public static String visitSync="visitSync";
    public static  String url=//"http://192.168.43.239/app/";
   //  "http://117.218.170.92/app/";
    //  "http://115.124.127.109/app/";
          // "http://174.141.238.216/app/";
    "https://apgautomation.co.in/app/";
   // "https://apgmobileservice.conveyor.cloud/";
          //  "http://115.124.127.109/saileenapp/";
         // "http://103.142.174.118/appv1/";
    public static  String urlv1=//"http://192.168.43.239/app/";
            // "http://117.218.170.92/app/";
  // "http://115.124.127.109/app/";
          //"http://174.141.238.216/app/";
    "https://apgautomation.co.in/app/";
    public static CharSequence url1 ="http://174.141.238.216/app/";
    //  "http://103.142.174.118/appv1/";
  //  "https://apgmobileservice.conveyor.cloud/";

    public static void alert(Context context, String str)
    {
       AlertDialog.Builder alert=new AlertDialog.Builder(context);
       alert.setMessage(str);
       alert.setPositiveButton("Ok",null);
       alert.show();
    }

    public static boolean isFsrEnabled(Context context)
    {
       return true;
    }

    //------------  App Common Code --------------------
    public static void saveLoginData(Context context, String str)
    {
        context.getSharedPreferences("LoginData",Context.MODE_PRIVATE).edit().putString("LoginData",str).commit();
    }
    public static void clearLoginData(Context context)
    {
        context.getSharedPreferences("LoginData",Context.MODE_PRIVATE).edit().clear().commit();
    }
    public static  int getUserId(Context context)
    {
        try {
         String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");
            return jObj.getInt("UserId");
            }
        catch (Exception ex){

        }
        return  0;
    }
    public static  String getContactName(Context context)
    {
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");
            return jObj.getString("ContactName");
        }
        catch (Exception ex){

        }
        return  "";
    }
    public static  String getContactNumber(Context context)
    {
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");
            return jObj.getString("MobileNo");
        }
        catch (Exception ex){

        }
        return  "";
    }
    public static  int getCustomerId(Context context)
    {
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");
            return jObj.getInt("custid");
        }
        catch (Exception ex){

        }
        return  0;
    }
    public static String getCustomerIds(Context context) {
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");
            return jObj.getString("CustIds");
        }
        catch (Exception ex){

        }
        return  "";
    }

    public static HashMap<Integer,EmployeeModel> empMap=new HashMap<>();
    public static void GenerateEmployeeMap(Context context)
    {
        empMap.clear();
       ArrayList<EmployeeModel> list=  getEmployeeList(context);
        for (EmployeeModel model:list
             ) {
            empMap.put(model.EmpId,model);
        }
    }

    public static int getEmpId(Context context)     {
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");
            return jObj.getInt("empid");
        }
        catch (Exception ex){

        }
        return  0;
    }
    public static String getEmpName(Context context)     {
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");

            return jObj.getString("empname");
        }
        catch (Exception ex){

        }
        return  "";
    }
    public static String getRole(Context context) {
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONObject jObj=  jsonObject.getJSONObject("user");
            return jObj.getString("rolename");
        }
        catch (Exception ex){

        }
        return  "";
    }

    public static ArrayList<AttendanceStatusBean> getAttendanceStatusList(Context context)
    {
        ArrayList<AttendanceStatusBean> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("atteStatus");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                AttendanceStatusBean bean=gson.fromJson(jArray.getJSONObject(i).toString(),AttendanceStatusBean.class);
                list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }

    public static ArrayList<ComplaintTypeModel> getClientComplaintType(Context context)
    {
        ArrayList<ComplaintTypeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("complaintTypeList");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                ComplaintTypeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),ComplaintTypeModel.class);
                if(bean.IsClientType)
                list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static ArrayList<ComplaintTypeModel> getApgComplaintType(Context context)
    {
        ArrayList<ComplaintTypeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("complaintTypeList");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                ComplaintTypeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),ComplaintTypeModel.class);
                if(bean.IsApgType)
                    list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static ArrayList<EmployeeModel> getEmployeeList(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("empList");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
                list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static ArrayList<EmployeeModel> getActiveEmployeeList(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("empList");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
                if(bean.DeleteStatus==false)
                 list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static ArrayList<EmployeeModel> geSEmployeeList(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try
        {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("empList");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
                if(bean.DeptId==3)
                {
                    list.add(bean);
                }
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static ArrayList<EmployeeModel> geSalesEmployeeList(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try
        {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("empList");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
                if(bean.DeptId==2)
                {

                       list.add(bean);
                }
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static ArrayList<EmployeeModel> getQEmployeeList(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("quotationResponsible");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
                list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static ArrayList<EmployeeModel> getAskQueryList(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("askPerson");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
                list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }
    public static EmployeeModel getMyEmployeeModel(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("empList");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
               // list.add(bean);
                if(bean.EmpId==CommonShare.getEmpId(context)) {
                    //  CommonShare.alert(context,bean.IsOldCamera+"--"+jArray.getJSONObject(i).toString());
                    return bean;
                }
            }

        }
        catch (Exception ex){

        }
        return  null;
    }
    public static ArrayList<EmployeeModel> getTeamList(Context context)
    {
    ArrayList<EmployeeModel> list=new ArrayList<>();
    try {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

        JSONObject jsonObject=new JSONObject(str);
        JSONArray jArray=  jsonObject.getJSONArray("team");
        Gson gson=new Gson();
        for(int i=0;i<jArray.length();i++)
        {
            EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
            list.add(bean);
        }

    }
    catch (Exception ex){

    }
    return  list;
}
    public static ArrayList<AreaMaster> getArea(Context context)
    {
        ArrayList<AreaMaster> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("Areas");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                AreaMaster bean=gson.fromJson(jArray.getJSONObject(i).toString(),AreaMaster.class);
                list.add(bean);
            }

        }
        catch (Exception ex){

        }
        return  list;
    }

    public static ArrayList<EmployeeModel> getTeamListPlusOwn(Context context)
    {
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
            JSONArray jArray=  jsonObject.getJSONArray("team");
            Gson gson=new Gson();
            for(int i=0;i<jArray.length();i++)
            {
                EmployeeModel bean=gson.fromJson(jArray.getJSONObject(i).toString(),EmployeeModel.class);
                list.add(bean);
            }
            ArrayList<EmployeeModel>  empList=   getEmployeeList(context);
            int EmpId= CommonShare.getEmpId(context);
            for (EmployeeModel model:empList
                 ) {
                if(EmpId==model.EmpId)
                {
                    list.add(model);
                }
            }
        }
        catch (Exception ex)
        {

        }
        return  list;
    }
    public static EmployeeModel getManager(Context context)
    {
      try
      {
            String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");

            JSONObject jsonObject=new JSONObject(str);
           // JSONArray jArray=  jsonObject.getJSONArray("team");
            Gson gson=new Gson();
            EmployeeModel bean=gson.fromJson(jsonObject.getJSONObject("manager").toString(),EmployeeModel.class);
            return  bean;
        }
        catch (Exception ex){

        }
        return  null;
    }

    public static String getSelectedStatusTextById(int statusId,Context context) {
        ArrayList<AttendanceStatusBean> list=getAttendanceStatusList(context);
        for (AttendanceStatusBean  bean:list
             )
        {
            if(bean.StatusId== statusId )
                return  bean.StatusName;
        }
        return "Select";
    }

    public  static  String generateToken(Context context)
    {
        String token=CommonShare.getUserId(context)+"_"+System.currentTimeMillis()+"_";
        try
        {
            token=token+""+new DeviceUuidFactory(context).getDeviceUuid();
        }
        catch (Exception ex){}

        return token;
    }

    public static void saveGroupAndEquipment(Context context, String str) {
    SharedPreferences sharedPreferences=context.getSharedPreferences("GroupAndEquipment",Context.MODE_PRIVATE);
    sharedPreferences.edit().putString("GroupAndEquipment",str).commit();
    }
    public static ArrayList<GsonEquipment> getEquipments(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("GroupAndEquipment",Context.MODE_PRIVATE);
       // sharedPreferences.edit().putString("GroupAndEquipment",str).commit();
        ArrayList<GsonEquipment> list=new ArrayList<>();
        try
        {
         JSONObject jsonObject=new JSONObject(sharedPreferences.getString("GroupAndEquipment",""));
          JSONArray array=jsonObject.getJSONArray("equipmenttype");
          Gson g=new Gson();
          for(int i=0;i<array.length();i++)
          {
             list.add(   g.fromJson(array.getJSONObject(i).toString(),  GsonEquipment.class));
          }
        }
        catch (Exception ex)
        {}
        return  list;
    }
    public static ArrayList<GsonGroup> getGroups(Context context) {
       /*

        SharedPreferences sharedPreferences=context.getSharedPreferences("GroupAndEquipment",Context.MODE_PRIVATE);
        // sharedPreferences.edit().putString("GroupAndEquipment",str).commit();
        ArrayList<GsonGroup> list=new ArrayList<>();
        try
        {
            JSONObject jsonObject=new JSONObject(sharedPreferences.getString("GroupAndEquipment",""));
            JSONArray array=jsonObject.getJSONArray("groups");
            Gson g=new Gson();
            for(int i=0;i<array.length();i++)
            {
                list.add(   g.fromJson(array.getJSONObject(i).toString(),  GsonGroup.class));
            }
        }
        catch (Exception ex)
        {}
        return  list;

        */
        ArrayList<GsonGroup> list=new ArrayList<>();
        Realm realm;   realm=Realm.getDefaultInstance();
        Gson gson=new Gson();
        ArrayList <GsonGroup>  mList=null;
        EmployeeModel m= CommonShare.getMyEmployeeModel(context);
        if(m.DeptId==3)
        {
            mList =new ArrayList(    realm.where(GsonGroup.class).equalTo("ISVeryfied",true).findAll());
        }
        else
        {
            mList =new ArrayList(    realm.where(GsonGroup.class).findAll());
        }
        for ( int i=0;i<mList.size();i++ )
        {
            GsonGroup g=realm.copyFromRealm(mList.get(i));
            list.add(g);
        }

        return  list;
    }
    public static ArrayList<GsonGroup> getGroupsVerified(Context context)
    {

        ArrayList<GsonGroup> list=new ArrayList<>();
        Realm realm;   realm=Realm.getDefaultInstance();
        Gson gson=new Gson();
        ArrayList <GsonGroup>  mList=null;
        EmployeeModel m= CommonShare.getMyEmployeeModel(context);

        mList =new ArrayList(    realm.where(GsonGroup.class).equalTo("ISVeryfied",true).findAll());

        for ( int i=0;i<mList.size();i++ )
        {
            GsonGroup g=realm.copyFromRealm(mList.get(i));
            list.add(g);
        }
        return  list;
    }
    public static ArrayList<GsonVisitType> getVisitTypes(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("GroupAndEquipment",Context.MODE_PRIVATE);
        // sharedPreferences.edit().putString("GroupAndEquipment",str).commit();
        ArrayList<GsonVisitType> list=new ArrayList<>();
        try
        {
            JSONObject jsonObject=new JSONObject(sharedPreferences.getString("GroupAndEquipment",""));
            JSONArray array=jsonObject.getJSONArray("visitTypes");
            Gson g=new Gson();
            GsonVisitType vtype=new GsonVisitType();
            vtype.TypeId=0;
            vtype.VisitType1="--Select--";
            list.add(vtype);
            for(int i=0;i<array.length();i++)
            {
                list.add(   g.fromJson(array.getJSONObject(i).toString(),  GsonVisitType.class));
            }
        }
        catch (Exception ex)
        {}
        return  list;
    }

    public static ArrayList<GsonComplaintStatus> getComplaintStatusList(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        // sharedPreferences.edit().putString("GroupAndEquipment",str).commit();
        ArrayList<GsonComplaintStatus> list=new ArrayList<>();
        try
        {
            JSONObject j=new JSONObject(str);
            //JSONObject jsonObject=
                    //new JSONObject(sharedPreferences.getString("complaintStatus",""));
            JSONArray array=j.getJSONArray("complaintStatus");
            Gson g=new Gson();
            GsonComplaintStatus vtype=new GsonComplaintStatus();
            vtype.StatusId=0;
            vtype.Status="--Select--";
            list.add(vtype);
            for(int i=0;i<array.length();i++)
            {
                list.add(   g.fromJson(array.getJSONObject(i).toString(),  GsonComplaintStatus.class));
            }
        }
        catch (Exception ex)
        {}
        return  list;
    }
    public static ArrayList<GsonSalesVisitType> getSalesVisitTypes(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        // sharedPreferences.edit().putString("GroupAndEquipment",str).commit();
        ArrayList<GsonSalesVisitType> list=new ArrayList<>();
        try
        {
            JSONObject jsonObject=new JSONObject(str);
            JSONArray array=jsonObject.getJSONArray("salesVisitType");
            Gson g=new Gson();
            GsonSalesVisitType vtype=new GsonSalesVisitType();
            vtype.SaleVisitTypeId=0;
            vtype.VisitType="--Select--";
            list.add(vtype);
            for(int i=0;i<array.length();i++)
            {
                list.add(   g.fromJson(array.getJSONObject(i).toString(),  GsonSalesVisitType.class));
            }
        }
        catch (Exception ex)
        {}
        return  list;
    }

    public static ArrayList<EmployeeModel> getSectionList(Context context,int section) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        // sharedPreferences.edit().putString("GroupAndEquipment",str).commit();
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try
        {
            JSONObject j=new JSONObject(str);
            //JSONObject jsonObject=
            //new JSONObject(sharedPreferences.getString("complaintStatus",""));
            JSONArray array=j.getJSONArray("muterialsection");
            Gson g=new Gson();
            for(int i=0;i<array.length();i++)
            {
               // list.add(   g.fromJson(array.getJSONObject(i).toString(),  GsonComplaintStatus.class));
              String empIds= array.getJSONObject(i).getString("EmpIds");
              if(i==section-1)
              {
                  String [] arr=empIds.split(",");
                  for(int jc=0;jc<arr.length;jc++){
                      EmployeeModel m=CommonShare.empMap.get(Integer.parseInt(arr[jc]));
                      list.add(m);
                  }

              }
            }
        }
        catch (Exception ex)
        {}
        return  list;
    }

    public static boolean isServiceVisitPhotoEnabled(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        try
        {
            JSONObject j=new JSONObject(str);
            JSONObject obj=j.getJSONObject("setting");
            return  obj.getBoolean("IsServicePhoto");
        }
        catch (Exception ex)
        {}
        return  false;
    }
    public static boolean isSalesVisitPhotoEnabled(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        try
        {
            JSONObject j=new JSONObject(str);
            JSONObject obj=j.getJSONObject("setting");
            return  obj.getBoolean("IsSalesPhoto");
        }
        catch (Exception ex)
        {}
        return  false;
    }

    public static ArrayList<EmployeeModel> getEnquirySentTo(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try
        {
            JSONObject j=new JSONObject(str);
            JSONObject obj=j.getJSONObject("setting");
            String empIds=j.getJSONObject("setting").getString("EnquirySentToIds");
            String [] arr=empIds.split(",");
            for(int jc=0;jc<arr.length;jc++){
                EmployeeModel m=CommonShare.empMap.get(Integer.parseInt(arr[jc]));
                list.add(m);
            }
        }
        catch (Exception ex)
        {}
        return  list;
    }

    public static String [] getEnquiryProducts(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try
        {
            JSONObject j=new JSONObject(str);
            JSONObject obj=j.getJSONObject("setting");
            String empIds=j.getJSONObject("setting").getString("EnquiryProducts");
            String [] arr=empIds.split(",");
            return  arr;
        }
        catch (Exception ex)
        {}
        return  null;
    }

    public static boolean isEnquiryAthurity(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        ArrayList<EmployeeModel> list=new ArrayList<>();
        try
        {
            JSONObject j=new JSONObject(str);
            JSONObject obj=j.getJSONObject("setting");
            String empIds=j.getJSONObject("setting").getString("EnquiryAuthorityIds");
            String [] arr=empIds.split(",");
            for(int jc=0;jc<arr.length;jc++){
                EmployeeModel m=CommonShare.empMap.get(Integer.parseInt(arr[jc]));
                list.add(m);
                if(m.EmpId==CommonShare.getEmpId(context))
                {
                    return  true;
                }
            }
        }
        catch (Exception ex)
        {}
        return  false;
    }

    public static boolean isResposibleSectionPersion(Context context,int section) {

             ArrayList<EmployeeModel> list=getSectionList(context,section);
             for (EmployeeModel m:list)
             {
                   if(m.EmpId==CommonShare.getEmpId(context))
                       return  true;
             }
            return  false;
    }
    //--------------------------------------------------
    public static boolean checkPhoneStatePermission(final Activity context)
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED   )
        {


            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.CAMERA))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                /*
                 */
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}
                        ,
                        1947

                );


            }
            else
            {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Permission needed.");
                alert.setMessage("App requires permission.");
                alert.setPositiveButton("Accept Permission ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                             /*
							ActivityCompat.requestPermissions(context,
									new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}
									,
									1947

							); */


                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                    }
                });
                alert.setNegativeButton("Cancel",null);
                alert.show();
            }

        }
        return false;
    }

    public static void hideSoftKeyBord(Activity context)
    {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = context.getCurrentFocus();
        if (v == null)
            return;


        inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideatInItInputBoard(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //----------- Date Function ---------//
    public static long ddMMYYDateToLong(String ddmmyyy) {
        ddmmyyy=ddmmyyy.replace("/","-");
        long longDate = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(ddmmyyy);
            longDate = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longDate;
    }
    public static long parseDate(String strDate) {
        try
        {
            return Long.valueOf(strDate.replace("/Date(", "").replace(")/", ""));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    public static long parseDate1(String trim) {
        try
        {
           // String[] arr=trim.split("/Date(");
          //  String s=arr[1].split(")/")[0];
            String s=trim.substring(8,trim.length()-4);
            return Long.parseLong(s);
        }
        catch (Exception ex){
            ex.toString();
        }
        return 0;
    }

    public static String convertToDate(long longTime) {
        Date date = new Date(longTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        if(longTime==0)
            return "-";
        return strDate;
    }
    public static String getDateTime(long longTime)
    {
        Date date = new Date(longTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String strDate = dateFormat.format(date);
        return strDate;

    }
    public static Date  getJavaDate(long longTime)
    {
        Date date = new Date(longTime);
        return date;

    }
    public static  long getForwordDay(long Millisecond,int days)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(Millisecond));
        cal.add(Calendar.DAY_OF_YEAR, days);

        return cal.getTimeInMillis();
    }


    //----------  Android Common Code -------------------
    public static String getEncodeString(String str) {
        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isAttendanceVerification(Context context) {
        String str=   context.getSharedPreferences("LoginData", Context.MODE_PRIVATE).getString("LoginData", "{}");
        try
        {
            JSONObject j=new JSONObject(str);
            JSONObject obj=j.getJSONObject("setting");
            return  obj.getBoolean("IsAttendanceVerification");
        }
        catch (Exception ex)
        {}
        return  false;
    }


    public void call(String number, Context context) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        context.startActivity(i);
    }

    public void send(String subject, String text, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, subject + "\n" + text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(intent);
    }

    public boolean checkEmail(String email) {
        if (!email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            return false;
        } else {
            return true;
        }
    }

    public void email(Activity context, String emailTo) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder
                .from(context);
        builder.setType("message/rfc822");
        builder.addEmailTo(emailTo);
        builder.setSubject("Subject");
        builder.setChooserTitle("");
        builder.startChooser();
    }

    public void openWebsite(Activity activity, String string) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(string));
        activity.startActivity(intent);
    }

    static public void setRemindar(long millisecond, String title, String guest, String venue, String description, Context context) {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", millisecond + 8 * 60 * 60 * 1000);
        intent.putExtra("allDay", false);
        //intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", millisecond + 9 * 60 * 60 * 1000);
        intent.putExtra("title", "" + title);
        intent.putExtra("guests", "" + guest);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "" + venue);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "" + description);
        //intent.putExtra(Events., ""+venue);
        intent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "" + description);
        //intent.putExtra(Events., ""+venue);
      // intent.putExtra(Events.HAS_ALARM, 1);
        context.startActivity(intent);
    }


    public static String getMimeType(String url) {
        String type = null;
        try {

            try {
                url = url.replace(" ", "");
                url = url.replace("+", "");
                url = url.replace("-", "");
                url = url.replace("*", "");
                url = url.replace("~", "");
                url = url.replace("`", "");
                url = url.replace("!", "");
                url = url.replace("@", "");
                url = url.replace("#", "");
                url = url.replace("$", "");
                url = url.replace("%", "");
                url = url.replace("^", "");
                url = url.replace("&", "");
                url = url.replace("(", "");
                url = url.replace(")", "");
                url = url.replace("|", "");
                url = url.replace("'", "");
                url = url.replace(";", "");
                url = url.replace(":", "");
                url = url.replace(",", "");
                url = url.replace("<", "");
                url = url.replace(">", "");
                url = url.replace("?", "");
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);

        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    public static void openFile(String path, Context context) {
        try
        {
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, getMimeType(path));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Found to open this file", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error While Opening File", Toast.LENGTH_LONG).show();
        }
    }

    public static void openMap(Context context,String query)
    {
        Uri gmmIntentUri = Uri.parse("geo:19.838330,74.489664?q=" + Uri.encode("Om Gurudev English Medium Gurukul- Up down , At: Kokamthan Post: Jeur-Kumbhari, Shirdi-Kopargaon Road, 423601"));

        Uri gmmIntentUri1 = Uri.parse("google.navigation:q="+query);
        //Uri.parse("google.navigation:q=ATMA MALIK INTERNATIONAL SCHOOL");

        Uri gmmIntentUri2 = Uri.parse("google.navigation:19.838330,74.489664?q=ATMA MALIK INTERNATIONAL SCHOOL");

        Intent intent = new Intent(Intent.ACTION_VIEW,
                gmmIntentUri);
        //  Uri.parse("http://maps.google.com/maps?daddr=16.694549,74.224205 (Sarthi Pure Veg Restaraunt)"));
        intent.setPackage("com.google.android.apps.maps");

        context.startActivity(intent);
    }
    public static void openMap(Context context)
    {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //  Uri.parse("http://maps.google.com/maps?daddr=16.694549,74.224205 (Sarthi Pure Veg Restaraunt)"));
        intent.setPackage("com.google.android.apps.maps");

        context.startActivity(intent);
    }
    public static void openMapWithGeo(Context context,String marker,String geolocation)
    {

        try {
            Uri gmmIntentUri = Uri.parse("geo:" + geolocation + "?q=" + geolocation + "(" + Uri.encode(marker) + ")");


            Intent intent = new Intent(Intent.ACTION_VIEW,
                    gmmIntentUri);
            //  Uri.parse("http://maps.google.com/maps?daddr=16.694549,74.224205 (Sarthi Pure Veg Restaraunt)"));
            intent.setPackage("com.google.android.apps.maps");

            context.startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(context,"Unable to find map application",Toast.LENGTH_LONG).show();
        }
    }


    private void addAutoStartup(Context context) {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list =context. getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                context. startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException
    {
        if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try
            {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
