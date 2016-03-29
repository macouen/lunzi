package com.oakzmm.demoapp.network;


import android.content.Context;

import com.aprbrother.patrol.model.request.UploadFileEntity;
import com.aprbrother.patrol.utils.AprilLog;
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
 *
 * 用法同HttpClientRequest，
 * 注意
 * 1.callback 的回调非UI线程；
 * 2.需要自己解析Response数据，暂不支持解析为JavaBean，解析方法  new Gson().fromJson( response.body().string(),Bean.class);
 */
public class UploadFileRequest {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Context mCtx;
    private OkHttpClient mOkHttpClient;
    private Call call;
   private  String url = RequestConstant.UPLOAD_FILE;

    private UploadFileRequest() {
        mOkHttpClient = new OkHttpClient();
    }

    public static UploadFileRequest getInstance(Context context) {
        mCtx = context;
        return ClientHolder.CLIENT_REQUEST;
    }

    private OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 上传单个image文件
     * @param uploadFile   upload params
     * @param file upload file
     * @param callback callback
     */
    public void uploadImage(UploadFileEntity uploadFile,File file, UploadCallback callback) {


        String json = new Gson().toJson(uploadFile);
//        RequestBody body = RequestBody.create(JSON, json);
        final MediaType type = MediaType.parse(guessMimeType(file.getName()));
        final RequestBody body = createCustomRequestBody(type, file, callback);

        AprilLog.i(type.type());
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
//                .addPart(body)
//        .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + "json" + "\""),
//                RequestBody.create(JSON, json))
                .addFormDataPart(RequestConstant.UPLOAD_KEY_JSON, json)
                .addFormDataPart(RequestConstant.UPLOAD_KEY_FILE, file.getName(), body)
                .build();

        newRequestCall(callback, url, requestBody);
    }

    /**
     * 上传多个文件
     *
     * @param uploadFile upload params
     * @param files upload files
     * @param callback callback
     */
    public void uploadFiles(UploadFileEntity uploadFile,List<File> files, UploadCallback callback) {
        newRequestCall(callback, url, buildRequestBody(uploadFile,files,callback));
    }

    private void newRequestCall(Callback callback, String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }


    protected RequestBody buildRequestBody(UploadFileEntity uploadFile,List<File> files,UploadCallback callback) {
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        String json = new Gson().toJson(uploadFile);
        // add params
        builder.addFormDataPart(RequestConstant.UPLOAD_KEY_JSON, json);
        //add file
        for (int i = 0; i < files.size(); i++) {
            File fileInput = files.get(i);
            final MediaType type = MediaType.parse(guessMimeType(fileInput.getName()));
            final RequestBody fileBody = createCustomRequestBody(type, fileInput, callback);
            builder.addFormDataPart(RequestConstant.UPLOAD_KEY_FILE, fileInput.getName(), fileBody);
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

    public void cancelCall() {
        if (call != null)
            call.cancel();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final UploadCallback listener) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
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
    private static class ClientHolder {
        private static final UploadFileRequest CLIENT_REQUEST = new UploadFileRequest();
    }

}
