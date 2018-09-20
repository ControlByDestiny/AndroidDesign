package com.designpattern.Study;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private final static String TAG="Service";
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"---onStartCommand---");
        Log.i(TAG,"||| flags->"+flags+"||| startId->"+startId);
        return super.onStartCommand(intent, flags, startId);
    }
    public final class MyBinder extends Binder{
        public MyBinder getBinder(){

            return new MyBinder();
        }
        public void sendBroadcast(Context context,String msg){
            Intent intent=new Intent();
            intent.setAction("com.test.MSG");
            intent.setData(Uri.parse(msg));
            context.sendBroadcast(intent);
        }

    }
    @Override
    public void onCreate() {
        Log.i(TAG,"---onCreate---");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"---onDestroy---");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"---onUnbind---");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG,"---onRebind---");
        super.onRebind(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG,"---onStart---");

        int i=0;
        while(true){
            Log.i(TAG,i+"");
            i++;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG,"---onBind---");
        return new MyBinder();
    }
}
