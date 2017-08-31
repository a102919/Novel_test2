package com.tsai.alan.novel_test2.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tsai.alan.novel_test2.Adapter.viewAdatpter;
import com.tsai.alan.novel_test2.Broadcast.MaekBroadcastReceiver;
import com.tsai.alan.novel_test2.Broadcast.lifeBroadcast;
import com.tsai.alan.novel_test2.R;
import com.tsai.alan.novel_test2.SQLite.SqlAdapter;
import com.tsai.alan.novel_test2.SettingReceiver;
import com.tsai.alan.novel_test2.message.downloadMessage;
import com.tsai.alan.novel_test2.novelData.homeData;
import com.tsai.alan.novel_test2.setting;

import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadFragment extends Fragment {
    private static lifeBroadcast mHomeKeyReceiver;
    private View view;
    private homeData data;
    private ViewPager pager;
    private viewAdatpter pageAdapter;
    private Toolbar toolbar;
    private AppBarLayout mAppBarLayout;
    private boolean mark = false;
    private SqlAdapter sqlAdapter;
    public static final String BROADCAST_ACTION = "MARK";
    private Context mContext;

    public ReadFragment(homeData data) {
        this.data=data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read, container, false);
        // Inflate the layout for this fragment
        mContext = getContext();

        setting.getSetting(mContext);
        sqlAdapter = new SqlAdapter(getContext());

        initSetting();

        toolbar = (Toolbar)view.findViewById(R.id.toolBar);
        toolbar.setTitle(data.getTitle());
        toolbar.setSubtitle(data.getBookmarksPage()+1+"/"+data.getPage());
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        toolbar.inflateMenu(R.menu.menu_main);
        if(sqlAdapter.searchMark(data.getId())){
            mark = true;
            toolbar.getMenu().findItem(R.id.mark_icon).setIcon(R.drawable.mark_yes_icon);
        }else {
            mark = false;
            toolbar.getMenu().findItem(R.id.mark_icon).setIcon(R.drawable.mark_no_icon);
        }
        SettingReceiver.newInstance().setSettingView(toolbar);

                //toolbar.setNavigationIcon(R.drawable.ab_android);
        //mBottomNavigationView = (BottomNavigationView)view.findViewById(R.id.bottomNavigationView);

        mAppBarLayout = (AppBarLayout)view.findViewById(R.id.appBarLayout) ;
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //Log.d("MainActivity", verticalOffset + "");
               // mBottomNavigationView.setTranslationY(-verticalOffset);
            }
        });
        pager=(ViewPager)view.findViewById(R.id.page);
        pager.removeAllViewsInLayout();
        View settingView = (View)view.findViewById(R.id.settView_id);
        pageAdapter = new viewAdatpter(getActivity().getSupportFragmentManager(),data,toolbar,settingView);
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(data.getBookmarksPage());
        //Log.i("dataa","3.pager"+data.getTitle());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                arg0++;
                toolbar.setSubtitle("\t"+arg0+"/"+data.getPage());
                data.setBookmarksPage(arg0-1);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // arg0 :当前页面，及你点击滑动的页面；arg1:当前页面偏移的百分比；arg2:当前页面偏移的像素位置

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                //arg0 ==1的时表示正在滑动，arg0==2的时表示滑动完毕了，arg0==0的时表示什么都没做。
                if(arg0 == 0){

                }else if(arg0 == 1){
                }else if(arg0 == 2){
                    data.setBookmarks(0);
                }

            }
        });
        //toolbar.setupWithViewPager();
        return view;
    }
    public static final String SETTINGRECEIVER = "SettingReceiver";
    private void initSetting() {
        SeekBar textBar = (SeekBar)view.findViewById(R.id.textBar);
        textBar.setProgress(setting.textSize-20);
        textBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setting.textSize = progress+20;

                Intent intentt = new Intent();
                intentt.putExtra("textsia",progress+20);
                intentt.setAction(setting.settingType.TEXTSIZE_ACTION.toString());
                LocalBroadcastManager.getInstance(getContext()).registerReceiver(SettingReceiver.newInstance(),new IntentFilter(setting.settingType.TEXTSIZE_ACTION.toString()));
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentt);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        CheckBox bgCheckBox = (CheckBox)view.findViewById(R.id.setBlack_id);
        if(setting.bgm.equals(setting.bgModel.blackbg)){
            bgCheckBox.setChecked(true);
        }else {
            bgCheckBox.setChecked(false);
        }
        bgCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setting.bgm = setting.bgModel.blackbg;
                }else {
                    setting.bgm = setting.bgModel.white;
                }
                Intent intentt = new Intent();
                intentt.putExtra("bg",isChecked);
                intentt.setAction(setting.settingType.Bg_ACTION.toString());
                LocalBroadcastManager.getInstance(getContext()).registerReceiver(SettingReceiver.newInstance(),new IntentFilter(setting.settingType.Bg_ACTION.toString()));
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentt);
            }
        });
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.mark_icon:
                    if(mark){
                        menuItem.setIcon(R.drawable.mark_no_icon);
                        mark = false;
                        data.setMark(mark);
                        sqlAdapter.deletMark(data.getId());

                        Intent intentt = new Intent();
                        intentt.setAction(BROADCAST_ACTION);
                        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MaekBroadcastReceiver.newInstance(),new IntentFilter(BROADCAST_ACTION));
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentt);

                    }else {
                        menuItem.setIcon(R.drawable.mark_yes_icon);
                        mark = true;
                        data.setMark(mark);
                        data.saveNovel();
                        sqlAdapter.insertMark(data.getId());
                        //Toast.makeText(view.getContext(), "新增"+new_id, Toast.LENGTH_LONG).show();
                        Intent intentt = new Intent();
                        intentt.setAction(BROADCAST_ACTION);
                        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MaekBroadcastReceiver.newInstance(),new IntentFilter(BROADCAST_ACTION));
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentt);
                    }
                    break;
                case R.id.download_icon:
                    downloadMessage downloadMessage = new downloadMessage(getActivity(),data);
                    downloadMessage.fetchData();
                    break;
            }
            if(!msg.equals("")) {
            }
            return true;
        }
    };

    //注册广播
    private static void registerHomeKeyReceiver(Context context, homeData data) {
        mHomeKeyReceiver=new lifeBroadcast(data);
        final IntentFilter homeFilter =new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    //注销广播
    private static void unregisterHomeKeyReceiver(Context context) {
        if(null!=mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Log.i("data","name: "+data.getTitle()+" "+data.getNovel().size());
        pageAdapter.saveScrollView();
        data.saveNovel();
        setting.saveSetting(mContext);
        SettingReceiver.newInstance().deleteText();
        Log.d("FRAG", "onDestroy");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FRAG", "onCreate");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("FRAG", "onStart");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("FRAG", "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        pageAdapter.saveScrollView();
        data.saveNovel();
        Log.d("FRAG", "onPause2: "+data.getBookmarks());
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("FRAG", "onStop");
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("FRAG", "onActivityCreated");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("FRAG", "onDetach");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FRAG", "onDestroyView");
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        data.takeNovel();
        Log.d("FRAG", "onAttach");
    }
}
