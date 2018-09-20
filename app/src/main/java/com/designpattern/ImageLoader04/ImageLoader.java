package com.designpattern.ImageLoader04;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import android.widget.ImageView;

import com.designpattern.util.MD5Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//imgurl--http://img.zcool.cn/community/01858e591fe6a6b5b3086ed473c1d0.jpg
public final class ImageLoader {
    private final static String TAG = "ImageLoader";
    //volatile变量级别的同步锁，当变量的值发生改变时，会尽快通知其他线程，保证了变量的修改可见性
    private static volatile ImageLoader mInstance = null;
    //ImageLoader配置
    private ImageLoaderConfig mConfig = null;
    //缓存设置
    private ImageCache mCache = null;
    //下载线程数
    private int mThreadCount = -1;
    //线程池
    private ExecutorService mExecutorService;
    //用于更新UI的Handler
    private Handler mHandler;
    private DisplayConfig mDisplayConfig;
    private final static int BUFFER_SIZE = 16 * 1024;

    private ImageLoader() {
    }

    //DCL单例模式
    public static ImageLoader getInstance() {
        if (mInstance == null) {
            //synchronized比volatile重的同步锁，可以对方法，变量，类等进行同步锁，保证了再程序执行时，同一时刻只有一个线程可以进行操作，而其他线程被阻塞
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void init(ImageLoaderConfig config) {
        this.mConfig = config;
        this.mCache = mConfig.mCache;
        this.mThreadCount = mConfig.threadCount;
        mHandler = new Handler(Looper.getMainLooper());
        mDisplayConfig = mConfig.displayConfig;
        checkConfig();
        this.mExecutorService = Executors.newFixedThreadPool(mThreadCount);
    }

    //检查配置
    private void checkConfig() {
        if (mConfig == null) {
            throw new RuntimeException("the config of ImageLoaderConfig is null");
        }
        if (mCache == null) {
            mCache = new MemoryCache();
        }
    }

    public void displayImage(final String imageUrl, final ImageView imageView) {
        if (mCache.getBitmap(MD5Util.getMD5(imageUrl)) != null) {
            Log.i(TAG, "--getCache--");
            updateImage(imageView, mCache.getBitmap(MD5Util.getMD5(imageUrl)));
            return;
        }
        imageView.setTag(imageUrl);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                if (mConfig.displayConfig.downLoadListener != null) {
                    bitmap = downloadImage(imageUrl, mConfig.displayConfig.downLoadListener);
                } else {
                    bitmap = downloadImage(imageUrl);
                }
                if (bitmap == null) {
                    updateImage(imageView, mConfig.displayConfig.failedResId);
                }
                if (imageView.getTag().equals(imageUrl)) {
                    updateImage(imageView, bitmap);
                }
                mCache.putBitmap(MD5Util.getMD5(imageUrl), bitmap);
            }
        });
    }

    public void submitLoadRequest(String imageUrl, ImageView imageView) {
    }

    //下载图片
    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection urlConnection = url.openConnection();
            bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    //带有接口回调的下载图片，实时传送下载进度
    private Bitmap downloadImage(String imageUrl, DownLoadListener downloadListener) {
        Bitmap bitmap = null;
        try {
            //将传过来的URL进行URL编码
            URL url = new URL(imageUrl);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            URLConnection urlConnection = url.openConnection();
            //获取图片总大小
            final int fileLength = urlConnection.getContentLength();
            downloadListener.onImageDownloadStart(fileLength);
            Log.i(TAG, "文件大小：" + fileLength);
            //网络请求的输入流（相对于客户端）
            InputStream inputStream = urlConnection.getInputStream();
            //临时存储数据
            byte[] tempData = new byte[BUFFER_SIZE];
            //临时下载字节数
            int count;
            //已下载字节数
            int downloadFilesize = 0;
            while ((count = inputStream.read(tempData)) != -1) {
                downloadFilesize += count;
                byteArrayOutputStream.write(tempData, 0, tempData.length);
                byteArrayOutputStream.flush();
                downloadListener.onImageDownloading(downloadFilesize);
            }
            downloadListener.onImageDownloaded();
            byte [] downloadBytes=byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=64;
            Log.i(TAG,"downloadBytes.length-"+downloadBytes.length+"  downloadFilesize-"+downloadFilesize);
            bitmap = BitmapFactory.decodeByteArray(downloadBytes,0,downloadBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //更新UI

    private void updateImage(final ImageView imageView, final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    //更新UI
    private void updateImage(final ImageView imageView, final int resId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(resId);
            }
        });
    }

    public interface DownLoadListener {
        void onImageDownloadStart(int imageLength);

        void onImageDownloading(int downloadLength);

        void onImageDownloaded();
    }
}
