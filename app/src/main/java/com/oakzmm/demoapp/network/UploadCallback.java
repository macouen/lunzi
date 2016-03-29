package com.oakzmm.demoapp.network;

import com.squareup.okhttp.Callback;

/**
 * DemoApp
 * Created by OakZmm
 * on 2016/3/17
 * Description: TODO
 */
public interface UploadCallback extends Callback {

    void onProgress(long totalBytes, long remainingBytes, boolean done);

}
