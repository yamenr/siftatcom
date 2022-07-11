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
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("  Shiftatcom");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        initialize();
    }

    private void initialize() {
        fbs = FirebaseServices.getInstance();
        gotoFragment(R.id.miProfile);
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
                gotoFragment(R.id.miProfile);
                return true;

            case R.id.miAdmin:
                if (userIsAdmin())
                    gotoFragment(R.id.miAdmin);
                else
                    Toast.makeText(this, R.string.err_not_admin, Toast.LENGTH_SHORT).show();
                return true;
                
            case R.id.miSignout:
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
            case R.id.miProfile:
                fr = new Todays();
                break;

            case R.id.miAdmin:
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