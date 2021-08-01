package com.example.firstnationconnect;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private PostActivity mParentActivity;
    private List<ForumPost> mPosts;

    private final int REGULAR = 0, IMAGE = 1 , VIDEO = 2;

    public PostAdapter(PostActivity parent, List<ForumPost> posts) {
        mParentActivity = parent;
        mPosts = posts;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder { //implements View.OnCreateContextMenuListener {


        public TextView postContent, postUser;
        public CircleImageView postUserImage;



        public PostViewHolder(View v) {
            super(v);
            postContent = v.findViewById(R.id.tvPostContent);
            postUser = v.findViewById(R.id.postUser);
            postUserImage = v.findViewById(R.id.postProfileImage);

            //v.setOnCreateContextMenuListener(this);
        }


        /*@Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            //mParentActivity.super.onCreateContextMenu(menu, v, menuInfo);
            //MenuInflater inflater = getMenuInflater();
            //inflater.inflate(R.menu.post_menu, menu);

            menu.add(this.getAdapterPosition(), 1, 1, "Edit Post");
            menu.add(this.getAdapterPosition(), 2, 2, "Delete Post");
        }*/
    }

    public class ImagePostViewHolder extends RecyclerView.ViewHolder {


        public TextView postContent, postUser;
        public CircleImageView postUserImage;
        public ImageView postImage;


        public ImagePostViewHolder(View v) {
            super(v);
            postContent = v.findViewById(R.id.tvPostImageContent);
            postUser = v.findViewById(R.id.postUser);
            postUserImage = v.findViewById(R.id.postProfileImage);
            postImage = v.findViewById(R.id.ivPostImage);
        }
    }

    public class VideoPostViewHolder extends RecyclerView.ViewHolder {


        public TextView postContent, postUser;
        public CircleImageView postUserImage;


        public VideoPostViewHolder(View v) {
            super(v);
            postContent = v.findViewById(R.id.tvPostVideoContent);
            postUser = v.findViewById(R.id.postUser);
            postUserImage = v.findViewById(R.id.postProfileImage);
        }
    }

    //Adapted from: https://stackoverflow.com/questions/45883415/how-to-load-different-layouts-in-a-recycler-view-with-single-adapter
    @Override
    public int getItemViewType(int position) {
        System.out.println("Checkpoint 1");
        System.out.println(mPosts.get(position).getPostType());
        if (mPosts.get(position).getPostType().equals("Regular")) {
            System.out.println("REGULAR");
            return REGULAR;
        } else if (mPosts.get(position).getPostType().equals("Image")) {
            System.out.println("IMAGE");
            return IMAGE;
        } else if (mPosts.get(position).getPostType().equals("Video")) {
            System.out.println("VIDEO");
            return VIDEO;
        } else {
            return -1;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        //RecyclerView.ViewHolder viewHolder = new PostViewHolder(v);

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case REGULAR:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
                viewHolder = new PostAdapter.PostViewHolder(v1);
                break;
            case IMAGE:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image_row, parent, false);
                viewHolder = new PostAdapter.ImagePostViewHolder(v2);
                break;
            case VIDEO:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_video_row, parent, false);
                viewHolder = new PostAdapter.VideoPostViewHolder(v3);
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case REGULAR:
                PostViewHolder vh1 = (PostViewHolder) holder;
                configurePostViewHolder (vh1, position);
                break;
            case IMAGE:
                ImagePostViewHolder vh2 = (ImagePostViewHolder) holder;
                configureImagePostViewHolder (vh2, position);
                break;
            case VIDEO:
                VideoPostViewHolder vh3 = (VideoPostViewHolder) holder;
                configureVideoPostViewHolder(vh3, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void removePost(int position) {

    }

    public void editPost(int position) {

    }

    public void setDataset(List<ForumPost> posts) {
        mPosts = posts;
        notifyDataSetChanged();
    }

    private void configurePostViewHolder(PostViewHolder holder, int position) {
        ForumPost post = mPosts.get(position);

        holder.postContent.setText(post.getPostContent());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa dd-MM-yyyy");
        String postDate = dateFormat.format(post.getPostDate());

        holder.postUser.setText("By " + post.getPostUser() + " on " + postDate);
        holder.itemView.setTag(post);

        if (post.getUserImage() != null) {
            Uri postImageUri = Uri.parse(post.getUserImage());
            holder.postUserImage.setImageURI(postImageUri);
        }
    }

    private void configureImagePostViewHolder(ImagePostViewHolder holder, int position) {
        ForumPost post = mPosts.get(position);

        holder.postContent.setText(post.getPostContent());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa dd-MM-yyyy");
        String postDate = dateFormat.format(post.getPostDate());

        holder.postUser.setText("By " + post.getPostUser() + " on " + postDate);
        holder.itemView.setTag(post);

        if (post.getUserImage() != null) {
            Uri postImageUri = Uri.parse(post.getUserImage());
            holder.postUserImage.setImageURI(postImageUri);
        }

        if (post.getPostImage() != null) {
            Uri postImageUri = Uri.parse(post.getPostImage());
            holder.postImage.setImageURI(postImageUri);
        }
    }

    private void configureVideoPostViewHolder(VideoPostViewHolder holder, int position) {
        ForumPost post = mPosts.get(position);

        holder.postContent.setText(post.getPostContent());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa dd-MM-yyyy");
        String postDate = dateFormat.format(post.getPostDate());

        holder.postUser.setText("By " + post.getPostUser() + " on " + postDate);
        holder.itemView.setTag(post);

        if (post.getUserImage() != null) {
            Uri postImageUri = Uri.parse(post.getUserImage());
            holder.postUserImage.setImageURI(postImageUri);
        }
    }
}
