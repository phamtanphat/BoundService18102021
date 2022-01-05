package com.example.boundservice18102021;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    String CHANNEL_ID = "CHANNEL_ID";
    int REQUEST_CODE_COUNT = 1;
    Notification mNotification;
    int mCount = 0;
    NotificationManager mNotificationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotification = createNotification(this,"Count : " + mCount);
        startForeground(1,mNotification);
        mNotificationManager.notify(1,mNotification);
        Log.d("BBB","onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BBB","onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("BBB","onDestroy");
        super.onDestroy();
    }

    private Notification createNotification(Context context, String message){
        Intent intentMinus = new Intent(context,MyService.class);
        intentMinus.putExtra("count",-1);

        PendingIntent pendingIntentMinus = PendingIntent.getService(context,REQUEST_CODE_COUNT,intentMinus,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlus = new Intent(context,MyService.class);
        intentPlus.putExtra("count",1);

        PendingIntent pendingIntentPlus = PendingIntent.getService(context,REQUEST_CODE_COUNT,intentPlus,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID);
        builder.setContentTitle("Thông báo");
        builder.setContentText(message);
        builder.setShowWhen(true);
        builder.addAction(R.mipmap.ic_launcher,"Plus",pendingIntentPlus);
        builder.addAction(R.mipmap.ic_launcher,"Minus",pendingIntentMinus);

        return builder.build();
    }
}
