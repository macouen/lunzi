package com.oakzmm.demoapp.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * DemoApp
 * Created by acer_april
 * on 2015/7/20
 * Description: httpRequest
 */
public class HttpClientRequest {

    private static Context sContext;
    public RequestQueue mRequestQueue;

    private HttpClientRequest() {
        mRequestQueue = getRequestQueue();
    }

    public static HttpClientRequest getInstance(Context context) {
        sContext = context.getApplicationContext();
        return ClientHolder.CLIENT_REQUEST;
    }
    private static class ClientHolder {
        private static final HttpClientRequest CLIENT_REQUEST = new HttpClientRequest();
    }
    /**
     * Cancels all the request in the Volley queue for a given tag
     *
     * @param tag associated with the Volley requests to be cancelled
     */
    public void cancelAllRequests(String tag) {
        if (getRequestQueue() != null) {
            getRequestQueue().cancelAll(tag);
        }
    }

    /**
     * Returns a Volley request queue for creating network requests
     *
     * @return {@link RequestQueue}
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            // use  custom okhttpStack, make better work .
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext(),
                    new OkHttpStack());
        }
        return mRequestQueue;
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request is the request to add to the Volley queue
     */
    public <T> void addRequest(Request<T> request) {
        getRequestQueue().add(request);
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request is the request to add to the Volley queuest
     * @param tag     is the tag identifying the request
     */
    public <T> void addRequest(Request<T> request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    /**
     * 使用和参数配置范例
     *
     * @param param1
     * @param param2
     * @param listener
     * @param errorListener
     */
    public void getDemoData(String param1,
                            String param2,
                            Response.Listener listener,
                            Response.ErrorListener errorListener, String tag) {
        Map<String, String> params = new HashMap<>();
        params.put("param1", param1);
        params.put("param2", param2);

        CustomRequest request = new CustomRequest.RequestBuilder()
                .post()//不设置的话默认GET 但是设置了参数就不需要了。。。
                .url("")//url会统一配置到requestUrl类中 必填
                        // 添加参数方法1 适用参数比较多的情况下
//               .params(params)
                        // 添加参数方法2
                .addParams("param1", param1)//添加参数1
                .addParams("param2", param2)//添加参数2
//               .clazz(Test.calss) //如果设置了返回类型，会自动解析返回model(Gson解析) 如果不设置会直接返回json数据;
                .successListener(listener)//获取数据成功的listener
                .errorListener(errorListener)//获取数据异常的listener
//              .toJSON() //将add的params 转成json数据请求
//                .JSONString(json) // 直接添加jsonString 为 requestBody，！！！不能再同时使用addParams/params的方法
                .build();
        addRequest(request, tag);
        //将请求add到队列中。并设置tag  并需要相应activity onStop方法中调用cancel方法
    }
    //Demo
    public void getWeatherData(Response.Listener listener,
                               Response.ErrorListener errorListener, String tag) {
        CustomRequest request = new CustomRequest.RequestBuilder()
                .url(RequestConstant.TEST_URL)
//                .clazz(Weather.class)
                .successListener(listener)
                .errorListener(errorListener)
                .build();
        addRequest(request, tag);//将请求add到队列中。
    }


}
