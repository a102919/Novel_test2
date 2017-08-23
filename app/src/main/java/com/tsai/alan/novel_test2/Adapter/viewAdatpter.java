package com.tsai.alan.novel_test2.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.tsai.alan.novel_test2.novelData.homeData;
import com.tsai.alan.novel_test2.fragment.novelFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alan on 2017/7/16.
 */

public class viewAdatpter extends FragmentStatePagerAdapter {
    private homeData data;
    private List<novelFragment> list = new ArrayList<>();
    private int position;
    private View settingView;
    public viewAdatpter(FragmentManager fm, homeData data, Toolbar toolbar, View settingView) {
        super(fm);
        this.data = data;
        this.settingView = settingView;
        for(int i=1;i<Integer.parseInt(data.getPage())+1;i++){
            list.add(novelFragment.newInstance(i,data,toolbar,settingView));
        }
    }

    @Override
    public Fragment getItem(int position) {
        this.position = position;
        return  list.get(position);
    }

    @Override
    public int getCount() {
        return Integer.parseInt(data.getPage());
    }
    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }

    public void saveScrollView(){
        Log.i("saveScrollView","position = "+position);

        list.get(position-1).saveScrollView();
    }

}