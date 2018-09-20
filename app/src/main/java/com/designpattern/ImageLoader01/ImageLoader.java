package com.designpattern.ImageLoader01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.designpattern.util.MD5Util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    //图片缓存（缓存到内存中）
    LruCache<String, Bitmap> mCache;
    //实例化一个handler用于更新UI
    Handler uiHandler = new Handler(Looper.getMainLooper());
    //线程池，线程数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public ImageLoader(){
        init();
    }

    private void init() {
        Log.i("Bitmap",Runtime.getRuntime().availableProcessors()+"");
        int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize = maxMemory / 4;
        mCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String url, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }

    //1.加载图片
    public void loadImg(final String url, final ImageView imageView) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                if (mCache.get(url)!=null){
                    Log.i("Bitmap","mCache.get(url)!=null");
                    updateImg(imageView,mCache.get(url));
                }else{
                    final Bitmap bitmap = downloadImg(url);
                    if(bitmap!=null){
                        updateImg(imageView,bitmap);
                        mCache.put(url,bitmap);
                        try {
                            FileOutputStream fileOutputStream=new FileOutputStream("sdcard/Android/data/"+ MD5Util.getMD5(url)+".png");
                            boolean l = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            Log.i("Bitmap",l+"");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Bitmap","downloadImg(url)");
                }
            }



        });


    }
    //更新UI
    private void updateImg(final ImageView imageView, final Bitmap bitmap) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    //2.从网络获取图片
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
}
