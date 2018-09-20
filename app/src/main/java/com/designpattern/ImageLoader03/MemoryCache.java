package com.designpattern.ImageLoader03;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements ImageCache {
    private LruCache<String,Bitmap> mLruCache;
    public MemoryCache(){
        init();
    }

    private void init() {
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
    public Bitmap get(String key) {
        return mLruCache.get(key);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        mLruCache.put(key,bitmap);
    }
}
