package com.example.boundservice18102021;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button mBtnStartService,mBtnStopService;
    TextView mTvCount;
    boolean mBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStartService = findViewById(R.id.buttonStartService);
        mBtnStopService = findViewById(R.id.buttonStopService);
        mTvCount = findViewById(R.id.textViewCount);

        mBtnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
                bindService(intent,connection,BIND_AUTO_CREATE);
            }
        });

        mBtnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyService.class);
                stopService(intent);
                unbindService(connection);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isMyServiceRunning(MyService.class)){
            Intent intent = new Intent(MainActivity.this,MyService.class);
            bindService(intent,connection,BIND_AUTO_CREATE);
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            MyService myService = myBinder.getService();
            if (myService != null){
                myService.setOnListenerCountChange(new MyService.OnListenerCountChange() {
                    @Override
                    public void onCountChange(int count) {
                        mTvCount.setText("Count : " + count);
                    }
                });
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("BBB","Disconnect");
            mBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (isMyServiceRunning(MyService.class)){
            unbindService(connection);
            mBound = false;
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}