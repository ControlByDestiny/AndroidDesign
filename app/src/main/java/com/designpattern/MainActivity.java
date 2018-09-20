package com.designpattern;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.designpattern.ImageLoader03.DoubleCahce;
import com.designpattern.ImageLoader03.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;


public class MainActivity extends AppCompatActivity {
    ImageView imageView,imageView1;
    ImageLoader imageLoader =new ImageLoader();
    EditText et_url;
    Button btn_load;
    TextView tv_cache;
    final String imgurl="http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg";
    DiskLruCache mDiskLruCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        et_url=findViewById(R.id.act_et_url);
        btn_load=findViewById(R.id.act_btn_load);
        tv_cache=findViewById(R.id.act_tv_cache);
        imageLoader.setImgCahce(new DoubleCahce(this));
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
        }
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempUrl;
                tempUrl=et_url.getText().toString();
                if(tempUrl.length()>5){
                    imageLoader.loadImg(tempUrl,imageView);
                }else {
                    imageLoader.loadImg(imgurl,imageView);
                }
            }
        });

    }
}
