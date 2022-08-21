package com.ahmadyosef.app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.fragments.AboutFragment;
import com.ahmadyosef.app.fragments.AdminFragment;
import com.ahmadyosef.app.fragments.CommonFragment;
import com.ahmadyosef.app.fragments.Todays;

public class FeedActivity extends AppCompatActivity {

    private FirebaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initialize();
        gotoFragment(R.id.miTodayFeed);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miTodayFeed:
                gotoFragment(R.id.miTodayFeed);
                return true;

                /*
            case R.id.miCommonFeed:
                gotoFragment(R.id.miCommonFeed);
                return true; */

            case R.id.miAboutFeed:
                gotoFragment(R.id.miAboutFeed);
                return true;

            case R.id.miSignoutFeed:
                fbs.getAuth().getInstance().signOut();
                gotoMainActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void gotoFragment(int frId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fr = null;
        switch (frId) {
            case R.id.miTodayFeed:
                fr = new Todays();
                break;
/*
            case R.id.miCommonFeed:
                fr = new CommonFragment();
                break; */

            case R.id.miAboutFeed:
                fr = new AboutFragment();
                break;
        }
        ft.replace(R.id.frameLayoutFeed, fr);
        ft.commit();
    }

    public void gotoMainActivity () {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}