package com.designpattern.ImageLoader03;

import android.graphics.Bitmap;

public interface ImageCache {
    Bitmap get(String key);
    void put(String key,Bitmap bitmap);

}
