package com.designpattern.ImageLoader02;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {
    LruCache<String,Bitmap> mLruCache;

    ImageCache() {
        init();
    }

    private void init() {
        final int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize=maxMemory/8;
        mLruCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }

    public void put(String url,Bitmap bitmap){
        mLruCache.put(url,bitmap);
    }
    public Bitmap get(String url){
        return mLruCache.get(url);
    }
}
