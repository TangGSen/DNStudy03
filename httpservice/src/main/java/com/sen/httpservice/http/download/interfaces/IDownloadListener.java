package com.sen.httpservice.http.download.interfaces;

import com.sen.httpservice.http.interfaces.IHttpListener;
import com.sen.httpservice.http.interfaces.IHttpService;

/**
 * Created by Administrator on 2017/7/13.
 */

public interface IDownloadListener extends IHttpListener {

    void setHttpService(IHttpService httpService);

    void setCancleable();

    void setPauseable();
}
