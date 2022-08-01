package com.ahmadyosef.app.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
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
import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.FirebaseServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanySignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanySignupFragment extends Fragment {
    private EditText etName, etUsername, etPassword, etAddress, etPhone;
    private ImageView ivPhoto;
    private Button btnSignup;
    private Utilities utils;
    private FirebaseServices fbs;
    private static final String TAG = "CompanySignupFragment";
    private Uri filePath;
    private StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompanySignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanySignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanySignupFragment newInstance(String param1, String param2) {
        CompanySignupFragment fragment = new CompanySignupFragment();
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
        return inflater.inflate(R.layout.fragment_company_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        etName = getView().findViewById(R.id.etNameSignup2);
        etUsername = getView().findViewById(R.id.etUsernameSignup2);
        etPassword = getView().findViewById(R.id.etPasswordSignup2);
        etAddress = getView().findViewById(R.id.etAddressSignup2);
        etPhone = getView().findViewById(R.id.etPhoneSignup2);
        ivPhoto = getView().findViewById(R.id.ivProfilePhotoSignup2);
        btnSignup = getView().findViewById(R.id.btnSignupSignup2);
        utils = Utilities.getInstance();
        fbs = FirebaseServices.getInstance();
        //spUserType = getView().findViewById(R.id.spUserTypeSignup);
        //spUserType.setAdapter(new ArrayAdapter<UserType>(getActivity(), android.R.layout.simple_list_item_1, UserType.values()));
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

        fbs.getAuth().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signupInFirestore();
                            gotoFeedActivity();
                        } else {
                            Log.e(TAG, task.getException().getMessage());
                            //Toast.makeText(getv, R.string.err_firebase_general, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signupInFirestore() {
        String id = UUID.randomUUID().toString();
        String name = etName.getText().toString();
        String username = etUsername.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etName.getText().toString();
        Company company = new Company(id, name, username, address, phone, "");
        //String id, String name, String username, String address, String phone, String photo

        fbs.getFire().collection("companies")
                .add(company)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private boolean checkFields() {
        if (etName.length() == 0)
        {
            etName.setError(getResources().getString(R.string.required_field));
            return false;
        }

        if (etUsername.length() == 0)
        {
            etUsername.setError(getResources().getString(R.string.required_field));
            return false;
        }

        if (etPassword.length() == 0)
        {
            etPassword.setError(getResources().getString(R.string.required_field));
            return false;
        }

        if (etAddress.length() == 0)
        {
            etAddress.setError(getResources().getString(R.string.required_field));
            return false;
        }

        if (etPhone.length() == 0)
        {
            etPhone.setError(getResources().getString(R.string.required_field));
            return false;
        }

        return true;
    }

    private void gotoFeedActivity() {
        Intent i = new Intent(getActivity(), FeedActivity.class);
        i.putExtra("userType", "Company");
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
}