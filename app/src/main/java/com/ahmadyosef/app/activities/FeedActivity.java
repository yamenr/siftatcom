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
import com.ahmadyosef.app.fragments.AdminFragment;
import com.ahmadyosef.app.fragments.Todays;

public class FeedActivity extends AppCompatActivity {

    private FirebaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initialize();
        gotoFragment(R.id.miProfileFeed);
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
            case R.id.miProfileFeed:
                gotoFragment(R.id.miProfileFeed);
                return true;

            case R.id.miCommonFeed:
                if (userIsAdmin())
                    gotoFragment(R.id.miCommonFeed);
                else
                    Toast.makeText(this, R.string.err_not_admin, Toast.LENGTH_SHORT).show();
                return true;
                
            case R.id.miSignoutFeed:
                fbs.getAuth().getInstance().signOut();
                gotoMainActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoFragment(int frId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fr = null;
        switch (frId) {
            case R.id.miProfileFeed:
                fr = new Todays();
                break;

            case R.id.miCommonFeed:
                if (userIsAdmin())
                    fr = new AdminFragment();
                else {
                    Toast.makeText(this, R.string.err_not_admin, Toast.LENGTH_SHORT).show();
                    return;
                }
        }
        ft.replace(R.id.frameLayoutFeed, fr);
        ft.commit();
    }

    private boolean userIsAdmin() {
        // TODO: Add check user admin
        return true;
    }

    public void gotoMainActivity () {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}