package com.example.xixihaha5;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;


public class UtilDao {
    private DatabaseUtil du;
    private SQLiteDatabase db;

    public UtilDao(Context context){
        du = new DatabaseUtil(context);
        db = du.getWritableDatabase();
    }


    /**
     * 添加数据
     * */
    public void addData(String timeid,String usetime){
        //System.out.println("insert into UserInfo values ('"+timeid+"','"+usetime+"')");
        db.execSQL("insert into UserInfo values ('"+timeid+"','"+usetime+"')");
    }

    /**
     * 删除数据
     * */
    public int delData(String where,String[] values){
        int del_data;
        del_data = db.delete("UserInfo",where,values);
        return del_data;
    }

    /**
     * 修改数据
     * */
    public void update(String[] values){
        db.execSQL("update UserInfo set userName=?,userPhone=? where userName=? ",values);
    }

    /**
     * 查询数据
     * */
    public List<recordDemo> inquireData(){
        List<recordDemo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select *" +
                " from UserInfo",null);
        while(cursor.moveToNext()){
            long useTime = cursor.getLong(1);
            String timeid = cursor.getString(0);

            recordDemo user = new recordDemo();
            user.setTimeid(timeid);
            user.setUseTime(useTime);

            list.add(user);
        }

        return list;
    }

    /**
     * 关闭数据库连接
     * */
    public void getClose(){
        if(db != null){
            db.close();
        }
    }
}
