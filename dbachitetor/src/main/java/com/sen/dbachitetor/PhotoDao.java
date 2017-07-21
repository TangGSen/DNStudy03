package com.sen.dbachitetor;


import com.sen.v2.db.BaseDao;

/**
 * Created by david on 20/1/2017.
 */

public class PhotoDao  extends BaseDao<Photo> {



    @Override
    public String createTableSQL() {
        return "create table if not exists tb_photo(\n" +
                "                time TEXT,\n" +
                "                path TEXT,\n" +
                "                to_user TEXT\n" +
                "                )";
    }


}
