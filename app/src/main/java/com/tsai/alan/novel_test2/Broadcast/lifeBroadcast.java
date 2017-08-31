package com.tsai.alan.novel_test2.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tsai.alan.novel_test2.novelData.homeData;

import static android.content.ContentValues.TAG;

/**
 * Created by Alan on 2017/8/24.
 */

public class lifeBroadcast extends BroadcastReceiver {
    private static final String SYSTEM_DIALOG_REASON_KEY="reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS="recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY="homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK="lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST="assist";
    private homeData data;
    public lifeBroadcast(homeData data) {
        this.data = data;
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if(SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                //短按Home键
                data.saveNovel();
                Log.d(TAG,"短按Home键: "+data.getBookmarks());
            }else if(SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                //长按Home键 或者activity切换键
                data.saveNovel();
                Log.d(TAG,"长按Home键 或者activity切换键");
            }else if(SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                //锁屏
                data.saveNovel();
                Log.d(TAG,"锁屏");
            }else if(SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                //长按Home键
                data.saveNovel();
                Log.d(TAG,"长按Home键");
            }
        }
    }
}
