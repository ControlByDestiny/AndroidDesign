package com.designpattern.Study;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadCastReceiver extends BroadcastReceiver {
    private final static String TAG="MyBroadCastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"---receivedMsg---");
        Log.i(TAG,intent.getAction());
        Log.i(TAG,"MSG:"+intent.getData().toString());
    }
}
