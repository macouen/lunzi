package com.oakzmm.demoapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.oakzmm.demoapp.R;
import com.oakzmm.demoapp.utils.OakLog;
import com.oakzmm.demoapp.view.WheelView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WheelViewActivity extends AppCompatActivity {

    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    private static final String[] PLANETS_TOW = new String[]{"0001", "0002", "0003", "0004", "0005", "0006", "0007", "0008"};
    @Bind(R.id.wheelView)
    WheelView wheelView;
    @Bind(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);
        ButterKnife.bind(this);
        wheelView.setOffset(2);
        wheelView.setItems(Arrays.asList(PLANETS));
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int index, String item) {
                OakLog.d("selectedIndex: " + index + ", item: " + item);
            }
        });
        wheelView.setOnItemListener(new WheelView.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                super.onItemClick(index);
                OakLog.d("onItemClick: " + index);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    wheelView.setItems(Arrays.asList(PLANETS_TOW));
            }
        });

    }
}
