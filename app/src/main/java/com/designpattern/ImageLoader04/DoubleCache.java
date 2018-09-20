package com.designpattern.ImageLoader04;

import android.content.Context;
import android.graphics.Bitmap;


public class DoubleCache implements ImageCache {
    private Context mContext;
    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;
    DoubleCache(Context context){
        this.mContext=context;
        init();
    }

    private void init() {
        mMemoryCache=new MemoryCache();
        mDiskCache=new DiskCache(mContext);
    }

    @Override
    public Bitmap getBitmap(String key) {
        return mMemoryCache.getBitmap(key)==null?mDiskCache.getBitmap(key):mMemoryCache.getBitmap(key);
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        mMemoryCache.putBitmap(key, bitmap);
        mDiskCache.putBitmap(key, bitmap);
    }
}
