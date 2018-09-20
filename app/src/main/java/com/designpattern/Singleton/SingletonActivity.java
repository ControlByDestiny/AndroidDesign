package com.designpattern.Singleton;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.designpattern.R;

import java.util.HashMap;
import java.util.Iterator;

public class SingletonActivity extends AppCompatActivity {
    Button btn;
    HashMap<String, Integer> temp = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleton);
        btn = findViewById(R.id.button);
        String[] a = {"abc", "Abc", "ABc", "abc", "acd", "Abc"};
        int i = 0;
        while (i < a.length) {
            if (temp.containsKey(a[i]))
                temp.put(a[i],temp.get(a[i]) + 1);
            else
                temp.put(a[i], 1);
            i++;
        }
        for (String tempKey : temp.keySet()) {
            Log.i("String", "---key=" + tempKey + "  value=" + temp.get(tempKey));

        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager wm = getWindowManager();
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main, null);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = 400;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.type = WindowManager.LayoutParams.FIRST_SUB_WINDOW;
                layoutParams.format = 1;
                wm.addView(view, layoutParams);


            }
        });

    }
}
