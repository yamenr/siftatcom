package com.ahmadyosef.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.fragments.LoginFragment;
import com.ahmadyosef.app.fragments.Todays;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getSupportActionBar().hide();
        initialize();
    }

    private void initialize() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutFeed, new Todays());
        ft.commit();
    }
}