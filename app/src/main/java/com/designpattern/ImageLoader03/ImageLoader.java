package com.designpattern.ImageLoader03;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.designpattern.util.MD5Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private final static String TAG="ImageLoader";
    ExecutorService mExecutorService;
    Handler mHandler;
    ImageCache mImageCache;
    public ImageLoader(){
        init();
    }

    private void init() {
        mExecutorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        mHandler=new Handler(Looper.getMainLooper());
        //设置默认缓存
        mImageCache=new MemoryCache();
    }

    public void setImgCahce(ImageCache imgCahce){
        this.mImageCache=imgCahce;
    }
    public void loadImg(final String url, final ImageView imageView) {
        if (mImageCache.get(MD5Util.getMD5(url)) != null) {
            Log.i(TAG, "--getCache--");
            updateImg(imageView, mImageCache.get(MD5Util.getMD5(url)));
            return;
        }
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImg(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    updateImg(imageView, bitmap);
                }
                mImageCache.put(MD5Util.getMD5(url), bitmap);
            }
        });
    }
    private Bitmap downloadImg(final String url) {
        Bitmap bitmap = null;
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void updateImg(final ImageView imageView, final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
