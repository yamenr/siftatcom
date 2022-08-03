package com.ahmadyosef.app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.adapters.ShiftAdapter;
import com.ahmadyosef.app.adapters.UserAdapter;
import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.interfaces.UsersCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<User> users = new ArrayList<>();
    private FirebaseServices fbs;
    private UsersCallback ucall;
    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private static final String TAG = "UsersListFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersListFragment newInstance(String param1, String param2) {
        UsersListFragment fragment = new UsersListFragment();
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
        return inflater.inflate(R.layout.fragment_users_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        fbs = FirebaseServices.getInstance();
        rvUsers = getView().findViewById(R.id.rvUsersUsersFragment);
        ucall = new UsersCallback() {
            @Override
            public void onCallback(List<User> usersList) {
                rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new UserAdapter(getContext(), usersList);
                rvUsers.setAdapter(adapter);
            }
        };
        getUsers();
    }

    public ArrayList<User> getUsers()
    {
        try {
            users.clear();
            fbs.getFire().collection("users_")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    if (fbs.getCompany().getUsers().contains(user.getUsername()))
                                        users.add(document.toObject(User.class));
                                }

                                ucall.onCallback(users);

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

        return users;
    }
}