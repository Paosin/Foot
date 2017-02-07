package com.octave.foot.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.octave.foot.R;
import com.octave.foot.adapter.DatashowListAdapter;
import com.octave.foot.adapter.ItemCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataShowFragment extends Fragment {

    @Bind(R.id.rv_datashow)
    RecyclerView mRv;

    public static Handler handler;
    private View view;
    public DatashowListAdapter adapter;

    public DataShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_data_show, container, false);
        ButterKnife.bind(this, view);

        initChart();
        adapter = new DatashowListAdapter(getActivity(),5);
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ((DefaultItemAnimator) mRv.getItemAnimator()).setSupportsChangeAnimations(false);
        mRv.setAdapter(adapter);


        //
        ItemTouchHelper.Callback callback=new ItemCallBack(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRv);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                adapter.notifyItemChanged(0,123);
            }
        };
//        adapter.setRecyclerViewOnItemClickListener(new DatashowListAdapter.RecyclerViewOnItemClickListener() {
//            @Override
//            public void onItemClickListener(View view, int position) {
//                Snackbar.make(view,"点击事件",Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        adapter.setOnItemLongClickListener(new DatashowListAdapter.RecyclerViewOnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClickListener(View view, int position) {
//
//                return true;
//            }
//        });
        return view;
    }

    private void initChart() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
