package com.example.administrator.dongnaosqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.dongnaosqlite.db.BaseDaoFactory;
import com.example.administrator.dongnaosqlite.db.IBaseDao;

public class MainActivity extends AppCompatActivity {
    IBaseDao<User> baseDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseDao= BaseDaoFactory.getInstance().getDataHelper(UserDao.class,User.class);
    }
    public void save(View view)
    {
        User user=new User("teacher","123456");
        baseDao.insert(user);
//
//        BaseDao<DownFile> baseDao1=BaseDaoFactory.getInstance().getDataHelper(DownDao.class,DownFile.class);
//        baseDao1.insert(new DownFile("2013.1.9","data/data/apth"));
    }
}
