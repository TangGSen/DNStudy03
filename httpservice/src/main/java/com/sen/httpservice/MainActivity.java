package com.sen.httpservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sen.httpservice.http.SenHttp;
import com.sen.httpservice.http.download.DownloadFileManager;
import com.sen.httpservice.http.interfaces.IDataListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "sen";
    String apiKey = "c97ef6446ed7baa5d3a9b015ed9c5108";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void getData(View view){
        for (int i=0;i<50;i++) {
                User user = new User();
                user.setKey(apiKey);
                user.setNum(10+"");
                String url = "http://api.tianapi.com/social/?key=" + apiKey+"&num=10";
               // String url = "http://api.tianapi.com/social/";
                SenHttp.sendRequest(user, url, LoginResponece.class, new IDataListener<LoginResponece>() {

                    @Override
                    public void onSuccess(LoginResponece loginResponece) {
                        if (loginResponece.getCode()==200){
                            Log.e(TAG, loginResponece.getNewslist().get(0).getTitle()+"******");
                        }else{
                            Log.e(TAG,"Code:"+loginResponece.getCode()+"msg:"+loginResponece.getMsg());
                        }

                    }

                    @Override
                    public void onFail() {
                        Log.e(TAG, "获取失败");
                    }
                });
        }
    }


    public void download(View view){
        String url = "http://172.26.5.7:8080/QQ_692.apk";
        String filePath = this.getExternalFilesDir(null).getAbsolutePath()+ File.separator+"testdownload.apk";
        new DownloadFileManager().download(url,filePath);
    }
}
