package com.example.accordo.channel;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accordo.OnRecyclerViewClickListener;
import com.example.accordo.R;

public class  ChannelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView titleHolder;
    private TextView mineHolder;
    private OnRecyclerViewClickListener myRecyclerViewClickListener;

    public ChannelViewHolder(@NonNull View itemView, OnRecyclerViewClickListener onRecyclerViewClickListener) {
        super(itemView);
            titleHolder = itemView.findViewById(R.id.titleHolder);
            mineHolder = itemView.findViewById(R.id.mineHolder);

        itemView.setOnClickListener(this);
        this.myRecyclerViewClickListener = onRecyclerViewClickListener;
    }

    public void updateContent(Channel c){
       titleHolder.setText(c.getTitle());
       titleHolder.setTypeface(null, Typeface.BOLD);
       if (c.isMine()) {
           titleHolder.setTypeface(null, Typeface.BOLD);
       } else {
           titleHolder.setTypeface(null, Typeface.NORMAL);
       }
    }

    @Override
    public void onClick(View v) {
        myRecyclerViewClickListener.OnRecyclerViewClick(v, getAdapterPosition());
    }
}
