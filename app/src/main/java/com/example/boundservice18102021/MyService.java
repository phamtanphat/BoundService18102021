package com.example.boundservice18102021;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    String CHANNEL_ID = "CHANNEL_ID";
    int REQUEST_CODE_COUNT = 1;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
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
