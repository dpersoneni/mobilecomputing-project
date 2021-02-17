package com.example.accordo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommunicationController {
    private static final String BASE_URL = "https://ewserver.di.unimi.it/mobicomp/accordo/";
    private final RequestQueue requestQueue;
    private final String user_sid;

    public CommunicationController(Context context){
        requestQueue = Volley.newRequestQueue(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("WallActivity",Context.MODE_PRIVATE);
        user_sid = sharedPreferences.getString("user_sid",null);
    }

    public void register(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        final String service_url = "register.php";
        final String url = BASE_URL + service_url;
        final JSONObject jsonBody = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request" + service_url);
        requestQueue.add(request);
    }

    public void getWall(String user_sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "getWall.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url + user_sid);
        requestQueue.add(request);
    }

    public void getProfile(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "getProfile.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);
        requestQueue.add(request);
    }

    public void setProfile(String paramToSet, String paramValue, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "setProfile.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        params.put(paramToSet, paramValue);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url + ", setting "+ paramToSet);
        requestQueue.add(request);
    }

    public void getChannel(String ctitle, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "getChannel.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        params.put("ctitle" , ctitle);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);
        requestQueue.add(request);
    }

    public void addChannel(String ctitle, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "addChannel.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        params.put("ctitle" , ctitle);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);

        requestQueue.add(request);
    }

    public void addPost(String ctitle, String type, String lat, String lon, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "addPost.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        params.put("ctitle" , ctitle);
        params.put("type" , type);
        params.put("lat" , lat);
        params.put("lon" , lon);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);

        requestQueue.add(request);
    }

    public void addPost(String ctitle, String type, String content, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "addPost.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        params.put("ctitle" , ctitle);
        params.put("type" , type);
        params.put("content" , content);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);

        requestQueue.add(request);
    }

    public void getPostImage(String pid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "getPostImage.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        params.put("pid" , pid);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);

        requestQueue.add(request);
    }

    public void getUserPicture(String uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "getUserPicture.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        params.put("uid" , uid);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);

        requestQueue.add(request);
    }

    public void sponsors(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String service_url = "sponsors.php";
        final String url = BASE_URL + service_url;

        Map<String, String> params = new HashMap();
        params.put("sid", user_sid);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(url, jsonBody, responseListener, errorListener);
        Log.d("Danilo Volley", "Sending Request " + service_url);
        requestQueue.add(request);
    }

}
