package com.tsai.alan.novel_test2.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tsai.alan.novel_test2.MainActivity;
import com.tsai.alan.novel_test2.R;
import com.tsai.alan.novel_test2.SettingReceiver;
import com.tsai.alan.novel_test2.fragmentGo;
import com.tsai.alan.novel_test2.novelData.homeData;
import com.tsai.alan.novel_test2.message.readNovelMesage;
import com.tsai.alan.novel_test2.setting;

import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 */
public class novelFragment extends Fragment {
private View view;
private TextView novel;
    private int position;
    private homeData data;
    private readNovelMesage readNovelMesage;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Toolbar toolbar;
    private Handler mHandler;
    private int lastScrollY;
    private View settingView;
    private LinearLayout bgLayout;

    private novelFragment(int position, homeData data, Toolbar toolbar, View settingView) {
        this.data = data;
        this.position = position;
        this.toolbar = toolbar;
        this.settingView = settingView;
    }
    public static novelFragment newInstance(int position, homeData data, Toolbar toolbar, View settingView){
        return new novelFragment(position,data,toolbar,settingView );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_novel, container, false);

        bgLayout = (LinearLayout)view.findViewById(R.id.read_bg_id);
        novel = (TextView)view.findViewById(R.id.read_id);

        initSetting();

        scrollView = (ScrollView)view.findViewById(R.id.scrollView2);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar2);


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //Log.i(TAG, "last time scrollY=>"+scrollView.getScrollY());

                }
                return false;
            }
        });
        readNovelMesage = new readNovelMesage(data,novel,progressBar,scrollView,getActivity());
        readNovelMesage.fetchData(position);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.openSet);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingView.getVisibility()==view.GONE){
                    settingView.setVisibility(view.VISIBLE);
                }else {
                    settingView.setVisibility(view.GONE);
                }
            }
        });

        mHandler = new Handler();
        return view;
    }

    private void initSetting() {
        switch(setting.bgm.getbgModel()){
            case 1:
                Drawable blackDrawable = getResources().getDrawable(R.color.black);
                bgLayout.setBackground(blackDrawable);
                novel.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                Drawable whiteDrawable = getResources().getDrawable(R.color.white);
                bgLayout.setBackground(whiteDrawable);
                novel.setTextColor(getResources().getColor(R.color.black));
        }
        novel.setTextSize(setting.textSize);
        SettingReceiver.newInstance().setSettingView(novel,bgLayout);
    }


    public void saveScrollView() {
        if(scrollView.getScrollY()!=0){
            data.setBookmarks( scrollView.getScrollY());
        }

    }


}
