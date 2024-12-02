package com.apgautomation.fcm;

public class MainActivity
{
        /*extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fcm_mainactivity);
        //FirebaseMessagingService

        findViewById(R.id.txt).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subScibe();
            }
        });
    }
    void subScibe()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("news1")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       // String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                        //    msg = getString(R.string.msg_subscribe_failed);
                            Toast.makeText(MainActivity.this, "Subscibe Complete", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Subscibe Incomplete", Toast.LENGTH_SHORT).show();
                        }
                       // Log.d(TAG, msg);

                    }
                });
        // [END subscribe_topics]
    }


    @Override
    public void onClick(View v) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Dilip", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                      //  String msg = getString(R.string.msg_token_fmt, token);
                       // Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT,token);
                        startActivity(i);
                    }
                });
    } */
}
