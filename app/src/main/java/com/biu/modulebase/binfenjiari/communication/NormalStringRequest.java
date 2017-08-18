package com.biu.modulebase.binfenjiari.communication;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Volley 普通post请求对象
 *
 * @author Lee
 */
public class NormalStringRequest extends Request<JSONObject> {
    private Map<String, String> headers;
    private Map<String, Object> params;
    private Listener<JSONObject> mListener;

    public NormalStringRequest(Map<String, Object> map, String url, Listener<JSONObject> listener,
            ErrorListener errorListener)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        params = map;
    }

    public NormalStringRequest(Map<String, Object> map, String url, int method,
            Listener<JSONObject> listener, ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mListener = listener;
        params = map;
    }

    public NormalStringRequest(Map<String, String> headers, Map<String, Object> map, String url,
            int method, Listener<JSONObject> listener, ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mListener = listener;
        this.headers = headers;
        params = map;
    }

    // mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers != null) {
            return headers;
        } else {
            return super.getHeaders();
        }

    }

    // 此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}