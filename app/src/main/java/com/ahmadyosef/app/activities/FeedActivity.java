package com.ahmadyosef.app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.fragments.Todays;

public class FeedActivity extends AppCompatActivity {

    private FirebaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("  Shiftatcom");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        initialize();
    }

    private void initialize() {
        fbs = FirebaseServices.getInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutFeed, new Todays());
        ft.commit();
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
            case R.id.miProfile:
                return true;

            case R.id.miSignout:
                fbs.getAuth().getInstance().signOut();
                gotoMainActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void gotoMainActivity () {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
    }
}