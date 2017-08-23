package com.tsai.alan.novel_test2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import com.tsai.alan.novel_test2.Adapter.HomePagerAdapter;
import com.tsai.alan.novel_test2.fragment.BasicFragment;
import com.tsai.alan.novel_test2.fragment.HomeFragment;
import com.tsai.alan.novel_test2.fragment.MarkFragment;
import com.tsai.alan.novel_test2.fragment.ReadFragment;
import com.tsai.alan.novel_test2.fragment.SetFragment;
import com.tsai.alan.novel_test2.novelData.homeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements fragmentGo {
    private Context mContext;
    private FragmentManager  fragmentMgr;
    private BasicFragment basicFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        MyBroadcastReceiver.newInstance().setmContext(mContext,this);
        basicFragment = new BasicFragment();

        fragmentMgr = getSupportFragmentManager();
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        fragmentTrans.addToBackStack(basicFragment.getClass().getName());
        fragmentTrans.add(R.id.fragment, basicFragment, "HomeFragment");
        fragmentTrans.commit();
    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTrans = fragmentMgr.beginTransaction();
        fragmentTrans.addToBackStack(fragment.getClass().getName());
        fragmentTrans.hide(basicFragment);
        fragmentTrans.add(R.id.fragment , fragment, "ReadFragment");
        //fragmentTrans.replace(R.id.fragment, fragment, "My fragment A");
        fragmentTrans.commit();
    }

    // 繼承自BroadcastReceiver的廣播接收元件
    public static class MyBroadcastReceiver extends BroadcastReceiver {
        private static MyBroadcastReceiver myBroadcastReceiver = null;
        private Stack fragmmentStack;
        private MyBroadcastReceiver(){}
        public static MyBroadcastReceiver newInstance(){
            if(null == myBroadcastReceiver){
                myBroadcastReceiver = new MyBroadcastReceiver();
            }
            return myBroadcastReceiver;
        }
        // 接收廣播後執行這個方法
        // 第一個參數Context物件，用來顯示訊息框、啟動服務
        // 第二個參數是發出廣播事件的Intent物件，可以包含資料
        private Context mContext;
        private fragmentGo fragmentgo;
        public static final String BROADCAST_ACTION = "HomeFragment";
        public void setmContext(Context mContext,fragmentGo fragmentgo) {
            this.mContext = mContext;
            this.fragmentgo=fragmentgo;
            //fragmmentStack.push()
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // 讀取包含在Intent物件中的資料
            String name = intent.getStringExtra("name");
            int age = intent.getIntExtra("age", -1);
            // 因為這不是Activity元件，需要使用Context物件的時候，
            // 不可以使用「this」，要使用參數提供的Context物件
            if(intent.getAction().equals(BROADCAST_ACTION)){
                //Toast.makeText(context,Integer.toString(age), Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(mContext).unregisterReceiver(MyBroadcastReceiver.newInstance());
                homeData data =(homeData)intent.getSerializableExtra("novelData");
                ReadFragment readFragment = new ReadFragment(data);
                fragmentgo.changeFragment(readFragment);
            }

        }
    }
    @Override
    public void onBackPressed() {
        Log.i("ccc","1");
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
