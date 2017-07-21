package com.sen.dbachitetor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sen.v2.db.BaseDaoFactory;
import com.sen.v2.db.IBaseDao;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sen";
    private IBaseDao<User> baseDao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseDao = BaseDaoFactory.getInstace().getDataHelper(UserDao.class,User.class);

    }

    public void instert(View view) {
        insterData();
    }
    int i = 0;
    public void mutUser(View view) {
       User user = new User();
        i++;
        user.setName("唐家森"+i);
        user.setUserId("N000SEN"+i);
        user.setPassword("0000"+i);
        baseDao.instert(user);
    }

    public void insertUserData(View view) {
        Photo photo=new Photo();
        photo.setPath("data/data/my.jpg");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        photo.setTime(dateFormat.format(new Date()));
        PhotoDao photoDao=BaseDaoFactory.getInstace().getUserHelper(PhotoDao.class,Photo.class);
        photoDao.instert(photo);
    }



    private void insterData() {
        user = new User();

        user.setName("sen");
        user.setPassword("12345678");

//        baseDao.instert(user);


        new Thread() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (int i = 0; i <= 1000; i++) {
                    if (i > 500) {

                        user.setName("jsen");
                    }
                    user.setUserId(i+"");
                    baseDao.instert(user);
                }
                Log.e("sen", System.currentTimeMillis() - start + "ms");

            }
        }.start();
    }

    public void update(View view) {

        new Thread() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                User userWhere = new User();
                userWhere.setName("sen");

                User userUpdate = new User();
                userUpdate.setPassword("00012345678");
                baseDao.update(userUpdate,userWhere);
                Log.e("sen", System.currentTimeMillis() - start + "ms");
            }
        }.start();
    }

    public void delete(View view) {

        new Thread() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                User userWhere = new User();
                userWhere.setName("jsen");
                baseDao.delete(userWhere);
                Log.e("sen", System.currentTimeMillis() - start + "ms");
            }
        }.start();
    }

    public void query(View view) {
        queryCondition2();

    }

    private void queryCondition1() {
        new Thread() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                User userWhere = new User();
                userWhere.setName("jsen");
//                userWhere.setUserId(1000);
                List<User> list = baseDao.query(userWhere);
                Log.e("sen", System.currentTimeMillis() - start + "ms");
                Log.e(TAG, list.size() + "数据");
                for (User user : list) {
                    Log.e(TAG, user.toString());
                }
                Log.e("sen", System.currentTimeMillis() - start + "——————ms");
            }
        }.start();
    }

    private void queryCondition2() {
        new Thread() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                User userWhere = new User();
                userWhere.setName("jsen");
//                userWhere.setUserId(1000);
                List<User> list = baseDao.query(userWhere,null,760,100);
                Log.e("sen", System.currentTimeMillis() - start + "ms");
                Log.e(TAG, list.size() + "数据");
                for (User user : list) {
                    Log.e(TAG, user.toString());
                }
                Log.e("sen", System.currentTimeMillis() - start + "——————ms");
            }
        }.start();
    }
}
