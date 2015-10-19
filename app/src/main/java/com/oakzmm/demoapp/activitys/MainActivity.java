package com.oakzmm.demoapp.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.oakzmm.demoapp.R;
import com.oakzmm.demoapp.view.WheelView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    @Bind(R.id.wheelView)
    WheelView wheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        wheelView.setOffset(2);
        wheelView.setItems(Arrays.asList(PLANETS));
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int index, String item) {
                Log.d("MainActivity", "selectedIndex: " + index + ", item: " + item);
            }
        });
        wheelView.setOnItemListener(new WheelView.OnItemClickListener(){
            @Override
            public void onItemClick(int index) {
                super.onItemClick(index);
                Log.d("MainActivity", "onItemClick: " + index );
            }
        });
    }

}
