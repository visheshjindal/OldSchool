package com.vishesh.oldschool;


import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;

public class OldSchoolApplication extends Application {

    public static final String TAG = OldSchoolApplication.class.getSimpleName();
    public static boolean IS_NETWORK_AVAILABLE = true;
    public static boolean isWorkshopRegistered = false;
    private RequestQueue mRequestQueue;
    //    private ImageLoader mImageLoader;
    private RetryPolicy policy;
    int socketTimeOut = 60000;

    /**
     * Receiver to fetch status of the network
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            OldSchoolApplication.IS_NETWORK_AVAILABLE = activeNetInfo != null && activeNetInfo.isAvailable() && activeNetInfo.isConnected();
        }
    };



    @Override
    public void onCreate() {
        FacebookSdk.sdkInitialize(this);

        super.onCreate();



        policy = new DefaultRetryPolicy(socketTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        registerReceiver(mReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

//    public void cancelPendingRequests(Object tag) {
//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(tag);
//        }
//    }

    public void addToRequestQueue(String tag_string_req, StringRequest stringRequest) {
        stringRequest.setRetryPolicy(policy);

        addToRequestQueue(stringRequest, tag_string_req);
    }

    public void addToRequestQueue(String tag, JsonObjectRequest jsonObjectRequest) {

        jsonObjectRequest.setRetryPolicy(policy);

        addToRequestQueue(jsonObjectRequest, tag);
    }

    /**
     * To fetch credential stored in the Shared Preferences
     * @param key Required Credential key e.g. password, username
     * @return value of the fetched credential
     */
//    public String getSharedCredentials(String key) {
//        SharedPreferences setting =  getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
//        return setting.getString(key, "");
//
//    }


}



