package com.ahmadyosef.app.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.activities.AdminActivity;
import com.ahmadyosef.app.activities.FeedActivity;
import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.data.UserType;
import com.ahmadyosef.app.interfaces.AddUserToCompanyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    private EditText etName, etUsername, etPassword, etAddress, etPhone;
    private ImageView ivPhoto;
    private Spinner spUserType;
    private Button btnSignup;
    private Utilities utils;
    private FirebaseServices fbs;
    private static final String TAG = "SignupFragment";
    private Uri filePath;
    private StorageReference storageReference;
    private AddUserToCompanyCallback acall;
    private FirebaseOptions firebaseOptions;
    private FirebaseAuth mAuth2;

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

    @Override
    public void onResume() {
        super.onResume();
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
        spUserType = getView().findViewById(R.id.spUserTypeSignup);
        spUserType.setAdapter(new ArrayAdapter<UserType>(getActivity(), android.R.layout.simple_list_item_1, UserType.values()));
        spUserType.setSelection(0);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(view);
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
        acall = new AddUserToCompanyCallback() {
            @Override
            public void onCallback(Map<String, Company> companies, User user) {
                addUserToCompany(companies, user.getUsername());
            }
        };
        PrepareFirebaseAuth2();
    }

    private void PrepareFirebaseAuth2() {
        firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl(getResources().getString(R.string.dburl))
                .setApiKey(getResources().getString(R.string.apk))
                .setApplicationId(getResources().getString(R.string.apid)).build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getActivity(), firebaseOptions, "shiftatcom2");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("shiftatcom2"));
        }
    }

    public void signup(View view) {

        if (!checkFields())
        {
            return;
        }

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        // TODO: Add offline email password validation
        /*
        if (!utils.validateEmailPassword(username, password))
        {
            Toast.makeText(getActivity(), R.string.err_incorrect_user_password, Toast.LENGTH_SHORT).show();
            return;
        } */

        mAuth2.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signupInFirestore();
                            Toast.makeText(getActivity(), R.string.user_successfully_registered, Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private void gotoCommonFragment() {
        Fragment fragment = new CommonFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutAdmin, fragment);
        fragmentTransaction.addToBackStack(null);
        ((AdminActivity)getActivity()).setFr(fragment);
        fragmentTransaction.commit();
    }

    private void signupInFirestore() {
        String id = UUID.randomUUID().toString();
        String name = etName.getText().toString();
        String username = etUsername.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etName.getText().toString();
        String userType = spUserType.getSelectedItem().toString();
        /*
        if (userType.equals(String.valueOf(UserType.Regular)))
            userType = String.valueOf(UserType.Regular); */
        User user = new User(id, name, username, address, phone, "", UserType.valueOf(userType));
        //String id, String name, String username, String address, String phone, String photo, UserType type

        fbs.getFire().collection("users_")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        AddToCompany(user);
                        fbs.refreshUsersMap();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document", e);
                    }
                });
    }

    private void AddToCompany(User user) {
        getCompaniesMap(user);
    }

    private boolean checkFields() {
            if (etName.length() == 0        ||
                etUsername.length() == 0    ||
                etPassword.length() == 0    ||
                etAddress.length() == 0     ||
                etPhone.length() == 0)
            {
                etName.setError(getResources().getString(R.string.required_field));
                return false;
            }

            return true;
    }

    private void gotoFeedActivity() {
        Intent i = new Intent(getActivity(), FeedActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 40) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        filePath = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        ivPhoto.setBackground(null);
                        ivPhoto.setImageBitmap(bitmap);
                        uploadImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + fbs.getAuth().getCurrentUser().getDisplayName() + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    public void selectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"),40);
    }

    public Map<String, Company> getCompaniesMap(User user)
    {
        Map<String, Company> companies = new HashMap<>();

        try {
            companies.clear();
            fbs.getFire().collection("companies")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    companies.put(document.getId(), document.toObject(Company.class));
                                }
                                // TODO: add callback
                                acall.onCallback(companies, user);
                                //addUserToCompany(companies, user);
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Log.e("getCompaniesMap(): ", e.getMessage());
        }

        return companies;
    }

    private void addUserToCompany(Map<String, Company> companies, String username) {
        for(Map.Entry<String, Company> company: companies.entrySet())
        {
            if (company.getValue().getUsername().equals(fbs.getAuth().getCurrentUser().getEmail()))
            {
                fbs.setCompany(company.getValue());
                company.getValue().getUsers().add(username);
                fbs.getFire().collection("companies").
                        document(company.getKey()).
                        set(company.getValue()).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoCommonFragment();
                                Log.i("addUserToCompany: ", "User added successfully to company!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("addUserToCompany: ", "User failed to be added successfully in Firestore! " + e.getMessage());
                            }
                        });
            }
        }
    }
}