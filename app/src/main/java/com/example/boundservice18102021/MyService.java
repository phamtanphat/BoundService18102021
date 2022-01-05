package com.example.boundservice18102021;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private String CHANNEL_ID = "CHANNEL_ID";
    private int REQUEST_CODE_MINUS = 1;
    private int REQUEST_CODE_PLUS = 2;
    private Notification mNotification;
    private int mCount = 0;
    private OnListenerCountChange mOnListenerCountChange;
    NotificationManager mNotificationManager;

    class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mNotification = createNotification(this, "Count : " + mCount);
        startForeground(1, mNotification);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mNotification);
        Log.d("BBB", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BBB", "onStartCommand");
        if (intent != null) {
            mCount += intent.getIntExtra("count", 0);
            if (mOnListenerCountChange != null){
                mOnListenerCountChange.onCountChange(mCount);
            }
            mNotification = createNotification(this, "Count : " + mCount);
            mNotificationManager.notify(1, mNotification);
        }
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("BBB","onUnbind");
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("BBB","Rebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("BBB", "onDestroy");
        super.onDestroy();
    }

    private Notification createNotification(Context context, String message) {
        Intent intentMinus = new Intent(context, MyService.class);
        intentMinus.putExtra("count", -1);

        PendingIntent pendingIntentMinus = PendingIntent.getService(context, REQUEST_CODE_MINUS, intentMinus, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlus = new Intent(context, MyService.class);
        intentPlus.putExtra("count", 1);

        PendingIntent pendingIntentPlus = PendingIntent.getService(context, REQUEST_CODE_PLUS, intentPlus, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle("Thông báo");
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setShowWhen(true);
        builder.addAction(R.mipmap.ic_launcher_round, "Plus", pendingIntentPlus);
        builder.addAction(R.mipmap.ic_launcher, "Minus", pendingIntentMinus);

        return builder.build();
    }
    public void setOnListenerCountChange(OnListenerCountChange onListenerCountChange){
        this.mOnListenerCountChange = onListenerCountChange;
    }

    interface OnListenerCountChange{
        void onCountChange(int count);
    }
}
