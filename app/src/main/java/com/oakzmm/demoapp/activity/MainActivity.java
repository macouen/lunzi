package com.oakzmm.demoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.oakzmm.demoapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.btn_wheelView)
    Button btnWheelView;
    @Bind(R.id.btn_Interpolator)
    Button btnInterpolator;
    @Bind(R.id.btn_QR_code)
    Button btnQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnWheelView.setOnClickListener(this);
        btnInterpolator.setOnClickListener(this);
        btnQRCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wheelView:
                startActivity(new Intent(MainActivity.this, WheelViewActivity.class));
                break;
            case R.id.btn_Interpolator:
                startActivity(new Intent(MainActivity.this, InterpolatorActivity.class));
                break;
            case R.id.btn_QR_code:
                startActivity(new Intent(MainActivity.this, CaptureActivity.class));
                break;
            default:
                break;
        }
    }
}
