package org.echomobile.refer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.echomobile.refer.objects.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeremygordon on 7/17/15.
 */
public class RestClient {
    OkHttpClient client;
    Gson gson;
    private Handler handler;
    private App app;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public RestClient(App _app) {
        client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(30, TimeUnit.SECONDS);    // socket timeout
        gson = new Gson();
        handler = new Handler();
        app = _app;
    }

    public String server_base() {
        return Constants.test_domain ? Constants.testServerBase : Constants.serverBase;
    }

    public void get(String url, HttpCallback cb) {
        call("GET", url, null, cb);
    }

    public void post(String url, RequestBody fb, HttpCallback cb) {
        call("POST", url, fb, cb);
    }

    public void apiPost(String path, HashMap<String, String> params, HttpCallback cb) {
        String url = this.server_base() + path;
        FormEncodingBuilder feb = new FormEncodingBuilder();
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            feb.add(entry.getKey(), entry.getValue());
        }
        post(url, feb.build(), cb);
    }

    public void apiGet(String path, HashMap<String, String> params, HttpCallback cb) {
        String url = this.server_base() + path;
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Iterator it = params.entrySet().iterator();
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        get(builder.build().toString(), cb);
    }


    public void userGet(String path, HashMap<String, String> params, HttpCallback cb, Client client) {
        if (client == null) return;
        if (params == null) params = new HashMap<String, String>();
        params.put("phone", String.valueOf(client.phone));
        params.put("auth", Constants.AUTH);
        apiGet(path, params, cb);
    }

    public void userPost(String url, HashMap<String, String> params, HttpCallback cb, Client client) {
        if (client == null) return;
        if (params == null) params = new HashMap<String, String>();
        params.put("phone", String.valueOf(client.phone));
        params.put("auth", Constants.AUTH);
        apiPost(url, params, cb);
    }

    private void call(String method, String url, RequestBody fb, final HttpCallback cb) {
        Request.Builder builder = new Request.Builder();
        builder = builder.url(url);
        if (fb != null) {
            builder = builder.post(fb);
        }
        Log.d(Constants.TAG, method + " to " + url);
        Request request = builder.build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                cb.onFailure(null, null);
            }

            @Override
            public void onResponse(Response response) {
                if (!response.isSuccessful()) {
                    Log.e(Constants.TAG, "call failed - " + response.message());
                    cb.onFailure(response, null);
                    return;
                }
                String string_response = "";
                try {
                    string_response = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    cb.onFailure(response, null);
                    return;
                }
                cb.onSuccess(string_response);
            }
        });
    }




    public interface HttpCallback  {
        public void onFailure(Response response, Throwable throwable);
        public void onSuccess(String string_response);
    }

    public interface SimpleJSONCallback  {
        public void onSuccess(Object json_response);
    }

    public interface SimpleCallback  {
        public void onSuccess();
    }

    public void toastToActivity(final String text) {
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(app, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void dismissDialogOnUIThread(final ProgressDialog dialog) {
        handler.post(new Runnable() {
            public void run() {
                if (dialog != null) dialog.dismiss();
            }
        });
    }

    public void hideProgressOnUIThread(final ProgressBar pb) {
        handler.post(new Runnable() {
            public void run() {
                if (pb != null) pb.setVisibility(View.GONE);
            }
        });
    }

    public static ProgressDialog open_loader(AuthenticateActivity cx, String loading_text, boolean indeterminate) {
        if (loading_text == null) loading_text = "Working...";
        ProgressDialog dialog = new ProgressDialog((Context) cx);
        dialog.setMessage(loading_text);
        dialog.setTitle("Please Wait");
        dialog.setCancelable(false);
        dialog.setIndeterminate(indeterminate);
        if (!indeterminate) {
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
        }
        dialog.show();
        return dialog;
    }
}