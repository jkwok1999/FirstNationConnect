package com.example.firstnationconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> { //implements View.OnClickListener {
    private PostActivity mParentActivity;
    private List<ForumPost> mPosts;

    public PostAdapter(PostActivity parent, List<ForumPost> posts) {
        mParentActivity = parent;
        mPosts = posts;
    }

    /*@Override
    public void onClick(View v) {
        ForumPost post = (ForumPost) v.getTag();

        String postName = post.getPostName();

        Context context = v.getContext();
        Intent intent = new Intent(context, PostActivity.class);

        intent.putExtra("PostName", postName);
        context.startActivity(intent);
    }*/

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView postContent, postUser;

        public PostViewHolder(View v) {
            super(v);
            postContent = v.findViewById(R.id.postContent);
            postUser = v.findViewById(R.id.postUser);
        }
    }

    public PostAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new PostAdapter.PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostAdapter.PostViewHolder holder, int position) {
        ForumPost post = mPosts.get(position);
        holder.postContent.setText(post.getPostContent());
        holder.postUser.setText("By " + post.getPostUser() + " at TIME");
        //holder.postTime.setText(post.getPostDate());
        holder.itemView.setTag(post);
        //holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


}
