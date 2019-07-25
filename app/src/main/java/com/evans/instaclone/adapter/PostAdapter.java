package com.evans.instaclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evans.instaclone.R;
import com.evans.instaclone.models.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    TextView username;
    FirebaseUser mUser;
    DatabaseReference mReference;
    private ArrayList<Posts> mPosts;

    public PostAdapter(Context context, ArrayList<Posts> posts) {
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Posts post = mPosts.get(position);

        holder.mDescription.setText(post.getDescription());
        Glide.with(mContext).load(post.getImageUrl()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView mUsername, mAuthor, mDescription;
        ImageView mImageView;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.post_username);
            mAuthor = itemView.findViewById(R.id.postUsername);
            mDescription = itemView.findViewById(R.id.postDescription);
            mImageView = itemView.findViewById(R.id.postImage);

            mUser = FirebaseAuth.getInstance().getCurrentUser();
            assert mUser != null;
            String userId = mUser.getUid();

            mReference = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId);
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(mUser.getUid()).exists()){
                        String userName = dataSnapshot.child("username").toString();
                        mUsername.setText(userName);
                        mAuthor.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

}