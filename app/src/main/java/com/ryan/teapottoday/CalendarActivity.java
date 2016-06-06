package com.ryan.teapottoday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ryan.teapottoday.diyView.FixParallaxScrollView;

public class CalendarActivity extends AppCompatActivity {
    private FixParallaxScrollView svCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }
}
