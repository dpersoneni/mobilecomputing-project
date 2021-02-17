package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.accordo.channel.Channel;
import com.example.accordo.channel.ChannelAdapter;
import com.example.accordo.sponsors.SponsorsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WallActivity extends AppCompatActivity implements OnRecyclerViewClickListener, View.OnClickListener {
    //instanzio una volta la coda delle richieste per non doverne dichiarare di più
    SharedPreferences sharedPreferences;
    OnRecyclerViewClickListener context;
    CommunicationController cc;
    TextView sponsors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("danilo", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final Activity thisActivity = this;

        cc = new CommunicationController(this);
        context = this;
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        sponsors = findViewById(R.id.sponsors);
        sponsors.setOnClickListener(this);



        if (sharedPreferences.getBoolean("first_access", true)) {

            cc.register(this::takeSidAndShowWall, this::logError);

        } else {

            String user_sid = sharedPreferences.getString("user_sid", null);
            cc.getWall(user_sid, this::showWall, this::logError);

        }
        FloatingActionButton buttonCreateChannel = findViewById(R.id.createChannel);
        buttonCreateChannel.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        Log.d("Danilo", "onResume");
        super.onResume();
    }

    private void showWall(JSONObject response) {
      //  Model.getInstance().clearChannels();
        Log.d("Danilo Volley", "Correct: " + response.toString());

        try {
            JSONArray arrayChannels = response.getJSONArray("channels");
            for (int i = 0; i < arrayChannels.length(); i++) {
                JSONObject obj = arrayChannels.getJSONObject(i);
                Channel c = new Channel(obj.getString("ctitle"), obj.getString("mine").equals("t"));
                Model.getInstance().addChannel(c);
            }
        } catch (JSONException e) {
            Log.d("Danilo JSON Error", "JSON Error", e);

        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewChannel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ChannelAdapter adapter = new ChannelAdapter(getApplicationContext(), context);
        recyclerView.setAdapter(adapter);
    }

    private void takeSidAndShowWall(JSONObject response) {
        Log.d("Danilo Volley", "Correct: " + response.toString());
        try {
            String user_sid = response.getString("sid");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_sid", user_sid);

            editor.putBoolean("first_access", false);
            editor.apply();

            cc.getWall(user_sid, this::showWall, this::logError);

        } catch (Exception e) {
            Log.d("Danilo Volley", "Error: JSON sid ");
        }
    }

    private void logError(VolleyError error) {
        Log.d("Danilo Volley", "Error: " + error.toString());
    }

    @Override
    public void OnRecyclerViewClick(View v, int position) {

        Intent intent = new Intent(this, ChannelActivity.class);
        intent.putExtra("ctitle", Model.getInstance().getTitleChannel(position));
        startActivity(intent);
        Log.d("Danilo", "click recycler funzionante");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miProfile:
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.sponsors):
                Intent intentSponsors = new Intent(getApplicationContext(), SponsorsActivity.class);
                startActivity(intentSponsors);
             break;
            case (R.id.createChannel):
                Intent intentCreate = new Intent(getApplicationContext(), CreateChannelActivity.class);
                startActivity(intentCreate);
                break;


        }

    }
}

 /*
        if (sharedPreferences.getBoolean("first_access", true)) {
            cc.register(response -> takeSidAndShowWall(response));
            Log.d("Main Activity", "FIRST ACCESS");
            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.putBoolean("silentMode", false);
            editor.apply();
            final String url = " https://ewserver.di.unimi.it/mobicomp/accordo/register.php";
            //ecco la richiesta: imposto il tipo (get), l'url e a null il body della richiesta
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("Volley", "Correct: " + response.toString());
                            try {
                                String mySid = response.getString("sid");

                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.putString("sid", mySid);
                                editor.putBoolean("first_access", false);
                                editor.apply();

                            } catch (JSONException e) {
                                Log.d("Volley", "Eroore");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Volley", "Error: " + error.toString());
                }
            });
            Log.d("Volley", "Sending request");
            //incodo la richiesta
            requestQueue.add(request);
        } else {
            Log.d("den", "other_access");
            String user_id = sharedPreferences.getString("sid", null);
            Log.d("Volley", "" + user_id);
            Map<String, String> params = new HashMap();
            params.put("sid", user_id);
            JSONObject parameters = new JSONObject(params);

            FloatingActionButton fab = findViewById(R.id.createChannel);
            final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getWall.php";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url , parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("Volley", "Correct: " + response.toString());
                            try {
                                JSONArray arrayChannel = response.getJSONArray("channels");
                                int i;
                                for (i=0; i < arrayChannel.length(); i++) {
                                    JSONObject obj = arrayChannel.getJSONObject(i);
                                    Channel channel = new Channel(obj.getString("ctitle"), obj.getString("mine").equals("t"));
                                    Model.getInstance().addChannel(channel);
                                    // Log.d("den", channel.toString());
                                }
                                RecyclerView recyclerView = findViewById(R.id.recyclerViewChannel);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                ChannelAdapter channelAdapter = new ChannelAdapter(getApplicationContext(),context);
                                recyclerView.setAdapter(channelAdapter);
                            } catch (JSONException e ) {
                                Log.d("den", "errore JSON ");
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Volley", "Error: " + error.toString());
                }
            });
            Log.d("Volley", "Sending request");
            //incodo la richiesta
            requestQueue.add(request);

        }
    }

         */