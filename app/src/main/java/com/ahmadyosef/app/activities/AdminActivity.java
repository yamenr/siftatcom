package com.ahmadyosef.app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.FirebaseServices;

public class AdminActivity extends AppCompatActivity {

    private FirebaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initialize();
    }

    private void initialize() {
        fbs = FirebaseServices.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar_layout);
        TextView tvCurrentUser = findViewById(R.id.tvCurrentUserBar);
        tvCurrentUser.setText(fbs.getAuth().getCurrentUser().getEmail());
    }
}