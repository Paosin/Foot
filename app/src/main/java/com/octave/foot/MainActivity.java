package com.octave.foot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.octave.foot.view.DrawPressurePath;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawPressurePath(this));
    }
}
