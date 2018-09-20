package com.designpattern.ImageLoader04;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class DiskCache implements ImageCache {
    private final static String TAG="ImageCache";
    private DiskLruCache mDiskLruCache;
    private Context mContext;
    DiskCache(Context context){
        this.mContext=context;
        init();
    }
    private void init(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                int appVersion=mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0).versionCode;
                File cacheFile=new File(Objects.requireNonNull(mContext.getExternalCacheDir()).getCanonicalPath()+"/"+mContext.getPackageName());
                mDiskLruCache=DiskLruCache.open(cacheFile,appVersion,1,10*1024*1024);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public Bitmap getBitmap(String key) {
        try {
            DiskLruCache.Snapshot snapshot=mDiskLruCache.get(key);
            Log.i(TAG,"DiskCache---getBitmap");
            if (snapshot != null)
                return BitmapFactory.decodeStream(snapshot.getInputStream(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        try {
            Log.i(TAG,"DiskCache---putBitmap");
            DiskLruCache.Editor editor=mDiskLruCache.edit(key);
            OutputStream outputStream=editor.newOutputStream(0);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
