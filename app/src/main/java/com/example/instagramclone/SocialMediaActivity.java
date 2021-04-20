package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.view.View;

import com.example.instagramclone.databinding.ActivitySocialMediaBinding;

public class SocialMediaActivity extends AppCompatActivity {

    private ActivitySocialMediaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocialMediaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(tabAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager, true);

    }
}