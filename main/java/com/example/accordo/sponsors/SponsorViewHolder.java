package com.example.accordo.sponsors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accordo.OnRecycleSponspr;
import com.example.accordo.OnRecyclerViewClickListener;
import com.example.accordo.R;
import com.example.accordo.channel.Channel;

public class SponsorViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView textHolder;
    private ImageView imageHolder;
    private OnRecycleSponspr myRecyclerViewClickListener;

    public SponsorViewHolder(@NonNull View itemView,  OnRecycleSponspr onRecyclerViewClickListener) {

        super(itemView);
        textHolder = itemView.findViewById(R.id.textHolder);
        imageHolder = itemView.findViewById(R.id.imageHolder);
        itemView.setOnClickListener(this);
        this.myRecyclerViewClickListener = onRecyclerViewClickListener;

    }

    public void updateContent(Sponsor s){

        textHolder.setText(s.getText());
        String picture = s.getImage();

        byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


        imageHolder.setImageBitmap(decodedByte);
    }


    @Override
    public void onClick(View v) {
        myRecyclerViewClickListener.OnRecyclerSponsor(v, getAdapterPosition());


    }
}
