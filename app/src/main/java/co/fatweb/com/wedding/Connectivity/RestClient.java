package co.fatweb.com.wedding.Connectivity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.security.KeyStore;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by Medi on 11/12/2016.
 */

public class RestClient {


    private static SyncHttpClient client = new SyncHttpClient();
    //private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(6*10000);
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(6*10000);
        client.post(url, params, responseHandler);
    }
    public static void post(Context context, String url, HttpEntity entity, String mimeType, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(6*10000);
        try {
            MySSLSocketFactory sf;
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {
        }
        client.post(context,url,entity,mimeType, responseHandler);

    }


    public static void post(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        try {
            MySSLSocketFactory sf;
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);

        }
        catch (Exception e) {
        }
        client.setTimeout(6*10000);
        client.post(context, url, entity, "application/json", responseHandler);
    }



//    public  static void post(String url, RequestParams params,String jsonParams, AsyncHttpResponseHandler responseHandler){
//        client.post(url, params,jsonParams, responseHandler);
//        clinet.po
//    }


    public static void put(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        try {
            MySSLSocketFactory sf;
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {
        }
        client.setTimeout(6*10000);
        client.put(context, url, entity, "application/json", responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(6*10000);
        client.put(url, params, responseHandler);

    }

    public static void patch(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(6*20000);
        client.patch(url, params, responseHandler);
    }

    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.delete(url, params, responseHandler);


    }
    public static void delete(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(6*10000);
        client.delete(context, url, entity, "application/json", responseHandler);
    }





    }
