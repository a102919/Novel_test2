package com.tsai.alan.novel_test2.message;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tsai.alan.novel_test2.Adapter.HomeAdapter;
import com.tsai.alan.novel_test2.SQLite.MarkDBhelper;
import com.tsai.alan.novel_test2.SQLite.SqlAdapter;
import com.tsai.alan.novel_test2.model.readModel;
import com.tsai.alan.novel_test2.novelData.homeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alan on 2017/7/22.
 */

public class readMarkMessage implements MakeData{
    private Context context;
    private HomeAdapter homeAdapter;
    private Activity activity;
    public readMarkMessage(Context context, HomeAdapter homeAdapter, Activity activity) {
        this.context = context;
        this.homeAdapter = homeAdapter;
        this.activity = activity;
    }
    public void fetchData(final readModel model ){
        SqlAdapter sqlAdapter = new SqlAdapter(context);
        final List<homeData> datas =new ArrayList<>();
        List<String> idLis = sqlAdapter.readMark();
        for (String id:idLis){
            homeData data = new homeData(context);
            data.takeNovel(id);
            datas.add(data);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //progressBar.setVisibility(View.VISIBLE);
                //更新列表
                parseDataFinish(datas,model);
            }
        });
    }
    private void parseDataFinish(List<homeData> data,readModel model ) {
        if (readModel.initRead.equals(model)) {
            homeAdapter.refresh(data);
        } else {
            homeAdapter.loadmore(data);
        }
            //progressBar.setVisibility(View.GONE);
    }

}
