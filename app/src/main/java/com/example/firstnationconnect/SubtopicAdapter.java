package com.example.firstnationconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SubtopicAdapter extends RecyclerView.Adapter<SubtopicAdapter.SubtopicViewHolder> implements View.OnClickListener {

    private SubforumActivity mParentActivity;
    private List<ForumPost> mPosts;

    public SubtopicAdapter(SubforumActivity parent, List<ForumPost> posts) {
        mParentActivity = parent;
        mPosts = posts;
    }

    @Override
    public void onClick(View v) {
        ForumPost post = (ForumPost) v.getTag();

        String postID = post.getPostID();
        String postTopic = post.getTopicName();
        String postName = post.getPostName();

        Context context = v.getContext();
        Intent intent = new Intent(context, PostActivity.class);

        //Potentially only pass one of these e.g. ID and query it from database
        intent.putExtra("PostID", postID);
        intent.putExtra("PostName", postName);
        intent.putExtra("TopicName", postTopic);

        context.startActivity(intent);
    }

    public class SubtopicViewHolder extends RecyclerView.ViewHolder {

        public TextView postName, postUser, tvLastReply;

        public SubtopicViewHolder(View v) {
            super(v);
            postName = v.findViewById(R.id.tvSubtopicName);
            postUser = v.findViewById(R.id.tvSubtopicUser);
            tvLastReply = v.findViewById(R.id.tvLastReply);
        }
    }

    public SubtopicAdapter.SubtopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subforum_row, parent, false);
        return new SubtopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubtopicAdapter.SubtopicViewHolder holder, int position) {
        ForumPost post = mPosts.get(position);
        holder.postName.setText(post.getPostName());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa dd-MM-yyyy");
        String postDate = dateFormat.format(post.getPostDate());

        holder.postUser.setText("By " + post.getPostUsername() + " at " + postDate);

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        if (post.getLastReply() != null) {
            DocumentReference docRef = firestoreDB.collection("Forum/" + post.getTopicName() + "/Subtopic/" + post.getPostID() + "/Replies").document(post.getLastReply());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ForumPost lastReply = documentSnapshot.toObject(ForumPost.class);

                    String lastReplyDate = dateFormat.format(lastReply.getPostDate());

                    holder.tvLastReply.setText("Last reply by " + lastReply.getPostUsername() + " on " + lastReplyDate);
                }
            });
        } else {
            holder.tvLastReply.setText("No replies yet");
        }


        holder.itemView.setTag(post);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

}
