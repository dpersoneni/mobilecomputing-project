package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.accordo.post.Post;
import com.example.accordo.post.PostImage;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        ImageView imageView = findViewById(R.id.imageFull);

        int positionPassed = getIntent().getIntExtra("image", 0);

        Post postImage = Model.getInstance().getPost(positionPassed);
        Bitmap image = ((PostImage) postImage).getImageContent();

        imageView.setImageBitmap(image);
    }
}