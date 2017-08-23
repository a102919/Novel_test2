package com.tsai.alan.novel_test2.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsai.alan.novel_test2.Broadcast.MaekBroadcastReceiver;
import com.tsai.alan.novel_test2.MainActivity;
import com.tsai.alan.novel_test2.R;
import com.tsai.alan.novel_test2.SQLite.MarkDBhelper;
import com.tsai.alan.novel_test2.SQLite.MyDBHelper;
import com.tsai.alan.novel_test2.SQLite.SqlAdapter;
import com.tsai.alan.novel_test2.SettingReceiver;
import com.tsai.alan.novel_test2.SmartViewHolder;
import com.tsai.alan.novel_test2.fragment.MarkFragment;
import com.tsai.alan.novel_test2.novelData.homeData;

import java.util.List;

/**
 * Created by Alan on 2017/7/11.
 */

public class HomeAdapter extends BaseRecyclerAdapter {
    private  Context context;
    private boolean mark = false;
    private SqlAdapter sqlAdapter;
    public static final String BROADCAST_ACTION = "MARK";
    private List<String> markList;
    public HomeAdapter(@LayoutRes int layoutId, Context context) {
        super(layoutId);
        this.context = context;
        sqlAdapter = new SqlAdapter(context);
        markList = sqlAdapter.readMark();
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, Object model, final int position) {
        holder.text(R.id.title_id, ((homeData)getmList().get(position)).getTitle());
        holder.text(R.id.author_id, ((homeData)getmList().get(position)).getAuthor());
        holder.text(R.id.classification_id, ((homeData)getmList().get(position)).getClassification());
        holder.text(R.id.page_id,((homeData)getmList().get(position)).getPage()+"章");
        //holder.image(R.id.mark_button,R.mipmap.mark_yes);
        final View view = holder.findViewById(R.id.mark_button);
        if (view instanceof ImageView) {

            final Resources resources =context.getResources();
            Drawable btnDrawable;
            if(sqlAdapter.searchMark(((homeData)getmList().get(position)).getId())){
                mark = true;
                btnDrawable = resources.getDrawable(R.drawable.mark_yes_icon);
            }else {
                mark = false;
                btnDrawable = resources.getDrawable(R.drawable.mark_no_icon);
            }
            ((ImageView) view).setBackground(btnDrawable);
            ((ImageView) view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mark){
                        Resources resources =context.getResources();
                        Drawable btnDrawable = resources.getDrawable(R.drawable.mark_no_icon);
                        ((ImageView) view).setBackground(btnDrawable);
                        mark = false;
                        ((homeData)getmList().get(position)).setMark(mark);
                        sqlAdapter.deletMark(((homeData)getmList().get(position)).getId());

                        Intent intentt = new Intent();
                        intentt.setAction(BROADCAST_ACTION);
                        LocalBroadcastManager.getInstance(context).registerReceiver(MaekBroadcastReceiver.newInstance(),new IntentFilter(BROADCAST_ACTION));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intentt);

                        Log.i("aaa","1.aaa");

                    }else {
                        Resources resources =context.getResources();
                        Drawable btnDrawable = resources.getDrawable(R.drawable.mark_yes_icon);
                        ((ImageView) view).setBackground(btnDrawable);
                        mark = true;
                        ((homeData)getmList().get(position)).setMark(mark);

                        ((homeData)getmList().get(position)).saveNovel();

                        sqlAdapter.insertMark(((homeData)getmList().get(position)).getId());
                        //Toast.makeText(view.getContext(), "新增"+new_id, Toast.LENGTH_LONG).show();
                        Intent intentt = new Intent();
                        intentt.setAction(BROADCAST_ACTION);
                        LocalBroadcastManager.getInstance(context).registerReceiver(MaekBroadcastReceiver.newInstance(),new IntentFilter(BROADCAST_ACTION));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intentt);

                        Log.i("aaa","2.aaa");
                    }
                }
            });
        }
        SettingReceiver.newInstance().setSettingView(holder);
    }
}
