package com.ahmadyosef.app.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.adapters.ShiftAdapter;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
    private ShiftTypeCallback scall;
    private CalendarView cal;
    private static final String TAG = "TodaysFragment";
    private Spinner spShiftType;
    private ShiftType selectedShiftType;
    private LocalDate selectedDate;
    private Switch swMyOrAll;


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
        View rootView = inflater.inflate(R.layout.fragment_todays, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        cal = getView().findViewById(R.id.cvTodays);
        rv = getView().findViewById(R.id.rvShiftsTodays);
        swMyOrAll = getView().findViewById(R.id.swMyShiftOrAllTeamShiftsTodays);
        swMyOrAll.setText(R.string.my_shifts);
        fbs = FirebaseServices.getInstance();
        users = getUsers();
        swMyOrAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch sw = (Switch)view;
                if (sw.isChecked())
                {
                    filterAllTeamShifts();
                    swMyOrAll.setText(R.string.team_shifts);
                }
                else
                {
                    filterOnlyCurrentUserShifts();
                    swMyOrAll.setText(R.string.my_shifts);
                }
            }
        });
    }

    private void filterOnlyCurrentUserShifts() {
    }

    private void filterAllTeamShifts() {
    }

    private void setCallbacksAndHandlers() {
        ucall = new UsersCallback() {
            @Override
            public void onCallback(List<User> usersList) {
                rv = getView().findViewById(R.id.rvShiftsTodays);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                User user = findUsingIterator(fbs.getAuth().getCurrentUser().getEmail(), usersList);
                if (user != null)
                {
                    adapter = new ShiftAdapter(getContext(), user.getShifts());
                    rv.setAdapter(adapter);
                }
            }
        };

        scall = new ShiftTypeCallback() {
            @Override
            public void onCallback(ShiftType type) {
                FirebaseUser fbUser = fbs.getAuth().getCurrentUser();
                String uniqID = UUID.randomUUID().toString();
                ShiftRequest req = new ShiftRequest(fbUser.getEmail(), new Shift(uniqID, selectedDate.toString(), selectedShiftType));
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
        };

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month,
                                            int dayOfMonth) {
                for (Shift s : shifts)
                {
                    if ((LocalDate.of(year, month + 1, dayOfMonth)).toString().equals(s.getDate()))
                    {
                        Toast.makeText(getActivity(), "You already have a shift on this day!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                // TODO: add dialogue box prompt
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                showAlertDialogButtonClicked();
            }
        });
    }

    public void deSetCallbacksAndHandlers()
    {
        ucall = new UsersCallback() {
            @Override
            public void onCallback(List<User> usersList) {
            }
        };

        scall = new ShiftTypeCallback() {
            @Override
            public void onCallback(ShiftType type) {
            }
        };

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month,
                                            int dayOfMonth) {
            }
        });
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

    public void showAlertDialogButtonClicked()
    {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_shift_time);

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.dialogue_book_shift,
                        null);
        spShiftType = customLayout.findViewById(R.id.spUserAddEditShiftDialogue);
        spShiftType.setAdapter(new ArrayAdapter<ShiftType>(getActivity(), android.R.layout.simple_list_item_1, ShiftType.values()));
        builder.setView(customLayout);

        // add a button
        builder
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {

                                // send data from the
                                // AlertDialog to the Activity
                                sendDialogDataToActivity(
                                        spShiftType
                                                .getSelectedItem().toString()
                                                .toString());
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();

    }

    // Do something with the data
    // coming from the AlertDialog
    private void sendDialogDataToActivity(String data)
    {
        selectedShiftType = ShiftType.valueOf(data);
        scall.onCallback(selectedShiftType);
        refreshShiftsInList();
        Toast.makeText(getActivity(), getResources().getString(R.string.shift_request_sent), Toast.LENGTH_LONG).show();
    }

    private void refreshShiftsInList()
    {
        getUsers();
        User user = findUsingIterator(fbs.getAuth().getCurrentUser().getEmail(), users);

        /*
        for (User u : users
             ) {
            if (u.getUsername() == fbs.getAuth().getCurrentUser().getEmail())
                shifts = u.getShifts();
        } */
        if (user != null) {
            adapter = new ShiftAdapter(getContext(), shifts);
            rv.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deSetCallbacksAndHandlers();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        deSetCallbacksAndHandlers();
    }

    @Override
    public void onPause() {
        super.onPause();
        deSetCallbacksAndHandlers();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCallbacksAndHandlers();
    }
}