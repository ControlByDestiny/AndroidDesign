package com.designpattern.ImageLoader03;

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
    private final static String TAG = "DiskCache";
    private DiskLruCache mDiskLruCache;
    private Context mContext;

    //初始化
    DiskCache(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        //判断外部SD卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                //获取app版本号
                int appVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
                //文件缓存位置
                File cacheFile = new File(Objects.requireNonNull(mContext.getExternalCacheDir()).getCanonicalPath() + "/" + mContext.getPackageName());
                //实例化DiskLruCache
                mDiskLruCache = DiskLruCache.open(cacheFile, appVersion, 1, 10 * 1024 * 1024);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Bitmap get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null)
                return BitmapFactory.decodeStream(snapshot.getInputStream(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        try {
            //缓存文件，文件名为key
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            //获取内容的输出流
            OutputStream outputStream = editor.newOutputStream(0);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            //回收bitmap
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
