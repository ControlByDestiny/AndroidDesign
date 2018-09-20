package com.designpattern.ImageLoader03;

import android.content.Context;
import android.graphics.Bitmap;

public class DoubleCahce implements ImageCache{
    Context mContext;
    MemoryCache memoryCache;
    DiskCache diskCache;
    public DoubleCahce(Context context){
        this.mContext=context;
        init();
    }

    private void init() {
        memoryCache =new MemoryCache();
        diskCache=new DiskCache(mContext);
    }

    @Override
    public Bitmap get(String key) {
        if(memoryCache.get(key)!=null)
            return memoryCache.get(key);
        return diskCache.get(key);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        memoryCache.put(key,bitmap);
        diskCache.put(key,bitmap);
    }
}
