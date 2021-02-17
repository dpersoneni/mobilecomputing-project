package com.example.accordo.sponsors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.accordo.ChannelActivity;
import com.example.accordo.CommunicationController;
import com.example.accordo.Model;
import com.example.accordo.OnRecycleSponspr;
import com.example.accordo.OnRecyclerViewClickListener;
import com.example.accordo.R;
import com.example.accordo.SponsorFullImage;
import com.example.accordo.channel.Channel;
import com.example.accordo.channel.ChannelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SponsorsActivity extends AppCompatActivity implements OnRecycleSponspr {
    CommunicationController cc;

    OnRecycleSponspr context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);

        cc = new CommunicationController(this);
        context = this;

        cc.sponsors(this::sponsorSuccess, this::logError);
    }

    private void sponsorSuccess(JSONObject response) {

        try {
            JSONArray sponsors = response.getJSONArray("sponsors");
            Log.d("Danilo", String.valueOf(sponsors.length()));
            for (int i = 0; i < sponsors.length(); i++) {
                JSONObject obj = sponsors.getJSONObject(i);
                Sponsor s = new Sponsor(obj.getString("url"), obj.getString("text"), obj.getString("image"));
                Model.getInstance().addSponsor(s);
            }

        } catch (JSONException e) {
            Log.d("Danilo JSON Error", "JSON Error", e);

        }

      //  Model.getInstance().printAllSponsors();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSponsors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SponsorAdapter adapter = new SponsorAdapter(getApplicationContext(), context);
        recyclerView.setAdapter(adapter);

    }

    private void logError(VolleyError error) {
        Log.d("Danilo", "Error" + error.toString());
        Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_LONG).show();
    }


    @Override
    public void OnRecyclerSponsor(View v, int position) {
        Log.d("Danilo", "ho cliccato su sponsor");

        Intent intent = new Intent(this, SponsorFullImage.class);
        intent.putExtra("url", Model.getInstance().getSponsor(position).getUrl());
        startActivity(intent);
    }
}