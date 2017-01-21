package com.octave.foot.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public static final int MAX_ITEM_VIEW = 5;
    private View view;
    public DatashowListAdapter adapter;
    private List<Integer> listOrder;

    public DataShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_data_show, container, false);
        ButterKnife.bind(this, view);

        listOrder = initListOrder();

        initChart();
        adapter = new DatashowListAdapter(getActivity(),listOrder);
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((DefaultItemAnimator) mRv.getItemAnimator()).setSupportsChangeAnimations(false);
        mRv.setAdapter(adapter);
        Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        adapter.notifyItemChanged(0);
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //
        ItemTouchHelper.Callback callback=new ItemCallBack(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRv);

        return view;
    }

    private List<Integer> initListOrder() {
        List<Integer> a = new ArrayList<Integer>();
        for(int i=0;i<MAX_ITEM_VIEW;i++) {
            a.add(i);
        }
        return a;
    }
    private void initChart() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
