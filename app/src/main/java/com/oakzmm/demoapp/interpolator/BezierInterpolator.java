package com.oakzmm.demoapp.interpolator;

import android.graphics.PointF;
import android.view.animation.Interpolator;

/**
 * DemoApp
 * Created by acer_april
 * on 2016/2/22
 * Description: 缓动三次方曲线插值器.(基于三次方贝塞尔曲线)
 */
public class BezierInterpolator implements Interpolator {


    private final PointF mControlPoint1 = new PointF();
    private final PointF mControlPoint2 = new PointF();


    /**
     * 设置中间两个控制点.
     * 在线工具: http://cubic-bezier.com
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */

    public BezierInterpolator(float x1, float y1, float x2, float y2) {
        mControlPoint1.x = x1;
        mControlPoint1.y = y1;
        mControlPoint2.x = x2;
        mControlPoint2.y = y2;
    }

    /**
     * 根据三阶贝塞尔曲线的多项式公式，求三次贝塞尔曲线(四个控制点)，一个点某个维度的值.
     *
     * @param t      取值[0, 1]
     * @param value0
     * @param value1
     * @param value2
     * @param value3
     * @return
     */

    public static double cubicCurves(double t, double value0, double value1, double value2, double value3) {

        double value;

        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        value = uuu * value0;
        value += 3 * uu * t * value1;
        value += 3 * u * tt * value2;
        value += ttt * value3;

        return value;

    }

    @Override

    public float getInterpolation(float input) {
        float t = input;
        //计算 在t时间下  曲线在y上的值
        double value = cubicCurves(t, 0, mControlPoint1.y, mControlPoint2.y, 1);
        if (value > 0.99d) {
            value = 1;
        }
        return (float) value;
    }
}
