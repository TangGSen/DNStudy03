package com.sen.dbachitetor;


import com.sen.v2.db.annotation.DbTable;

/**
 * Created by david on 20/1/2017.
 */
@DbTable("tb_photo")
public class Photo {
    public String time;

    public String path;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
