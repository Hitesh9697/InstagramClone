package com.example.instagramclone;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class UserItemViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView textView;

    public UserItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.userItemImageView);
        textView = itemView.findViewById(R.id.textViewUserItemDescription);
    }
    public ImageView getImageView() {
        return imageView;
    }
    public TextView getTextView() {
        return textView;
    }

}
