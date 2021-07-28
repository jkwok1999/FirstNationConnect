package com.example.firstnationconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> implements View.OnClickListener {
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

        public TextView topicName, tvNumPost;
        public ImageView topicImage;
        public ProgressBar pbTopic;

        public TopicViewHolder(View v) {
            super(v);
            topicName = v.findViewById(R.id.tvResourceName);
            tvNumPost = v.findViewById(R.id.tvNumPost);
            topicImage = v.findViewById(R.id.ivForumPic);
            pbTopic = v.findViewById(R.id.pbTopic);
        }
    }

    public TopicAdapter.TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_row, parent, false);
        return new TopicAdapter.TopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        Topic topic = mTopics.get(position);
        holder.topicName.setText(topic.getTopicName());

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("Forum/" + topic.getTopicName() + "/Subtopic")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int postCount = task.getResult().size();
                            holder.tvNumPost.setText(String.valueOf(postCount));
                        }
                    }
                });

        StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                .child("forum_pics")
                .child(topic.getTopicName() + ".png");
        imageRef.getDownloadUrl().
                addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(holder.topicImage);
                        holder.pbTopic.setVisibility(View.INVISIBLE);
                    }
                });


        holder.itemView.setTag(topic);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }


}
