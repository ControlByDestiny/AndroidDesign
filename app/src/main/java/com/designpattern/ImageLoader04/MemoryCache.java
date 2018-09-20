package com.designpattern.ImageLoader04;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class MemoryCache implements ImageCache {
    private final static String TAG="ImageCache";
    private LruCache<String,Bitmap> mLruCache;
    MemoryCache(){
        init();
    }
    private void init(){
        int maxMemoryCache= (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize=maxMemoryCache/8;
        mLruCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }
    @Override
    public Bitmap getBitmap(String key) {
        Log.i(TAG,"MemoryCache---getBitmap");
        return mLruCache.get(key);
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        mLruCache.put(key,bitmap);
        Log.i(TAG,"MemoryCache---putBitmap");
    }
}
