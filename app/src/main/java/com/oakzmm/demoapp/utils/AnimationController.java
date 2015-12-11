package com.oakzmm.demoapp.utils;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class AnimationController {
    public static final int rela1 = Animation.RELATIVE_TO_SELF;
    public static final int rela2 = Animation.RELATIVE_TO_PARENT;

    public static final int Default = -1;
    public static final int Linear = 0;
    public static final int Accelerate = 1;
    public static final int Decelerate = 2;
    public static final int AccelerateDecelerate = 3;
    public static final int Bounce = 4;
    public static final int Overshoot = 5;
    public static final int Anticipate = 6;
    public static final int AnticipateOvershoot = 7;

    private static void setEffect(Animation animation, int interpolatorType, long durationMillis, long delayMillis) {
        switch (interpolatorType) {
            case 0:
                animation.setInterpolator(new LinearInterpolator());
                break;
            case 1:
                animation.setInterpolator(new AccelerateInterpolator());
                break;
            case 2:
                animation.setInterpolator(new DecelerateInterpolator());
                break;
            case 3:
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case 4:
                animation.setInterpolator(new BounceInterpolator());
                break;
            case 5:
                animation.setInterpolator(new OvershootInterpolator());
                break;
            case 6:
                animation.setInterpolator(new AnticipateInterpolator());
                break;
            case 7:
                animation.setInterpolator(new AnticipateOvershootInterpolator());
                break;
            default:
                break;
        }
        animation.setDuration(durationMillis);
        animation.setStartOffset(delayMillis);
    }

    private static void baseIn(View view, Animation animation, long durationMillis, long delayMillis) {
        setEffect(animation, Default, durationMillis, delayMillis);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animation);
    }

    private static void baseOut(View view, Animation animation, long durationMillis, long delayMillis) {
        setEffect(animation, Default, durationMillis, delayMillis);
        animation.setAnimationListener(new MyAnimationListener(view));
        view.startAnimation(animation);
    }

    public static void fadeIn(View view, long durationMillis, long delayMillis) {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void fadeOut(View view, long durationMillis, long delayMillis) {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public static void slideIn(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0, rela2, 0);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void viticalIn(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 1, rela2, 0);
        animation.setFillAfter(true);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void viticalOut(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, 0, rela2, 0, rela2, 1);
        animation.setFillAfter(true);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void slideOut(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, -1, rela2, 0, rela2, 0);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public static void scaleIn(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, rela2, 0.5f, rela2, 0.5f);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void scaleOut(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 0, rela2, 0.5f, rela2, 0.5f);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public static void rotateIn(View view, long durationMillis, long delayMillis) {
        RotateAnimation animation = new RotateAnimation(-90, 0, rela1, 0, rela1, 1);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void rotateOut(View view, long durationMillis, long delayMillis) {
        RotateAnimation animation = new RotateAnimation(0, 90, rela1, 0, rela1, 1);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public static void scaleRotateIn(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation1 = new ScaleAnimation(0, 1, 0, 1, rela1, 0.5f, rela1, 0.5f);
        RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f, rela1, 0.5f);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void scaleRotateOut(View view, long durationMillis, long delayMillis) {
        ScaleAnimation animation1 = new ScaleAnimation(1, 0, 1, 0, rela1, 0.5f, rela1, 0.5f);
        RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f, rela1, 0.5f);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public static void slideFadeIn(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation1 = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0, rela2, 0);
        AlphaAnimation animation2 = new AlphaAnimation(0, 1);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public static void slideFadeOut(View view, long durationMillis, long delayMillis) {
        TranslateAnimation animation1 = new TranslateAnimation(rela2, 0, rela2, -1, rela2, 0, rela2, 0);
        AlphaAnimation animation2 = new AlphaAnimation(1, 0);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        baseOut(view, animation, durationMillis, delayMillis);
    }

    public void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void hide(View view) {
        view.setVisibility(View.GONE);
    }

    public void transparent(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    private static class MyAnimationListener implements AnimationListener {
        private View view;

        public MyAnimationListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            this.view.setVisibility(View.INVISIBLE);
//			this.view.setVisibility(View.GONE);
//			ViewGroup parent=(ViewGroup)this.view.getParent();
//			parent.removeView(view);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }
}
