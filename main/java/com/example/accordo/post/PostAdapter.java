package com.example.accordo.post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accordo.Model;
import com.example.accordo.R;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private LayoutInflater mInflater;

    public PostAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        Log.d("danilo", getItemCount() + "");
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post_holder, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = Model.getInstance().getPost(position);


        holder.updateContent(post, position);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getPostsSize();
    }
}
