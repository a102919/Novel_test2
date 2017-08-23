package com.tsai.alan.novel_test2.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 2017/7/22.
 */

public class SqlAdapter {
     private Context context;
    private Cursor cursor;
    private MarkDBhelper dbHelper;
    private SQLiteDatabase db;
    private static final String mySQL_NAME = "MARK";
    public SqlAdapter(Context context) {
        this.context = context;
        dbHelper = new MarkDBhelper(context);
        db = dbHelper.getWritableDatabase(); // 開啟資料庫
    }
    public List<String> readMark(){
        List<String> idList = new ArrayList<>();
        String[] colNames=new String[]{"_id","novel_id"};
        cursor= db.query(mySQL_NAME, colNames,null, null, null, null,null);
        cursor.moveToFirst();  // 第1筆
        // 顯示欄位值
        for (int i = 0; i < cursor.getCount(); i++) {
            idList.add(cursor.getString(1));
            cursor.moveToNext();  // 下一筆
        }
        return idList;
    }
    public void insertMark(String id){
        ContentValues cv = new ContentValues();
        cv.put("novel_id",id);
        long new_id = db.insert(mySQL_NAME, null, cv);
    }
    public void deletMark(String id){
        int count=db.delete(mySQL_NAME,"novel_id='"+id+"'",null);
    }

    public boolean searchMark(String id){
        Cursor c=db.rawQuery("SELECT * FROM "+mySQL_NAME+" where novel_id="+id, null);    // 查詢tb_name資料表中的所有資料
        Log.i("Cursor","id= "+c.getCount());
        if(c.getCount()>0)
        return true;
        return false;
    }
    public void closeDB(){
        db.close();
    }

}
