package com.sen.v2.db.emuns;

import android.os.Environment;

import com.sen.dbachitetor.User;
import com.sen.dbachitetor.UserDao;
import com.sen.v2.db.BaseDaoFactory;

import java.io.File;

/**
 * Created by Administrator on 2017/7/17.
 */

public enum PrivateDataBaseEnmus {
    database("local/data/database");
    private String value;

    PrivateDataBaseEnmus(String s) {
        this.value = s;
    }

    public String getValue() {
        UserDao userDao = BaseDaoFactory.getInstace().getDataHelper(UserDao.class, User.class);
        if (userDao != null) {
            User currentUser = userDao.getCurrentUser();
            if (currentUser!=null){
                File file = new File(Environment.getExternalStorageDirectory(),"update"+File.separator+currentUser.getUserId());
                if (!file.exists())
                    file.mkdir();

                return file.getAbsolutePath()+File.separator+"login.db";
            }
        }

        return value;
    }

}
