package com.tsai.alan.novel_test2.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tsai.alan.novel_test2.Adapter.HomePagerAdapter;
import com.tsai.alan.novel_test2.HomeFragmentInfo;
import com.tsai.alan.novel_test2.R;
import com.tsai.alan.novel_test2.SQLite.MyDBHelper;
import com.tsai.alan.novel_test2.SettingReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view;
    public BasicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_basic, container, false);
        initTablayout(view);
        return view;
    }
    private List<HomeFragmentInfo> initFragments(){
        List<HomeFragmentInfo> mFragments = new ArrayList<>(3);
        mFragments.add(new HomeFragmentInfo("首頁",HomeFragment.class));
        mFragments.add(new HomeFragmentInfo ("書籤", MarkFragment.class));
        mFragments.add(new HomeFragmentInfo ("設定", SetFragment.class));
        return  mFragments;
    }
    private void initTablayout(View view) {
        tabLayout = (TabLayout)view.findViewById(R.id.tablayout);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        PagerAdapter adapter = new
                HomePagerAdapter(getActivity().getSupportFragmentManager(),initFragments());
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setAdapter(adapter);
        //viewPager.notifySubtreeAccessibilityStateChanged();
        // Tablayout 关联 viewPager
        tabLayout.setupWithViewPager(viewPager);
        SettingReceiver.newInstance().setSettingView(tabLayout);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("string","anAngryAnt");
        super.onSaveInstanceState(outState);
    }
}
