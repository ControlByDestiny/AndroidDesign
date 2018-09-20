package com.designpattern.ImageLoader04;

import android.content.Context;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.designpattern.R;

public class SimpleActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        progressBar = findViewById(R.id.act_pgb_imgload04);
        imageView = findViewById(R.id.act_iv_imgload04);
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setCache(new DoubleCache(this))
                .setThreadCount(2)
//                .setLoadingProgressListener(new ImageLoader.DownLoadListener() {
//
//                    @Override
//                    public void onImageDownloadStart(final int imageLength) {
//                        SimpleActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setVisibility(ProgressBar.VISIBLE);
//                                progressBar.setMax(imageLength);
//                                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onImageDownloading(final int downloadLength) {
//                        SimpleActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setProgress(downloadLength);
//                            }
//                        });
//                    }
//
//
//                    @Override
//                    public void onImageDownloaded() {
//                        SimpleActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setVisibility(ProgressBar.GONE);
//                            }
//                        });
//                    }
//                })
                .setLoadingFailPlaceholder(android.R.drawable.ic_menu_report_image).create();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage("http://img.zcool.cn/community/01858e59jbhjghghghgfhfghfghfhgfhgfhfhfhffhg1fe6a6b5b3086ed473c1d0.jpg", imageView);

        //http://img0.imgtn.bdimg.com/it/u=2232516768,2646377770&fm=27&gp=0.jpg
    }
}
