package com.ahmadyosef.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.activities.AdminActivity;
import com.ahmadyosef.app.activities.FeedActivity;
import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.interfaces.CompaniesCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 Login fragment, logins user according to his type (company or regular employee)
 */
public class LoginFragment extends Fragment {

    private EditText etUsername,etPassword;
    private TextView tvSignupLink;
    private Button btnLogin;
    private ImageView ivLogo;
    private FirebaseServices fbs;
    private ArrayList<Company> companies = new ArrayList<>();
    private CompaniesCallback ccall;
    private static final String TAG = "LoginFragment";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
        //PickUserTypeActivity();
    }

    private void PickUserTypeActivity() {
        FirebaseUser currentUser = fbs.getAuth().getCurrentUser();
        if (currentUser != null)
        {
            getCompanies();
        }
    }

    private void initialize() {
        etUsername = getView().findViewById(R.id.etUsernameLogin);
        etPassword = getView().findViewById(R.id.etPasswordLogin);
        btnLogin = getView().findViewById(R.id.btnLoginLogin);
        tvSignupLink = getView().findViewById(R.id.lnkSignupLogin);
        ivLogo = getView().findViewById(R.id.imgLogoLogin);
        ivLogo.setImageResource(R.drawable.logo);
        fbs = FirebaseServices.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
        tvSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignup();
            }
        });
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
                        gotoAdminActivity(); // super user
                        return;
                    }
                }

                for(Company c : companies)
                {
                    if (c.getUsers().contains(user))
                    {
                        fbs.setCompany(c);
                        fbs.setSuperUser(false);
                        gotoFeedActivity(); // regular user
                        return;
                    }
                }
            }
        };
    }

    private void gotoSignup() {
        //Fragment fragment = new SignupFragment();
        Fragment fragment = new CompanySignupFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void login(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.trim().isEmpty() || password.trim().isEmpty())
        {
            Toast.makeText(getActivity(), R.string.err_fields_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            fbs.getAuth().signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                PickUserTypeActivity();
                            } else {
                                Log.e(TAG, task.getException().getMessage());
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoAdminActivity() {
        Intent i = new Intent(getActivity(), AdminActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
        /*Intent i = new Intent(getActivity(), AdminActivity.class);
        startActivity(i);*/
    }

    private void gotoFeedActivity () {
        Intent i = new Intent(getActivity(), FeedActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

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