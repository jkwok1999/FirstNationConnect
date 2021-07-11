package com.example.firstnationconnect;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements View.OnClickListener {

    public PostAdapter() {

    }

    @Override
    public void onClick(View view) {

    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public PostViewHolder(View itemView) {
            super(itemView);
        }
    }

    public PostAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PostAdapter.PostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
