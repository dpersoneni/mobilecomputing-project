package com.example.accordo.channel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accordo.Model;
import com.example.accordo.OnRecyclerViewClickListener;
import com.example.accordo.R;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelViewHolder> {

    private LayoutInflater mInflater;
    private OnRecyclerViewClickListener mRecyclerViewClickListener;

    public ChannelAdapter(Context context, OnRecyclerViewClickListener recyclerViewClickListener){
        this.mInflater = LayoutInflater.from(context);
        this.mRecyclerViewClickListener = recyclerViewClickListener;
    }


    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.channel_holder, parent, false);

        return new ChannelViewHolder(view, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        Channel channel = Model.getInstance().getChannel(position);
        holder.updateContent(channel);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getChannelsSize();
    }
}
