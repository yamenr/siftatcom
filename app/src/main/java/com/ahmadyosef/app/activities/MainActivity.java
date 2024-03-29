package com.ahmadyosef.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.fragments.LoginFragment;
import com.ahmadyosef.app.interfaces.CompaniesCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
 * Main Activity: Login, Signup portal
 * If user already logged in, user is directed to the regular employee activity (FeedActivity)
 * or to the administration activity (AdminActivity).
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseServices fbs;
    private ArrayList<Company> companies = new ArrayList<>();
    private CompaniesCallback ccall;

    /*
    * onCreate, onStart, initialize all prepare activity before being launched */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            getCompanies();
    }

    private void gotoFeedActivity() {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
        this.overridePendingTransition(0, 0);
    }

    private void initialize() {
        fbs = FirebaseServices.getInstance();
        // TODO: remove when done, for tests only
        //fbs.getAuth().signOut();
        //
        ccall = new CompaniesCallback() {
            @Override
            public void onCallback(List<Company> companies) {
                String user = fbs.getAuth().getCurrentUser().getEmail();

                for(Company c : companies)
                {
                    if (user.equals(c.getUsername()))
                    {
                        fbs.setCompany(c);
                        fbs.setSuperUser(true);
                        gotoAdminActivity();
                        return;
                    }
                }

                for(Company c : companies)
                {
                    if (c.getUsers().contains(user))
                    {
                        fbs.setCompany(c);
                        fbs.setSuperUser(false);
                        gotoFeedActivity();
                        return;
                    }
                }
            }
        };

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new LoginFragment());
        ft.commit();
    }

    private void gotoAdminActivity() {
        Intent i = new Intent(this, AdminActivity.class);
        startActivity(i);
    }

    /*
    * cGet companies data from firebase, set user company related properties
    * */
    public ArrayList<Company> getCompanies()
    {
        try {
            companies.clear();
            fbs.getFire().collection("companies")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    companies.add(document.toObject(Company.class));
                                }

                                ccall.onCallback(companies);

                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            //Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return companies;
    }
}