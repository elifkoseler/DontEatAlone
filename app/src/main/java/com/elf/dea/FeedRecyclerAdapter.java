package com.elf.dea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycle_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.userEmailText.setText("");
        holder.commentText.setText("");

    }

    @Override
    public int getItemCount() { //recycleviewda kaç tane row olacak
        return 0;
    }

    class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userEmailText;
        TextView commentText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recycler_row_imageView);
            userEmailText = itemView.findViewById(R.id.recycler_row_useremail_text);
            commentText = itemView.findViewById(R.id.recycler_row_comment_text);



        }
    }
}

