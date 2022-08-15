package com.ahmadyosef.app.fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.activities.FeedActivity;
import com.ahmadyosef.app.adapters.RequestAdapter;
import com.ahmadyosef.app.adapters.ShiftAdapter;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.interfaces.RequestsCallback;
import com.ahmadyosef.app.interfaces.ShiftTypeCallback;
import com.ahmadyosef.app.interfaces.UsersCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    private RecyclerView rv;
    private FirebaseServices fbs;
    private RequestAdapter adapter;
    private ArrayList<ShiftRequest> requests = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private UsersCallback ucall;
    private RequestsCallback rcall;
    private TextView tvApprove;
    private static final String TAG = "AdminFragment";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            ((FeedActivity) getActivity()).gotoFragment(R.id.miCommonFeed);
        }
    }

    private void initialize() {
        rv = getView().findViewById(R.id.rvRequestsAdmin);
        fbs = FirebaseServices.getInstance();
        requests = getRequests();

        rcall = new RequestsCallback() {
            @Override
            public void onCallback(List<ShiftRequest> requests) {
                rv = getView().findViewById(R.id.rvRequestsAdmin);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new RequestAdapter(getContext(), requests);
                rv.setAdapter(adapter);
            }
        };
    }

    public ArrayList<ShiftRequest> getRequests()
    {
        try {
            fbs.getFire().collection("requests")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    requests.add(document.toObject(ShiftRequest.class));
                                }

                                rcall.onCallback(requests);
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return requests;
    }
}