package com.sen.httpservice.http.download.db;

import android.database.Cursor;

import com.sen.httpservice.http.download.enums.DownloadStatus;
import com.sen.httpservice.http.download.mode.DownloadInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/15.
 */

public class DownloadDao extends BaseDao<DownloadInfo>{
    /**
     * 保存应该要下载的文件集合
     * 不包括下成功的
     * @return
     */

    private List<DownloadInfo> mDownloadList = Collections.synchronizedList(new ArrayList<DownloadInfo>());


    @Override
    public String createTableSQL() {
        return "create table if not exists  t_downloadInfo(" + "id Integer primary key, " + "url TEXT not null," + "filePath TEXT not null, " + "displayName TEXT, " + "status Integer, " + "totalLen Long, " + "currentLen Long," + "startTime TEXT," + "finishTime TEXT," + "userId TEXT, " + "httpTaskType TEXT," + "priority  Integer," + "stopMode Integer," + "downloadMaxSizeKey TEXT," + "unique(filePath))";

    }

    public DownloadInfo findRecord(String url, String filePath) {
        synchronized (DownloadDao.class) {
            //1.在内存中查找
            for (DownloadInfo downloadInfo : mDownloadList) {
                if (url.equals(downloadInfo.getUrl()) && filePath.equals(downloadInfo.getFilePath()))
                    return downloadInfo;
            }

            //2.内存没有，从数据库中查
            DownloadInfo where = new DownloadInfo();
            where.setUrl(url);
            where.setFilePath(filePath);
            List<DownloadInfo> finds = query(where);
            if (finds.size() > 0) {
                return finds.get(0);
            }
            return null;
        }
    }

    //根据文件路劲
    public List<DownloadInfo> findRecord(String filePath) {
        synchronized (DownloadDao.class) {
            DownloadInfo where = new DownloadInfo();
            where.setFilePath(filePath);
            List<DownloadInfo> finds = query(where);
            return finds;
        }
    }
    //添加一条记录
    public DownloadInfo addRecord(String url, String filePath, String disPlayName, int prority) {
        synchronized (DownloadDao.class){
            DownloadInfo record = findRecord(url ,filePath);
            if (record ==null){
                record = new DownloadInfo();
                record.setUrl(url);
                record.setFilePath(filePath);
                record.setDisplayName(disPlayName);
                record.setPriority(prority);
                record.setStatus(DownloadStatus.waiting.getValue());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                record.setStartTime(dateFormat.format(new Date()));
                record.setFinishTime("0");
                record.setId(generateRecordId());
                record.setTotalLength(0L);
                record.setCurrentLength(0L);
                instert(record);
                mDownloadList.add(record);
                return record;
            }
        }
        return null;
    }

    /**
     * 更新下载记录,数据库和内存
     * @param downloadInfo
     */
    public int updateRecord(DownloadInfo downloadInfo) {
        DownloadInfo where = new DownloadInfo();
        where.setId(where.getId());
        synchronized (DownloadDao.class){
            int result = -1;
            try {
                result = update(downloadInfo, where);
            }catch (Exception e){

            }
            if (result >0){
                int size = mDownloadList.size();
                for (int i= 0;i<size;i++){
                    if (mDownloadList.get(i).getId() == downloadInfo.getId()){
                        mDownloadList.set(i,downloadInfo);
                        break;
                    }
                }
            }
            return result;
        }

    }
    //从内存中移除下载记录
    public boolean removeRecordMenory(int downloadId) {
        synchronized (DownloadDao.class){
            int size = mDownloadList.size();
            for (int i= 0;i<size ;i++){
                if (mDownloadList.get(i).getId() ==downloadId){
                    mDownloadList.remove(i);
                    return true;
                }
            }
            return false;
        }

    }

    public DownloadInfo findSingleRecord(String filePath) {
        List<DownloadInfo> downloadInfoList = findRecord(filePath);
        if (downloadInfoList.isEmpty())
            return null;

        return downloadInfoList.get(0);
    }

    /**
     * 根据id查找下载记录对象
     *
     * @param recordId
     * @return
     */
    public DownloadInfo findRecordById(int recordId)
    {
        synchronized (DownloadDao.class)
        {
            for (DownloadInfo record :mDownloadList)
            {
                if (record.getId() == recordId)
                {
                    return record;
                }
            }

            DownloadInfo where = new DownloadInfo();
            where.setId(recordId);
            List<DownloadInfo> resultList = super.query(where);
            if (resultList.size() > 0)
            {
                return resultList.get(0);
            }
            return null;
        }

    }

    /**
     * 比较器，下载时使用id 来按顺序下载
     */

    class DownloadInfoComparator implements Comparator<DownloadInfo>{

        @Override
        public int compare(DownloadInfo o1, DownloadInfo o2) {
            return o1.getId()-o2.getId();
        }
    }

    /**
     * id 作为数据库的可以，我们要自定义增长的策略，不能自动增长
     */
    private Integer generateRecordId(){
        int maxId = 0;
        String sql = "select max(id) from "+getTableName();
        synchronized (DownloadDao.class){
            Cursor cursor = this.mSQlDb.rawQuery(sql,null);
            if (cursor.moveToNext()){
                int index = cursor.getColumnIndex("max(id)");
                if (index != -1){
                    Object value = cursor.getInt(index);
                    if (value!=null){
                        maxId = Integer.parseInt(String.valueOf(value));
                    }
                }
            }
        }
        return maxId+1;
    }
}
