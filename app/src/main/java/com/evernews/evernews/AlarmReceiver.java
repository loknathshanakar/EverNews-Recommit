package com.evernews.evernews;

/**
 * Created by lokanath on 4/11/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    String DB_PATH = "/data/data/com.evernews.evernews/databases/";
    String DB_NAME = "";
    String TABLE_NAME = "FULLNEWS_REV2";
    String RESERVED_3 = "RESERVED_3";   /**USED FOR LONG TIME**/
    String USERLOGINDETAILS = "USERLOGINDETAILS" ;
    String NOTIFICATIONENABLED="NOTIFICATIONENABLED";
    SharedPreferences sharedpreferences;
    int Category = 1;
    int RSSTitle = 3;
    int NewsTitle = 7;
    int NewsImage = 9;
    int mId=1;
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String test = sdf.format(cal.getTime());
        DB_NAME=TABLE_NAME;
        String path = DB_PATH + DB_NAME;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, 0);
        Cursor cur = db.query(TABLE_NAME, Initilization.col, null, null, null, null, RESERVED_3+" DESC");
        cur.moveToFirst();
        sharedpreferences = arg0.getSharedPreferences(USERLOGINDETAILS, Context.MODE_PRIVATE);
        for (int i = 0; i < cur.getCount(); i++) {
            if(cur.getString(Category).compareTo("Top News")==0 && sharedpreferences.getInt(NOTIFICATIONENABLED,-1) == 1){
                {
                    setNotification(arg0,cur.getString(RSSTitle),cur.getString(NewsTitle),cur.getString(NewsImage));
                    break;
                }
            }
            try{
                cur.moveToNext();
            }
            catch (CursorIndexOutOfBoundsException e){/****/}
        }
    }

    public void setNotification(Context context,String title,String text,String URL){
        final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        rv.setImageViewResource(R.id.remoteview_notification_icon, R.mipmap.news);
        rv.setTextViewText(R.id.remoteview_notification_headline, title);
        rv.setTextViewText(R.id.remoteview_notification_short_message, text);


        Intent resultIntent = new Intent(context, Initilization.class);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContent(rv)
                        .setPriority( NotificationCompat.PRIORITY_DEFAULT).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Initilization.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

// build notification

        final Notification notification = mBuilder.build();

// set big content view for newer androids
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            //notification.bigContentView = rv;
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, notification);

        NotificationTarget notificationTarget = new NotificationTarget(context, rv, R.id.remoteview_notification_icon, notification, mId);
        Glide.with( context).load(URL).asBitmap().fitCenter().placeholder(R.drawable.news).into(notificationTarget);
    }
}