package com.designpattern.CustomView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.designpattern.R;

public class ViewActivity extends AppCompatActivity {
    CustomView customView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
//        customView=findViewById(R.id.custom1);
//        Drawable drawable=getResources().getDrawable(R.drawable.ic_mouse);
//        customView.setCustomDrawable(drawable);
//        customView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ViewActivity.this,"点击？"+v.getId(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        customView.setCustomBitmap();
//        customView.setBackgroundColor(Color.RED);
//        ImageView imageView=null;
//        imageView.setImageDrawable();
    }
}
