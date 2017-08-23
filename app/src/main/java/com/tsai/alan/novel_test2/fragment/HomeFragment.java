package com.tsai.alan.novel_test2.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scwang.smartrefresh.header.FunGameBattleCityHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tsai.alan.novel_test2.Adapter.HomeAdapter;
import com.tsai.alan.novel_test2.Broadcast.MaekBroadcastReceiver;
import com.tsai.alan.novel_test2.MainActivity;
import com.tsai.alan.novel_test2.R;
import com.tsai.alan.novel_test2.SettingReceiver;
import com.tsai.alan.novel_test2.model.readModel;
import com.tsai.alan.novel_test2.novelData.homeData;
import com.tsai.alan.novel_test2.message.readHomeMesage;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{
    private HomeAdapter homeAdapter;
    private RecyclerView mRecyclerView;
    private readHomeMesage readHomeMesage;
    private ProgressBar progressBar;
    private View view;
    private Intent intentt;
    public static final String BROADCAST_ACTION = "HomeFragment";
    private ImageButton markbutton;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return view;
    }
    private void initView() {

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        homeAdapter = new HomeAdapter(R.layout.home_layout,getContext());
        homeAdapter.setOnItemClickListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(homeAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        SmartRefreshLayout refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ((View) refreshlayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readHomeMesage.fetchData(readModel.initRead);
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });
        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ((View) refreshlayout).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readHomeMesage.fetchData(readModel.secondRead);
                        refreshlayout.finishLoadmore();
                        if (readHomeMesage.getListSize() == 0) {
                            Toast.makeText(getActivity().getApplication(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshlayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
                        }
                    }
                }, 2000);
            }
        });

        SettingReceiver.newInstance().setSettingView(refreshLayout);

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new FunGameBattleCityHeader(context);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
//设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context);//指定为经典Footer，默认是 BallPulseFooter
            }
        });

        readHomeMesage = new readHomeMesage(homeAdapter,progressBar,getActivity());

        MaekBroadcastReceiver.newInstance().registMessage(readHomeMesage);



    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        homeData data =(homeData)homeAdapter.getItem(position);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        bundle.putSerializable("novelData",data);//傳遞Double

//將Bundle物件傳給intent
        intentt = new Intent();
        intentt.putExtras(bundle);
        intentt.setAction(BROADCAST_ACTION);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(MainActivity.MyBroadcastReceiver.newInstance(),new IntentFilter(BROADCAST_ACTION));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentt);
    }
}
