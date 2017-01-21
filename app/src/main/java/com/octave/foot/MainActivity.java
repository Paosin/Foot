package com.octave.foot;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.octave.foot.adapter.DataFragmentAdapter;
import com.octave.foot.fragments.DataShowFragment;
import com.octave.foot.fragments.PicShowFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tabs)
    TabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<String> mTitles = new ArrayList<String>();
    private DataFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        getSupportActionBar().hide();
        mTitles.add(getResources().getString(R.string.datashow_data_title));
        mTitles.add(getResources().getString(R.string.datashow_pic_title));
        mFragments.add(new DataShowFragment());
        mFragments.add(new PicShowFragment());

        adapter = new DataFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapter);
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
