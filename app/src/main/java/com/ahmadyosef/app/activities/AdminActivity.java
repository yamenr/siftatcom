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

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.fragments.AdminFragment;
import com.ahmadyosef.app.fragments.CommonFragment;
import com.ahmadyosef.app.fragments.SignupFragment;
import com.ahmadyosef.app.fragments.UsersListFragment;

public class AdminActivity extends AppCompatActivity {

    private FirebaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initialize();
        gotoFragment(R.id.miCommonAdm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miAddAdm:
                gotoFragment(R.id.miAddAdm);
                return true;

            case R.id.miCommonAdm:
                gotoFragment(R.id.miCommonAdm);
                return true;

            case R.id.miRequestsAdm:
                gotoFragment(R.id.miRequestsAdm);
                return true;

            case R.id.miUsersListAdm:
                gotoFragment(R.id.miUsersListAdm);
                return true;

            case R.id.miSignoutAdm:
                fbs.getAuth().getInstance().signOut();
                gotoMainActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void gotoFragment(int frId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fr = null;
        switch (frId) {
            case R.id.miAddAdm:
                fr = new SignupFragment();
                break;

            case R.id.miCommonAdm:
                fr = new CommonFragment();
                break;

            case R.id.miRequestsAdm:
                fr = new AdminFragment();
                break;

            case R.id.miUsersListAdm:
                fr = new UsersListFragment();
                break;
        }
        ft.replace(R.id.frameLayoutAdmin, fr);
        ft.commit();
    }

    public void gotoMainActivity () {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}