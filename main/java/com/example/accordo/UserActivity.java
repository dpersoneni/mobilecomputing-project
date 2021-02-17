package com.example.accordo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 0;
    private static final int MAX_IMAGE_SIZE = 137000;
    CommunicationController cc;
    private final static String SET_PICTURE = "picture";
    private final static String SET_NAME = "name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);



        cc = new CommunicationController(this);
        cc.getProfile(response -> {
            showNameInTextView(response);
            showPictureinImageView(response);
        }, this::logUserError);


        Button updateImage = findViewById(R.id.updateImage);
        updateImage.setOnClickListener(this);
        Button updateName = findViewById(R.id.updateName);
        updateName.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateImage:

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                Log.d("Danilo", "click su image update1");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;

            case R.id.updateName:

                TextView textView = findViewById(R.id.userName);
                String name = textView.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: name can't be blank", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff6f6c"), PorterDuff.Mode.SRC_IN);
                    toast.show();
                    break;
                } else if (name.length() > 20) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: user name can't be longer than 20 letters", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff6f6c"), PorterDuff.Mode.SRC_IN);
                    toast.show();
                } else {
                    cc.setProfile(SET_NAME, name, response -> changeNameInTextView(), this::logUserError);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (encoded.length() > MAX_IMAGE_SIZE) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: picture is too big.", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff6f6c"), PorterDuff.Mode.SRC_IN);
                    toast.show();

                    return;
                }
                cc.setProfile(SET_PICTURE, encoded, response -> changeImageFromGallery(decodedByte), this::logUserError);



                //    Log.d("Danilo - B64 img", encoded);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showNameInTextView(JSONObject response) {
        Log.d("Danilo", response.toString());
        TextView textView = findViewById(R.id.userName);

        try {
            String name = response.getString("name");
            textView.setText(name);


        } catch (Exception e) {
            Log.d("Danilo Volley", "Error: JSON sid ");
        }

    }

    private void showPictureinImageView(JSONObject response) {
        ImageView imageView = findViewById(R.id.userImage);

        try {
            String picture = response.getString("picture");

            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


            imageView.setImageBitmap(decodedByte);


        } catch (Exception e) {
            Log.d("Danilo Volley", "Error: JSON sid ");
        }

    }

    private void logUserError(VolleyError error) {
        Log.d("Danilo Volley", "Error: " + error.toString());
    }

    public void changeNameInTextView() {

        Toast toast = Toast.makeText(getApplicationContext(), "Username successfully changed!", Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.parseColor("#93ff95"), PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    public void changeImageFromGallery(Bitmap decodedByte) {
        Log.d("Danilo", "daje");
        ImageView imageView = findViewById(R.id.userImage);
        imageView.setImageBitmap(decodedByte);
        Toast toast = Toast.makeText(getApplicationContext(), "Profile image successfully changed!", Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.parseColor("#93ff95"), PorterDuff.Mode.SRC_IN);
        toast.show();
    }


}