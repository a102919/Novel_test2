package com.tsai.alan.novel_test2.fragment;


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

import com.tsai.alan.novel_test2.Adapter.HomeAdapter;
import com.tsai.alan.novel_test2.Broadcast.MaekBroadcastReceiver;
import com.tsai.alan.novel_test2.MainActivity;
import com.tsai.alan.novel_test2.R;
import com.tsai.alan.novel_test2.message.readMarkMessage;
import com.tsai.alan.novel_test2.model.readModel;
import com.tsai.alan.novel_test2.novelData.homeData;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkFragment extends Fragment {
private View view;
    private HomeAdapter homeAdapter;
    private RecyclerView mRecyclerView;
    private readMarkMessage readMarkMessage;
    private Intent intentt;
    public static final String BROADCAST_ACTION = "HomeFragment";
    public MarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mark, container, false);
        homeAdapter = new HomeAdapter(R.layout.home_layout,getContext());
        homeAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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
        });
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(homeAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        readMarkMessage = new readMarkMessage(getContext(),homeAdapter,getActivity());
        readMarkMessage.fetchData(readModel.initRead);

        MaekBroadcastReceiver.newInstance().registMessage(readMarkMessage);
        return view;
    }

}
