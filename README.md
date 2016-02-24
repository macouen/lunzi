# lunzi
常用整理，轮子收集
---
## 内容目录介绍

###   interpolator -- 自定义插值器
> 自定义了两个动画插值器，并简单分析了函数和插值器的基本应用。

#### BezierInterpolator.java 
> 基于贝塞尔三阶曲线自定义的动画插值器，可以实现三阶曲线能够构造的函数的插值器。 在线绘制三阶曲线 [http://cubic-bezier.com/](http://cubic-bezier.com/), 根据两个控制点的坐标，定义自己需要的函数的插值器。

#### DampingInterpolator.java
> 有阻尼的简谐运动插值器。具体请参见InterpolatorActivity

#### CustomInterpolator.java
> 简单分析了基本的插值器与函数之前的关系，以及如何自定义插值器。

###   network -- 网络请求模块
> 个人作品，文章介绍：[使用okHttp、Volley、Gson快速组装HttpClient](http://oakzmm.com/2015/07/22/okHttp-Volley-Gson/)  

> 2016-02-24  更新：添加 使用json格式的post请求，使用方法见demo。  

### zxing
> 使用zxing实现的二维码扫描模块，修复了横竖屏以及预览界面图像拉伸的bug，优化了扫描界面的UI，仿微信二维码. 

> 使用注意，需要copy /values 下的ids文件，以及colors和strings文件中zxing部分的代码；添加相机权限；copy后，注意 capturActivity和viewfinderView的包名。  

###  util -- 常用工具类
#### DensityUtil.java 

> px2dp，dp2px以及获取控件宽高的工具类

#### BitmapUtil.java 

> Bitmap处理工具 1. 旋转图片 2. 压缩图片（处理大图）3. 处理成圆角图 4. 处理成圆形图

#### OakLog.java 

> 自定义Log工具，可以Debug和Release控制。

#### AnimationController.java 

> 自己看。。。

#### view -- 自定义View

#### WheelView.java
> 修改自：[WheelView](https://github.com/wangjiegulu/WheelView).  
> 优化1：index从0开始;  
> 优化2：添加了条目的点击事件;  
> 优化3：更新items数据，通过setItems;  
