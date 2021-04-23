package com.example.instagramclone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserItemRecyclerViewAdapter extends RecyclerView.Adapter<UserItemViewHolder> {
    private String username;
    private List<ParseObject> parseObjects =new ArrayList<>();

    public UserItemRecyclerViewAdapter(List<ParseObject> objects) {

        parseObjects = objects;

    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_items, parent,  false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
       holder.getTextView().setText(parseObjects.get(position).get("description") + "");
        ParseFile parseFile = (ParseFile) parseObjects.get(position).get("picture");
        parseFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (data != null && e == null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    holder.getImageView().setImageBitmap(bitmap);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }
}
