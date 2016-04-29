# lunzi
常用整理，轮子收集
---
## 内容目录介绍

###   interpolator -- 自定义插值器
> 自定义了两个动画插值器，并简单分析了函数和插值器的基本应用。

```java
@Override
    public float getInterpolation(float input) {
        float t = input;
        float y;
        // y = x^2 加速插值器函数实现 ①
//        y = t*t;

        // y = -(x-1)^2+1  减速插值器函数实现  ②  通过① 上下反转，右移，上移 得到
//        y = 1 -(t-1)*(t-1);

        //  y = (1-(x2*x-1)^2)*M 过冲插值器函数实现   M 为过冲系数 x2 为y=1 是函数两个解中较大的那个。 也可使用三角函数来实现。
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
```
    
#### `BezierInterpolator.java `
> 基于贝塞尔三阶曲线自定义的动画插值器，可以实现三阶曲线能够构造的插值器函数。 
> 在线绘制三阶曲线 [http://cubic-bezier.com/](http://cubic-bezier.com/), 根据两个控制点的坐标，定义自己需要的函数的插值器。具体请参见`InterpolatorActivity`

#### `DampingInterpolator.java`
> 有阻尼的简谐运动插值器。具体请参见`InterpolatorActivity`

#### `CustomInterpolator.java`
> 简单分析了基本的插值器与函数之前的关系，以及如何自定义插值器。

###   network -- 网络请求模块
> 个人作品，文章介绍：[使用okHttp、Volley、Gson快速组装HttpClient](http://oakzmm.com/2015/07/22/okHttp-Volley-Gson/)  

> 2016-02-24  更新：添加 转换params为`json`格式的`post`请求，使用方法见demo。  
> 2016-03-29  更新：添加 直接使用`json`格式的数据请求。  
> 2016-03-29  更新：添加 使用okHttp封装文件上传的Request，并添加**progress 回调**，具体使用请见 `UploadFileRequest.java`。
> 2016-04-29  更新：优化 文件上传方法，修复 存在的可能导致**内存泄露**的问题。

### zxing -- 二维码扫描
> 使用zxing实现的二维码扫描模块，修复了横竖屏以及预览界面图像拉伸的bug，优化了扫描界面的UI，仿微信二维码. 
>
> 使用注意  
> 需要复制`/values `下的`ids`文件，以及`colors`和`strings`文件中zxing部分的代码；  
> 添加相机权限；  
> 复制后，注意 `CaptureActivity`和`ViewfinderView`的包名。    

###  util -- 常用工具类
#### `DensityUtil.java` 

> px2dp，dp2px以及获取控件宽高的工具类

#### `BitmapUtil.java `

> `Bitmap`处理工具 1. 旋转图片 2. 压缩图片（处理大图）3. 处理成圆角图 4. 处理成圆形图

#### `OakLog.java `

> 自定义Log工具，可以Debug和Release控制。

#### `AnimationController.java` 

> 自己看。。。

#### `CommonUtil.java` 

> 常用的一些方法，如验证邮箱/手机号码（正则）、检测网络类型、获取设备信息、获取APP版本号版本名称、根据包名启动应用等~

#### `Md5Util.java`

> 字符串、文件MD5加密

#### `TimeUtil.java`

> 获取常见的时间格式，具体请见。

#### `HexUtil.java`

> 16进制值与String/Byte之间的转换

#### view -- 自定义View

#### `WheelView.java`
> 修改自：[WheelView](https://github.com/wangjiegulu/WheelView).  
> 优化1：index从0开始;  
> 优化2：添加了条目的点击事件;  
> 优化3：更新items数据，通过setItems;  
