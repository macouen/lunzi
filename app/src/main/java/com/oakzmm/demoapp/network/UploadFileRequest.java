package com.oakzmm.demoapp.network;


import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 * PatrolDev
 * Created by acer_april
 * on 2016/3/14
 * Description:
 * <p/>
 * 用法同HttpClientRequest，
 * 注意
 * 1.callback 的回调非UI线程；
 * 2.需要自己解析Response数据，暂不支持解析为JavaBean，解析方法  new Gson().fromJson( response.body().string(),Bean.class);
 */
public class UploadFileRequest {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static Context mContext;
    public OkHttpClient mOkHttpClient;
//    public Call call;
//    private String url = RequestConstant.UPLOAD_FILE;

    private UploadFileRequest() {
        mOkHttpClient = new OkHttpClient();
    }

    public static UploadFileRequest getInstance(Context context) {
        // it keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        mContext = context.getApplicationContext();
        return ClientHolder.CLIENT_REQUEST;
    }

    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final UploadCallback listener) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    /**
     * 上传单个文件
     *
     * @param url      upload url
     * @param clazz    upload params
     * @param file     upload file
     * @param callback callback
     */
    public void uploadFile(String url, Object clazz, File file, UploadCallback callback) {
        final MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        // add params
        String json = new Gson().toJson(clazz);
        builder.addFormDataPart(RequestConstant.UPLOAD_KEY_JSON, json);
        // add file
        if (file != null) {
            final MediaType type = MediaType.parse(guessMimeType(file.getName()));
            final RequestBody body = createCustomRequestBody(type, file, callback);
            builder.addFormDataPart(RequestConstant.UPLOAD_KEY_FILE, file.getName(), body);
        }
        RequestBody requestBody = builder.build();

        newRequestCall(callback, url, requestBody);
    }

    /**
     * 上传多个文件
     *
     * @param url      upload url
     * @param clazz    upload params
     * @param files    upload files
     * @param callback callback
     */
    public void uploadFiles(String url, Object clazz, List<File> files, UploadCallback callback) {
        newRequestCall(callback, url, buildRequestBody(clazz, files, callback));
    }

    private void newRequestCall(Callback callback, String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call  call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    protected RequestBody buildRequestBody(Object clazz, List<File> files, UploadCallback callback) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        String json = new Gson().toJson(clazz);
        // add params
        builder.addFormDataPart(RequestConstant.UPLOAD_KEY_JSON, json);
        //add file
        if (files != null) {
            int count = files.size();
            for (int i = 0; i < count; i++) {
                File fileInput = files.get(i);
                final MediaType type = MediaType.parse(guessMimeType(fileInput.getName()));
                final RequestBody fileBody = createCustomRequestBody(type, fileInput, callback);
                builder.addFormDataPart(RequestConstant.UPLOAD_KEY_FILE, fileInput.getName(), fileBody);
            }
        }
        return builder.build();
    }

    public void setConnectTimeout(int timeout, TimeUnit units) {
        getOkHttpClient().setConnectTimeout(timeout, units);
    }

    public void setReadTimeout(int timeout, TimeUnit units) {
        getOkHttpClient().setReadTimeout(timeout, units);
    }

    public void setWriteTimeout(int timeout, TimeUnit units) {
        getOkHttpClient().setWriteTimeout(timeout, units);
    }

//    public void cancelCall() {
//        if (call != null)
//            call.cancel();
//    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private static class ClientHolder {
        private static final UploadFileRequest CLIENT_REQUEST = new UploadFileRequest();
    }

}
