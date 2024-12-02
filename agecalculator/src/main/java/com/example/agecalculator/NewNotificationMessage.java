package com.example.agecalculator;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NewNotificationMessage  {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewMessage";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of new message notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context,
                              final String newtitle,String details, final int number, PendingIntent pintent) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.age);


        final String ticker = newtitle;
        final String title = newtitle;
        final String text = details;



       /* PendingIntent contentIntent = PendingIntent.getActivity(context, number,
                new Intent(context, activity.getClass()).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_CANCEL_CURRENT);*/


        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "BootChannle";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;




        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID);

        // Set appropriate defaults for the notification light, sound,
        // and vibration.   .defaults|= Notification.DEFAULT_VIBRATE;
        builder .setDefaults(Notification.DEFAULT_VIBRATE)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.age )
                .setContentTitle(title)
                .setContentText(text)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(pintent)

                // Show expanded text content on devices running Android 4.1 or
                // later.
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText(details))
                .setAutoCancel(true)

                .setSound(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.when));


        // Example additional actions for this notification. These will
        // only show on devices running Android 4.1 or later, so you
        // should ensure that the activity in this notification's
        // content intent provides access to the same actions in
        // another way.


        //notify(context, builder.build(),number);



        notify(context, builder.build(),number,CHANNEL_ID,name,importance);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification,int number,String CHANNEL_ID,CharSequence  name,int importance)
    {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                Uri soundUri = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.when);
                        //Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getPackageName() + "/" + R.raw.when);

                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                // builder.setChannelId(CHANNEL_ID);

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                mChannel.setSound(soundUri, audioAttributes);
                nm.createNotificationChannel(mChannel);
            }
            nm.notify(NOTIFICATION_TAG, number, notification);

        } else {
            nm.notify(number, notification);
        }
    }



    /*
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int,long)}.
     */

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
