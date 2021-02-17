package com.example.accordo.sponsors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accordo.Model;
import com.example.accordo.OnRecycleSponspr;
import com.example.accordo.OnRecyclerViewClickListener;
import com.example.accordo.R;


public class SponsorAdapter extends RecyclerView.Adapter<SponsorViewHolder> {
    private LayoutInflater mInflater;
    private OnRecycleSponspr mRecyclerViewClickListener;

    public SponsorAdapter(Context context, OnRecycleSponspr recyclerViewClickListener){
        this.mInflater = LayoutInflater.from(context);
        this.mRecyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public SponsorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sponsor_holder, parent, false);

        return new SponsorViewHolder(view, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SponsorViewHolder holder, int position) {
        Sponsor sponsor = Model.getInstance().getSponsor(position);

        holder.updateContent(sponsor);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSponsorSize();
    }
}
