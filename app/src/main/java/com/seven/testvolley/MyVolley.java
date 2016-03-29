package com.seven.testvolley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HuangYW on 2016/3/28.
 */
public class MyVolley
{
    private RequestQueue mRequestQueue;
    private Context context;

    private static class inner
    {
        static MyVolley mInstance = new MyVolley();
    }

    public static MyVolley getInstance()
    {
        return inner.mInstance;
    }

    public void setContext(Context context)
    {
        this.context = context;
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());

    }

    public MyVolley()
    {
    }

    public void clearCache()
    {
        mRequestQueue.getCache().clear();
    }

    public void addRequesGet(Request request)
    {
        mRequestQueue.add(request);

    }

    public void cancel()
    {
        mRequestQueue.cancelAll(this);
        mRequestQueue.stop();
    }
}
