package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.accordo.sponsors.Sponsor;
import com.example.accordo.sponsors.SponsorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SponsorFullImage extends AppCompatActivity implements View.OnClickListener{

CommunicationController cc;
String text;
ImageView fullImage;
Button buttonFull;
String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_full_image);
        cc = new CommunicationController(this);

        Intent intent = getIntent();
          url = intent.getStringExtra("url");

         fullImage = findViewById(R.id.fullImageSponsor);
         buttonFull = findViewById(R.id.fullImageButton);
        buttonFull.setOnClickListener(this);

        cc.sponsors(this::sponsorSuccess, this::logError);

    }

    private void sponsorSuccess(JSONObject response) {

        try {
            JSONArray sponsors = response.getJSONArray("sponsors");


            for (int i = 0; i < sponsors.length(); i++) {
                JSONObject obj = sponsors.getJSONObject(i);


                if(obj.getString("url").equals(url)) {
                    Log.d("Danilo", "ok immagine");
                    byte[] decodedString = Base64.decode(obj.getString("image"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                    fullImage.setImageBitmap(decodedByte);
                }
            }

        } catch (JSONException e) {
            Log.d("Danilo JSON Error", "JSON Error", e);

        }


    }

    private void logError(VolleyError error) {
        Log.d("Danilo", "Error" + error.toString());
        Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        Log.d("Danilo", url);
        url = "http://" + url;
        Log.d("Danilo", url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}