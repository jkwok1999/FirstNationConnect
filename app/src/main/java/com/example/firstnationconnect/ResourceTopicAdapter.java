package com.example.firstnationconnect;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResourceTopicAdapter extends RecyclerView.Adapter<ResourceTopicAdapter.ResourceTopicViewHolder> implements View.OnClickListener {
    private ResourceTopicActivity mParentActivity;
    private List<Resource> mResources;

    public ResourceTopicAdapter(ResourceTopicActivity parent, List<Resource> resources) {
        mParentActivity = parent;
        mResources = resources;
    }

    @Override
    public void onClick(View v) {
        Resource resource = (Resource) v.getTag();

        Context context = v.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resource.getLink()));

        context.startActivity(intent);
    }

    public class ResourceTopicViewHolder extends RecyclerView.ViewHolder {

        public TextView tvResourceName, tvResourceLink;
        private ImageView ivWebsite;

        public ResourceTopicViewHolder(View v) {
            super(v);
            tvResourceName = v.findViewById(R.id.tvResourceName);
            tvResourceLink = v.findViewById(R.id.tvResourceLink);
            ivWebsite = v.findViewById(R.id.ivWebsite);
        }
    }

    public ResourceTopicAdapter.ResourceTopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_row, parent, false);
        return new ResourceTopicAdapter.ResourceTopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ResourceTopicAdapter.ResourceTopicViewHolder holder, int position) {
        Resource resource = mResources.get(position);
        holder.tvResourceName.setText(resource.getTitle());
        holder.tvResourceLink.setText("Link: " + resource.getLink());
        holder.ivWebsite.setImageResource(resource.getResourcePic());

        holder.itemView.setTag(resource);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mResources.size();
    }


}
