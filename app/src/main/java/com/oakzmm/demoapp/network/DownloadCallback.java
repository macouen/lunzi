package com.oakzmm.demoapp.network;

import com.squareup.okhttp.Callback;

/**
 * PatrolDev
 * Created by acer_april
 * on 2016/3/17
 * Description: TODO
 */
public interface DownloadCallback extends Callback {

    void onProgress(long totalBytes, long currentBytes, boolean done);

}
