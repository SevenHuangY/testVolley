package com.seven.testvolley;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "test";
    private String url = "http://api.androidhive.info/volley/person_object.json";
    private String url2 = "http://foo.cloudant.com/_session";
    private MyVolley mVolley;
    private MyHandler mHandler;

    @ViewById(R.id.button)
    Button StringRequestGetBtn;

    @ViewById(R.id.button2)
    Button JsonObjectRequestGetBtn;

    @ViewById(R.id.button3)
    Button StringRequestPostBtn;

    @ViewById(R.id.tx)
    TextView tx;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);
    }

    private void updateTextView(String text)
    {
        Message msg = new Message();
        msg.what = 0x1002;
        msg.obj = text;
        mHandler.sendMessage(msg);
    }

    @Click(R.id.button)
    void StringRequestGetBtnClick()
    {
        updateTextView("");
        StringRequestGet();
    }

    @Click(R.id.button2)
    void JsonObjectRequestGetBtnClick()
    {
        updateTextView("");
        JsonObjectRequestGet();
    }

    @Click(R.id.button3)
    void StringRequestPostBtnClick()
    {
        updateTextView("");
        StringRequestPost();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(TAG, "onResume");
        mVolley = MyVolley.getInstance();
        mVolley.setContext(this);
    }

    public void StringRequestPost()
    {
        StringRequest request = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    Log.i(TAG, "onResponse: " + response);
                    updateTextView(response);
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.i(TAG, "onErrorResponse: " + error);
                    updateTextView(error.toString());
                }
            })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/x-www-form-urldecoded");
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<>();
                map.put("name", "Androidhive");
                map.put("email", "abc@androidhive.info");
                map.put("password", "password123");
                return map;
            }
        };

        mVolley.addRequesGet(request);
    }

    public void JsonObjectRequestGet()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    Log.i(TAG, "onResponse: " + response);
                    updateTextView(response.toString());
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.i(TAG, "error: " + error.toString());
                    updateTextView(error.toString());
                }
            });
        mVolley.addRequesGet(jsonObjectRequest);
    }

    public void StringRequestGet()
    {
        StringRequest request = new StringRequest(Request.Method.GET, url2,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    Log.i(TAG, "reponse: " + response);
                    updateTextView(response.toString());
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.i(TAG, "error: " + error.toString());
                    updateTextView(error.toString());
                }
            });
        mVolley.addRequesGet(request);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(TAG, "onStop");
        if(mVolley != null)
        {
            mVolley.cancel();
        }
    }

    private class MyHandler extends Handler
    {
        WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity)
        {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            MainActivity activity = mActivity.get();
            if(activity == null)
                return;

            switch (msg.what)
            {
                case 0x1002:
                    activity.tx.setText(msg.obj.toString());
                    break;
            }
        }
    }
}
