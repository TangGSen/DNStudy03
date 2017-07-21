package com.sen.imageloaderframwork.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/19.
 */

public class LoaderManager {
    Map<String, Loader> loaderMap = new HashMap<>();

    private LoaderManager() {
        register("http", new NetUrlLoader());
        register("https", new NetUrlLoader());
        register("file", new LoaclLoader());
    }

    private static LoaderManager mInstance;

    public static LoaderManager getInstance() {
        if (mInstance == null) {
            synchronized (LoaderManager.class) {
                if (mInstance == null) {
                    mInstance = new LoaderManager();
                }
            }
        }
        return mInstance;
    }

    private void register(String schema, Loader loader) {
        loaderMap.put(schema, loader);
    }

    public Loader getLoader(String schema) {
        if (loaderMap.containsKey(schema)) {
            return loaderMap.get(schema);
        } else {
            return new NullLoader();
        }

    }
}
