package com.oakzmm.demoapp.interpolator;

import android.view.animation.Interpolator;

/**
 * DemoApp
 * Created by acer_april
 * on 2016/2/23
 * Description:   可以在 http://fooplot.com/ 输入相应的函数查看图形
 */
public class CustomInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        float t = input;
        float y;
        // y = x^2 加速 ①
//        y = t*t;

        // y = -(x-1)^2+1  减速  ②  通过① 上下反转，右移，上移 得到
//        y = 1 -(t-1)*(t-1);

        //  y = (1-(x2*x-1)^2)*M 过冲函数   M 为过冲系数 x2 为y=1 是函数两个解中较大的那个。 或者使用三角函数
        y = (1 - (1.4f * t - 1) * (1.4f * t - 1)) * 1.2f;

        // y = x*x*(3*x-2)  向后函数  三次函数

        // 先加速后减速 循环 都是使用三角函数

        // 反弹  分段函数

        // 非周期性的函数均可以使用三阶贝塞尔曲线，调整控制点的位置来实现。包括上面的加速、减速、过冲、向后，先加速后减速；
        // 周期性的函数需要使用三角函数（周期不变）或者傅立叶变换（周期有变化）来实现。上面的循环和先加速再减速；
        return y;
    }
}
