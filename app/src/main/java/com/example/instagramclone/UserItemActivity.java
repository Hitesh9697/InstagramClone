package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.instagramclone.databinding.ActivityUserItemBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserItemActivity extends AppCompatActivity {

    private ActivityUserItemBinding binding;
    private UserItemRecyclerViewAdapter recyclerViewAdapter;
    private Intent intent;
    private String user;
    private List<ParseObject> parseObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        intent = getIntent();
        user = intent.getStringExtra("username");
        setTitle(user);
        parseObjects = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
        query.whereEqualTo("username", user);
        //query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    for (ParseObject post : objects) {
                        parseObjects.add(post);
                    }
                    recyclerViewAdapter = new UserItemRecyclerViewAdapter(parseObjects);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(UserItemActivity.this));
                    binding.recyclerView.setAdapter(recyclerViewAdapter);
                    Log.i("qwerty", "Retrieved " + parseObjects + " scores");
                    Log.i("qwerty", "Retrieved " + objects.size() + " scores");
                } else {
                    Log.i("qwerty", "Error: " + e.getMessage());
                }
            }
        });


    }
}