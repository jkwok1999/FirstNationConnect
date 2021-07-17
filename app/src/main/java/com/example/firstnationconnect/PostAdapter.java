package com.example.firstnationconnect;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView postContent, postUser;

        public PostViewHolder(View v) {
            super(v);
            postContent = v.findViewById(R.id.postContent);
            postUser = v.findViewById(R.id.postUser);

            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            //mParentActivity.super.onCreateContextMenu(menu, v, menuInfo);
            //MenuInflater inflater = getMenuInflater();
            //inflater.inflate(R.menu.post_menu, menu);

            menu.add(this.getAdapterPosition(),1,1,"Edit Post");
            menu.add(this.getAdapterPosition(),2,2,"Delete Post");
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

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa dd-MM-yyyy");
        String postDate = dateFormat.format(post.getPostDate());

        holder.postUser.setText("By " + post.getPostUser() + " at " + postDate);
        holder.itemView.setTag(post);
        //holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void removePost(int position) {

    }

}