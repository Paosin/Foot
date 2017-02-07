package com.octave.foot;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapse_toolbar)
    CollapsingToolbarLayout collapseToolbar;

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<String> mTitles = new ArrayList<String>();
    private DataFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //显示标题,setTitle要在setSupportActionBar()之前调用或在onResume()调用
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Foot");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitles.add(getResources().getString(R.string.datashow_data_title));
        mTitles.add(getResources().getString(R.string.datashow_pic_title));
        mFragments.add(new DataShowFragment());
        mFragments.add(new PicShowFragment());

        adapter = new DataFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapter);
        mTab.setupWithViewPager(mViewPager);

        collapseToolbar.setTitleEnabled(false);
    }

    private Bitmap drawableToBitamp(int id) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
