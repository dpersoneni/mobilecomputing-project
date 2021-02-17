package com.example.accordo.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accordo.FullImageActivity;
import com.example.accordo.ImageController;
import com.example.accordo.OnRecyclerViewClickListener;
import com.example.accordo.R;
import com.example.accordo.ViewLocationActivity;

import java.io.ByteArrayOutputStream;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView userPictureHolder;
    private final TextView nameHolder;
    private final TextView textHolder;
    private final ImageView postImageHolder;
    private final Button postLocationHolder;

    private static final String textType = "t";
    private static final String imageType = "i";
    private static final String locationType = "l";

    Context context;

    private Double longitude;
    private Double latitude;

    private String imageToPass;
    private int positionToPass;


    private String defaultName = "Anonymous user";

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        userPictureHolder = itemView.findViewById(R.id.pictureUserPost);
        nameHolder = itemView.findViewById(R.id.userNamePost);
        textHolder = itemView.findViewById(R.id.postText);
        postImageHolder = itemView.findViewById(R.id.postImage);
        postLocationHolder = itemView.findViewById(R.id.viewPosition);

        int color = 0xffc4cccf;

        postLocationHolder.setBackgroundColor(color);


    }

    public void updateContent(Post post, int position) {

        if (post.getProfilePicture() != null) {
            userPictureHolder.setImageBitmap(post.getProfilePicture());
        } else {
            userPictureHolder.setImageResource(R.drawable.user);
        }


        if (!post.getName().equals("null")) {
            nameHolder.setText(post.getName());
        } else {
            nameHolder.setText(defaultName);
        }

        switch (post.getType()) {
            case "t":

                postImageHolder.setVisibility(View.GONE);
                postLocationHolder.setVisibility(View.GONE);
                textHolder.setVisibility(View.VISIBLE);

                Log.d("Daniloo", ((PostText) post).getTextContent());
                if (((PostText) post).getTextContent().equals("")) {
                    textHolder.setText("EMPTY TEXT");
                    break;
                }
                textHolder.setText(((PostText) post).getTextContent());
                break;
            case "i":
                Log.d("Daniloo", "gggggggg");

                textHolder.setVisibility(View.GONE);
                postLocationHolder.setVisibility(View.GONE);
                postImageHolder.setVisibility(View.VISIBLE);

                positionToPass = position;


                //   Log.d("DaniloImmagine", ((PostImage) post).getImageContent());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                if (((PostImage) post).getImageContent() != null) {
                    ((PostImage) post).getImageContent().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                imageToPass = encoded;

                //  Bitmap decodedByte = ImageController.fromBaseToBitmap(((PostImage) post).getImageContent());
                postImageHolder.setImageBitmap(((PostImage) post).getImageContent());

                postImageHolder.setOnClickListener(this);
                break;

            case "l":
                textHolder.setVisibility(View.GONE);
                postImageHolder.setVisibility(View.GONE);
                postLocationHolder.setVisibility(View.VISIBLE);

                Log.d("Daniloo", "position");
                longitude = ((PostLocation) post).getLongitude();
                latitude = ((PostLocation) post).getLatitude();
                postLocationHolder.setOnClickListener(this);

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case (R.id.viewPosition):
                Intent intent = new Intent(context.getApplicationContext(), ViewLocationActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;

            case (R.id.postImage):
                Log.d("Danilo", "click immagine");
                Intent intent1 = new Intent(context.getApplicationContext(), FullImageActivity.class);
                intent1.putExtra("image", positionToPass);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
        }

    }

}



/*

                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    postImageHolder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    postImageHolder.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    postImageHolder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    postImageHolder.setScaleType(ImageView.ScaleType.FIT_XY);
                }
*/


/*
        if (post.getType().equals(textType) && !((PostText) post).getTextContent().equals("")) {
            postImageHolder.setVisibility(View.INVISIBLE);
            postLocationHolder.setVisibility(View.INVISIBLE);

            textHolder.setText(((PostText) post).getTextContent());
        }else if (post.getType().equals(imageType)) {
            textHolder.setVisibility(View.INVISIBLE);
            postLocationHolder.setVisibility(View.INVISIBLE);
             Log.d("Daniloo", "gggggggg");
             Log.d("Daniloo", ((PostImage) post).getImageContent());
                try {
                    byte[] decodedString = Base64.decode(((PostImage) post).getImageContent(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    postImageHolder.setImageBitmap(decodedByte);
                } catch (IllegalArgumentException e) {
                    Log.d("Danilo", e.toString());
                }


            // Log.d("Danilooo", decodedByte + "");

        }else if (post.getType().equals(locationType)) {
            textHolder.setVisibility(View.INVISIBLE);
            postImageHolder.setVisibility(View.INVISIBLE);
            postLocationHolder.setVisibility(View.VISIBLE);
            Log.d("Daniloo", "position");
            longitude = ((PostLocation) post).getLongitude();
            latitude = ((PostLocation) post).getLatitude();
            postLocationHolder.setOnClickListener(this);

        }
        */