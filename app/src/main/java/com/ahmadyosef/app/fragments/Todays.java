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
import android.widget.CalendarView;
import android.widget.Toast;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.adapters.ShiftAdapter;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.interfaces.UsersCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Todays#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Todays extends Fragment {

    private RecyclerView rv;
    private FirebaseServices fbs;
    private ShiftAdapter adapter;
    private ArrayList<Shift> shifts = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private UsersCallback ucall;
    private CalendarView cal;
    private static final String TAG = "TodaysFragment";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Todays() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Todays.
     */
    // TODO: Rename and change types and number of parameters
    public static Todays newInstance(String param1, String param2) {
        Todays fragment = new Todays();
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
        return inflater.inflate(R.layout.fragment_todays, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        cal = getView().findViewById(R.id.cvTodays);
        rv = getView().findViewById(R.id.rvShiftsTodays);
        fbs = FirebaseServices.getInstance();
        ucall = new UsersCallback() {
            @Override
            public void onCallback(List<User> usersList) {
                RecyclerView recyclerView = getView().findViewById(R.id.rvShiftsTodays);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                User user = findUsingIterator(fbs.getAuth().getCurrentUser().getEmail(), getUsers());
                if (user != null)
                {
                    adapter = new ShiftAdapter(getContext(), user.getShifts());
                    recyclerView.setAdapter(adapter);
                }
            }
        };
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month,
                                            int dayOfMonth) {
                for (Shift s : shifts)
                {
                    if ((new Date(year, month, dayOfMonth)).toString().equals(s.getDate()))
                    {
                        Toast.makeText(getActivity(), "You already have a shift on this day!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                // TODO: add dialogue box prompt
                fileShiftRequest(year, month, dayOfMonth);
            }
        });
    }

    private void fileShiftRequest(int year, int month, int dayOfMonth) {
        FirebaseUser fbUser = fbs.getAuth().getCurrentUser();
        ShiftRequest req = new ShiftRequest(fbUser.getEmail(), year, month, dayOfMonth);
        fbs.getFire().collection("requests")
                .add(req)
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

    public ArrayList<User> getUsers()
    {
        try {
            fbs.getFire().collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
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

    public User findUsingIterator(String name, List<User> users) {
            Iterator<User> iterator = users.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if (user.getUsername().equals(name)) {
                    return user;
                }
        }
        return null;
    }
}