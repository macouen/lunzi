package com.oakzmm.demoapp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * DemoApp
 * Created by acer_april
 * on 2015/7/20
 * Description: customVolleyRequest
 */
public class CustomRequest<T> extends Request<T> {
    /**
     * Default charset for JSON request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";
    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final String mRequestBody;
    private Map<String, String> params;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url    URL of the request to make
     * @param clazz  Relevant class object, for Gson's reflection
     * @param params Map of request params
     */
    public CustomRequest(String url, Class<T> clazz, Map<String, String> params,
                         Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.clazz = clazz;
        this.headers = null;
        this.params = params;
        this.listener = listener;
        this.mRequestBody = null;
    }

    /**
     * Make a request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public CustomRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                         Map<String, String> params,
                         Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.params = params;
        this.listener = listener;
        this.mRequestBody = null;
    }

    public CustomRequest(int method, String url, Class<T> clazz, String requestBody, Response.Listener<T> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = null;
        this.params = null;
        this.listener = listener;
        this.mRequestBody = requestBody;
    }

    /**
     * @param builder requestBuilder
     */
    public CustomRequest(RequestBuilder builder) {
        super(builder.method, builder.url, builder.errorListener);
        clazz = builder.clazz;
        headers = builder.headers;
        listener = builder.successListener;
        mRequestBody = builder.requestBody;
        if (mRequestBody != null) {
            params = null;
        } else {
            params = builder.params;
        }
    }

    /**
     * Returns the content type of the POST or PUT body.
     */
    @Override
    public String getBodyContentType() {
        if (mRequestBody == null) {
            return super.getBodyContentType();
        }else{
            return PROTOCOL_CONTENT_TYPE;
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mRequestBody == null) {
            return super.getBody();
        } else {
            try {
                return mRequestBody.getBytes(PROTOCOL_CHARSET);
            } catch (UnsupportedEncodingException uee) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, PROTOCOL_CHARSET);
                return null;
            }
        }

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (clazz == null) {
                return (Response<T>) Response.success(parsed,
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success(gson.fromJson(parsed, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }

    }

    /**
     * requestBuilder  使用方法参见httpClientRequest
     */
    public static class RequestBuilder {
        private int method = Method.GET;
        private String url;
        private Class clazz;
        private Response.Listener successListener;
        private Response.ErrorListener errorListener;
        private Map<String, String> headers;
        private Map<String, String> params;
        private String requestBody;

        public RequestBuilder url(String url) {
            this.url = url;
            return this;
        }

        public RequestBuilder clazz(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public RequestBuilder successListener(Response.Listener successListener) {
            this.successListener = successListener;
            return this;
        }

        public RequestBuilder errorListener(Response.ErrorListener errorListener) {
            this.errorListener = errorListener;
            return this;
        }

        public RequestBuilder post() {
            this.method = Method.POST;
            return this;
        }

        public RequestBuilder addHeader(String key, String value) {
            if (headers == null)
                headers = new HashMap<>();
            headers.put(key, value);
            return this;
        }

        public RequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public RequestBuilder params(Map<String, String> params) {
            post();
            this.params = params;
            return this;
        }

        public RequestBuilder addParams(String key, String value) {
            if (params == null) {
                params = new HashMap<>();
                post();
            }
            params.put(key, value);
            return this;
        }

        public RequestBuilder toJSON() {
            final JSONObject jsonObject;
            if (params == null) {
                jsonObject = null;
            } else {
                jsonObject = new JSONObject(params);
            }
            requestBody = (jsonObject == null) ? null : jsonObject.toString();
            return this;
        }

        public RequestBuilder JSONString(String jsonBody) {
            requestBody = jsonBody;
            post();
            return this;
        }

        public CustomRequest build() {
            return new CustomRequest(this);
        }
    }

}
