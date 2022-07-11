package com.ahmadyosef.app.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.ahmadyosef.app.fragments.BlankFragment;
import com.ahmadyosef.app.fragments.TestFragment;
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
        gotoTestFragment();
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
                gotoTodaysFragment();
                return true;

            case R.id.miTest:
                gotoTestFragment();
                return true;

            case R.id.miBlank:
                gotoBlankFragment();
                return true;

            case R.id.miAdmin:
                if (userIsAdmin())
                    gotoAdminFragment();
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

    private void gotoTodaysFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutFeed, new Todays());
        ft.commit();
    }

    private void gotoAdminFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutFeed, new AdminFragment());
        ft.commit();
    }

    private void gotoTestFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutFeed, new TestFragment());
        ft.commit();
    }

    private void gotoBlankFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutFeed, new BlankFragment());
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