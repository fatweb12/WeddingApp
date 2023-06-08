package co.fatweb.com.wedding.Connectivity;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by MG on 04-03-2018.
 */

public class ApiCall1 {
    private static ApiCall1 mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public ApiCall1(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiCall1 getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall1(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = "https://ido.kiwi/api/common/suburbs";
        Log.d("Suburb URL",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall1.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}
