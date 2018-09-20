package com.designpattern.Study;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.designpattern.R;

import java.util.ArrayList;

public class ServiceActivity extends AppCompatActivity {
    Intent intent;
    private MyService.MyBinder myBinder;
    private final static String TAG="ServiceActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        final ServiceConnection serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder= (MyService.MyBinder) service;
                Log.i(TAG,"---onServiceConnected---");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG,"---onServiceDisconnected---");
            }
        };
        Log.i(TAG,"---onCreate---");
        findViewById(R.id.act_btn_startservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(ServiceActivity.this,MyService.class);
//                startService(intent);
                bindService(intent,serviceConnection,BIND_AUTO_CREATE);
            }
        });
        findViewById(R.id.act_btn_stopservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
//                stopService(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        Log.i(TAG,"---onStart---");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"---onResume---");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG,"---onPause---");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG,"---onStop---");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG,"---onRestart---");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"---onDestroy---");
        super.onDestroy();
    }
}
