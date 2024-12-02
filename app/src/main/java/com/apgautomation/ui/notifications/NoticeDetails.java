package com.apgautomation.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.NoticeServerModel;
import com.apgautomation.utility.CommonShare;
import com.squareup.picasso.Picasso;

public class NoticeDetails extends AppCompatActivity {

    TextView txtTopic,txtDate,txtDetails;
    ImageView img;
    public static NoticeServerModel model;
    Button btnAttachment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        txtTopic=(TextView)findViewById(R.id.txtTopic);
        txtDate=(TextView)findViewById(R.id.txtDate);
        txtDetails=(TextView)findViewById(R.id.txtDetails);
        img=findViewById(R.id.img);
        txtTopic.setText(model.Topic);
        txtDetails.setText(model.Details);
        txtDate.setText(CommonShare.convertToDate(  CommonShare.parseDate(model.EnterDate)));
        try
        {
            String url=model.Attachment.replace("~",CommonShare.url1);
            if(url.length()>5)
             Picasso.get().load(  url).placeholder(R.drawable.loading).into(img);
        }
        catch (Exception ex)
        {}
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice Details");
        btnAttachment=findViewById(R.id.btnAttachment);
        try
        {
            if(model.Attachment.length()>5)
              if(!isImageFile(model.Attachment))
              {

                  btnAttachment.setVisibility(View.VISIBLE);
                  btnAttachment.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          String url=model.Attachment.replace("~",CommonShare.url);
                          Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                          startActivity(intent);
                      }
                  });
              }
        }
        catch (Exception ex)
        {}

    }
    private boolean isImageFile(String file)
    {
        String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
        for (String extension : okFileExtensions)
        {
            if (file.toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
