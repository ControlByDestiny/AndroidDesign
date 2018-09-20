package com.designpattern.ImageLoader04;



class ImageLoaderConfig {
    ImageCache mCache=new MemoryCache();
    DisplayConfig displayConfig=new DisplayConfig();
    int threadCount=Runtime.getRuntime().availableProcessors()+1;


    private ImageLoaderConfig(){}
    public static class Builder{
        private  ImageCache mCache=new MemoryCache();
        private int threadCount=Runtime.getRuntime().availableProcessors()+1;
        private DisplayConfig displayConfig=new DisplayConfig();
        //设置下载线程数
        public Builder setThreadCount(int count){
            threadCount=Math.max(1,count);
            return this;
        }
        //设置缓存策略
        public Builder setCache(ImageCache imageCache){
            mCache=imageCache;
            return this;
        }
        public Builder setLoadingFailPlaceholder(int resId){
            displayConfig.failedResId=resId;
            return this;
        }
        //设置加载进度条
        public Builder setLoadingProgressListener(ImageLoader.DownLoadListener loadingProgressListener){
            displayConfig.downLoadListener=loadingProgressListener;
            return this;
        }
        private void applyConfig(ImageLoaderConfig config){
            config.mCache=this.mCache;
            config.threadCount=this.threadCount;
            config.displayConfig=this.displayConfig;
        }
        public ImageLoaderConfig create(){
            ImageLoaderConfig config=new ImageLoaderConfig();
            applyConfig(config);
            return config;
        }
    }


}
