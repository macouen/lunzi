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
        // y = x^2 加速插值器函数实现 ①
//        y = t*t;

        // y = -(x-1)^2+1  减速插值器函数实现  ②  通过① 上下反转，右移，上移 得到
//        y = 1 -(t-1)*(t-1);

        //  y = (1-(x2*x-1)^2)*M 过冲插值器函数实现   M 为过冲系数 x2 为y=1 是函数两个解中较大的那个。 也可使用三角函数来实现
        y = (1 - (1.4f * t - 1) * (1.4f * t - 1)) * 1.2f;

        // y = x*x*(3*x-2)  向后插值器函数实现，为三次函数。

        // 先加速后减速插值器和循环插值器都是使用三角函数来实现的。

        // 反弹插值器是使用分段函数实现。

        // 总结：
        // 简单的插值器和可以使用二次或者三次函数通过变换得到想要的插值器函数；
        // 非周期性的插值器函数均可以使用三阶贝塞尔曲线，调整控制点的位置来实现。包括上面的加速、减速、过冲、向后，先加速后减速；
        // 周期性的插值器函数需要使用三角函数（周期不变）或者傅立叶变换（周期有变化）来实现。上面的循环和先加速再减速；

        return y;
    }
}
