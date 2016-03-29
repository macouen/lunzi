package com.oakzmm.demoapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.oakzmm.demoapp.R;
import com.oakzmm.demoapp.interpolator.BezierInterpolator;
import com.oakzmm.demoapp.interpolator.CustomInterpolator;
import com.oakzmm.demoapp.interpolator.DampingInterpolator;
import com.oakzmm.demoapp.utils.OakLog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InterpolatorActivity extends AppCompatActivity {

    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.btn_play)
    Button btnPlay;
    @Bind(R.id.iv_circle)
    ImageView ivCircle;
    private RotateAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpolator);
        ButterKnife.bind(this);
        animation = new RotateAnimation(0.0f, 135.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        animation.setDuration(1000);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OakLog.i("position:" + position);
                animation.setDuration(1000);
                switch (position) {
                    case 0:
                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                        break;
                    case 1:
                        animation.setInterpolator(new AccelerateInterpolator());
                        break;
                    case 2:
                        animation.setInterpolator(new AnticipateInterpolator());
                        break;
                    case 3:
                        animation.setInterpolator(new AnticipateOvershootInterpolator());
                        break;
                    case 4:
                        animation.setInterpolator(new BounceInterpolator());
                        break;
                    case 5:
                        animation.setInterpolator(new CycleInterpolator(1));
                        break;
                    case 6:
                        animation.setInterpolator(new LinearInterpolator());
                        break;
                    case 7:
                        animation.setInterpolator(new OvershootInterpolator());
                        break;
                    case 8:
                        animation.setDuration(2400);
                        animation.setInterpolator(new DampingInterpolator(3, 0.8f));
                        break;
                    case 9:
                        animation.setDuration(1500);
                        animation.setInterpolator(new BezierInterpolator(.53f, -0.52f, .75f, -0.51f));
//                        animation.setInterpolator( new BezierInterpolator(0.0f, 1.56f,1.0f, -0.67f));
                        break;
                    case 10:
                        animation.setInterpolator(new CustomInterpolator());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void playAnimation(View view) {
        OakLog.i("playAnimation");
        ivCircle.clearAnimation();
        ivCircle.startAnimation(animation);
    }
}
