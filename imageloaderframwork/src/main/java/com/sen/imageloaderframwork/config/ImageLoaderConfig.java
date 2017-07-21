package com.sen.imageloaderframwork.config;

import com.sen.imageloaderframwork.cache.BitmapCache;
import com.sen.imageloaderframwork.cache.MemoryCache;
import com.sen.imageloaderframwork.policy.LoaderPolicy;
import com.sen.imageloaderframwork.policy.ReversePolicy;

/**
 * Created by Administrator on 2017/7/18.
 * 使用建造者模式
 * 1.ImageLoaderConfig 构造方法私有化
 * 2.静态内部类builder
 * 3.静态内部类持有外部类的引用
 */

public class ImageLoaderConfig {
    /**
     * 这里先给默认的一些值，防止调用者啥都没传
     */
    //缓存策略
    private BitmapCache bitmapCache = new MemoryCache();

    //加载策略
    private LoaderPolicy loaderPolicy = new ReversePolicy();

    //默认线程数 =处理器数量
    private int defThreadCount = Runtime.getRuntime().availableProcessors();

    //显示的配置
    private DisplayConfig displayConfig = new DisplayConfig();

    private ImageLoaderConfig() {

    }

    /**
     * 建造者模式
     */
    public static class Builder {
        //1.持有外部引用
        private ImageLoaderConfig loaderConfig;

        public Builder() {
            loaderConfig = new ImageLoaderConfig();
        }

        /**
         * 2.以下是对Config 进行设置
         *
         * @param bitmapCache
         * @return
         */
        public Builder setBitmapCache(BitmapCache bitmapCache) {
            loaderConfig.bitmapCache = bitmapCache;
            return this;
        }

        public Builder setLoaderPolicy(LoaderPolicy loaderPolicy) {
            loaderConfig.loaderPolicy = loaderPolicy;
            return this;
        }

        public Builder setThreadCount(int threadCount) {
            loaderConfig.defThreadCount = threadCount;
            return this;
        }

        public Builder setLoaddingImage(int resId) {
            loaderConfig.displayConfig.loaddingIamge = resId;
            return this;
        }

        public Builder setLoadFaildmage(int resId) {
            loaderConfig.displayConfig.loadFaildImage = resId;
            return this;
        }
        //3.将外部类对象进行返回
        public ImageLoaderConfig build(){
            return loaderConfig;
        }

    }

    /**
     * 4.对外部只提供get
     * @return
     */
    public BitmapCache getBitmapCache() {
        return bitmapCache;
    }

    public LoaderPolicy getLoaderPolicy() {
        return loaderPolicy;
    }

    public int getDefThreadCount() {
        return defThreadCount;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }
}
