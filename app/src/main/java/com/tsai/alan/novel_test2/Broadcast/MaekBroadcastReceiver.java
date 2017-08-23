package com.tsai.alan.novel_test2.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tsai.alan.novel_test2.message.MakeData;
import com.tsai.alan.novel_test2.message.readMarkMessage;
import com.tsai.alan.novel_test2.model.readModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Alan on 2017/7/23.
 */

public class MaekBroadcastReceiver  extends BroadcastReceiver {
    private static MaekBroadcastReceiver myBroadcastReceiver = null;
    private MaekBroadcastReceiver(){}
    public static MaekBroadcastReceiver newInstance(){
        if(null == myBroadcastReceiver){
            myBroadcastReceiver = new MaekBroadcastReceiver();
        }
        return myBroadcastReceiver;
    }
    // 接收廣播後執行這個方法
    // 第一個參數Context物件，用來顯示訊息框、啟動服務
    // 第二個參數是發出廣播事件的Intent物件，可以包含資料
    private List<MakeData> ListMessage = new ArrayList<>();
    public static final String BROADCAST_ACTION = "MARK";
    public void registMessage(MakeData Message) {
        this.ListMessage.add(Message);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 讀取包含在Intent物件中的資料
        // 因為這不是Activity元件，需要使用Context物件的時候，
        // 不可以使用「this」，要使用參數提供的Context物件
        if(intent.getAction().equals(BROADCAST_ACTION)){
            Log.i("ListMessage","ListMessage = "+ListMessage.size());
            for (MakeData Message:ListMessage)
            Message.fetchData(readModel.initRead);
        }

    }
}