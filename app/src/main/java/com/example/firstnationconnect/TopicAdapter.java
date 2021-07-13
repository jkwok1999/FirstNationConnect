package com.example.firstnationconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> implements View.OnClickListener{
    private ForumActivity mParentActivity;
    private List<Topic> mTopics;

    public TopicAdapter(ForumActivity parent, List<Topic> topics) {
        mParentActivity = parent;
        mTopics = topics;
    }

    @Override
    public void onClick(View v) {
        Topic topic = (Topic) v.getTag();

        String topicName = topic.getTopicName();

        Context context = v.getContext();
        Intent intent = new Intent(context, SubforumActivity.class);

        intent.putExtra("TopicName", topicName);
        context.startActivity(intent);
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        public TextView topicName;

        public TopicViewHolder(View v) {
            super(v);
            topicName = v.findViewById(R.id.topicName);
        }
    }

    public TopicAdapter.TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_row, parent, false);
        return new TopicAdapter.TopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicAdapter.TopicViewHolder holder, int position) {
        Topic topic = mTopics.get(position);
        holder.topicName.setText(topic.getTopicName());
        holder.itemView.setTag(topic);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }


}
