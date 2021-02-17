package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateChannelActivity extends AppCompatActivity implements View.OnClickListener {
    String text = "Create here a new awesome channel, you just have to think of a new title! ";
    CommunicationController cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);
        cc = new CommunicationController(this);

        TextView tv = findViewById(R.id.descriptionCreateChannel);
        tv.setText(text);

        Button createNewChannel = findViewById(R.id.createNewChannel);
        createNewChannel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        TextView textView = findViewById(R.id.insertTextPost);
        String title = textView.getText().toString().trim();

        if (title.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error: title can't be blank", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.parseColor("#ff6f6c"), PorterDuff.Mode.SRC_IN);
            toast.show();
            Log.d("den", "prova");

        } else if (title.length() > 20) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error: title can't be longer than 20 letters", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.parseColor("#ff6f6c"), PorterDuff.Mode.SRC_IN);
            toast.show();
        } else {
            Log.d("den", title);

            cc.addChannel(title, response -> {
                Log.d("den", response.toString());

                Intent intent = new Intent(getApplicationContext(), WallActivity.class);
                startActivity(intent);
            }, error -> {
                Log.d("DaniloErr", error.toString());
                Toast toast = Toast.makeText(getApplicationContext(), "Title already taken. Think of a new one!", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.parseColor("#ff6f6c"), PorterDuff.Mode.SRC_IN);
                toast.show();
            });


        }


    }
}