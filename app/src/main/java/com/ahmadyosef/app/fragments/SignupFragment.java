package com.ahmadyosef.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.activities.FeedActivity;
import com.ahmadyosef.app.data.FirebaseServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    private EditText etName, etUsername, etPassword, etAddress, etPhone;
    private ImageView ivPhoto;
    private Button btnSignup;
    private Utilities utils;
    private FirebaseServices fbs;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        etName = getView().findViewById(R.id.etNameSignup);
        etUsername = getView().findViewById(R.id.etUsernameSignup);
        etPassword = getView().findViewById(R.id.etPasswordSignup);
        etAddress = getView().findViewById(R.id.etAddressSignup);
        etPhone = getView().findViewById(R.id.etPhoneSignup);
        ivPhoto = getView().findViewById(R.id.ivProfilePhotoSignup);
        btnSignup = getView().findViewById(R.id.btnSignupSignup);
        utils = Utilities.getInstance();
        fbs = FirebaseServices.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(view);
            }
        });
    }

    public void signup(View view) {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.trim().isEmpty() || password.trim().isEmpty())
        {
         //   Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.validateEmail(username) || !utils.validatePassword(password))
        {
           // Toast.makeText(this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            return;
        }

        fbs.getAuth().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // TODO: get data from screen, create User object, if successfull goto feed
                            gotoFeedActivity();
                        } else {

                            //Toast.makeText(getv, R.string.err_firebase_general, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void gotoFeedActivity() {
        Intent i = new Intent(getActivity(), FeedActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }
}