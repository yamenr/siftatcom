package com.ahmadyosef.app.fragments;

import static com.ahmadyosef.app.CalendarUtils.daysInWeekArray;
import static com.ahmadyosef.app.CalendarUtils.monthYearFromDate;
import static com.ahmadyosef.app.CalendarUtils.selectedDate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.ahmadyosef.app.CalendarUtils;
import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.adapters.CommonAdapter;
import com.ahmadyosef.app.adapters.CommonUserAdapter;
import com.ahmadyosef.app.adapters.ShiftUserAdapter;
import com.ahmadyosef.app.adapters.ShiftsTeamAdapter;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.ShiftUser;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.interfaces.UsersCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
Fragment for testing: not used in project
 */
public class CommonUserFragment extends Fragment implements CommonUserAdapter.OnItemListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<ShiftUser> shifts;
    private FirebaseServices fbs;
    private Utilities utils;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView shiftUserTeamRecycler;
    private Button btnSubmitRequest;
    private FloatingActionButton flbtnSubmitRequest;
    private UsersCallback ucall;
    private Button btnPrevious, btnNext;
    private static final String TAG = "CommonUserFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommonUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommonUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommonUserFragment newInstance(String param1, String param2) {
        CommonUserFragment fragment = new CommonUserFragment();
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
        return inflater.inflate(R.layout.fragment_common_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initWidgets();
        initialize();
        setWeekView();
    }

    private void initialize() {
        fbs = FirebaseServices.getInstance();
        utils = Utilities.getInstance();
        users = getUsers();
        ucall = new UsersCallback() {
            @Override
            public void onCallback(List<User> usersList) {
                shifts = prepareShiftsListOrder(users);
                setShiftsUsersAdpater();
            }
        };
        CalendarUtils.selectedDate = LocalDate.now();
    }

    // TODO: move to FirebaseServices/Utilities
    private ArrayList<ShiftUser> prepareShiftsListOrder(ArrayList<User> users) {
        shifts = new ArrayList<>();

        for(User user: users)
        {
            for(Shift shift: user.getShifts())
            {
                if (shift.getDate().equals(CalendarUtils.selectedDate.toString()))
                    shifts.add(new ShiftUser(user.getUsername(), shift.getDate(), shift.getType()));
            }
        }

        return shifts;
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CommonUserAdapter calendarAdapter = new CommonUserAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        //getUsers();
        shifts = prepareShiftsListOrder(users);
        setShiftsUsersAdpater();
    }

    private void initWidgets() {
        btnPrevious = getActivity().findViewById(R.id.btnPreviousCommonUserFragment);
        btnNext = getActivity().findViewById(R.id.btnNextCommonUserFragment);
        calendarRecyclerView = getActivity().findViewById(R.id.calendarRecyclerViewUser);
        monthYearText = getActivity().findViewById(R.id.monthYearUserTV);
        shiftUserTeamRecycler = getActivity().findViewById(R.id.shiftUserTeamRecycler);
        btnSubmitRequest = getActivity().findViewById(R.id.btnRequestShiftCommonUser);
        flbtnSubmitRequest = getActivity().findViewById(R.id.fltbtnShiftRequestUser);
        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubmitRequestDialog();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });
        flbtnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubmitRequestDialog();
            }
        });
    }

    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        //getUsers();
        setWeekView();
    }

    private void setShiftsUsersAdpater()
    {
        // TODO: set adapter for shifts
        ShiftsTeamAdapter shiftTeamAdapter = new ShiftsTeamAdapter(getActivity(), shifts);
        shiftUserTeamRecycler.setAdapter(shiftTeamAdapter);
    }

    public void refreshCommon()
    {
        users = getUsers();
        ((ShiftsTeamAdapter)(shiftUserTeamRecycler.getAdapter())).notifyDataSetChanged();
    }

    private void showSubmitRequestDialog() {
        if (users.isEmpty())
        {
            Toast.makeText(getContext(), R.string.no_users_add_new_one, Toast.LENGTH_LONG).show();
            return;
        }

        Spinner spShift, spUsers;
        CalendarView cal;

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_user_shift_dialogue);

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.dialogue_edit_shift,
                        null);
        spShift = customLayout.findViewById(R.id.spShiftTypeAddEditShiftDialogue);
        spUsers = customLayout.findViewById(R.id.spUserAddEditShiftDialogue);
        cal = customLayout.findViewById(R.id.calAddEditShiftDialogue);
        final LocalDate[] curDate = {selectedDate};
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                curDate[0] = LocalDate.of(year, month + 1, day);
            }
        });
        cal.setDate(utils.getMilliSecsForCalendar(curDate[0]), true, true);
        spShift.setAdapter(new ArrayAdapter<ShiftType>(getActivity(), android.R.layout.simple_list_item_1, ShiftType.values()));
        ArrayList<String> userShifts = utils.usersList(users);
        spUsers.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userShifts));

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
                                ShiftUser newShiftUser = new ShiftUser(spUsers.getSelectedItem().toString(), curDate[0].toString(),
                                        ShiftType.valueOf(spShift.getSelectedItem().toString()));
                                refreshCommon();
                                fbs.addShiftToUser(newShiftUser);
                            }
                        });
        builder
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {
                                dialog.cancel();
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    public ArrayList<ShiftUser> getLastUsershifts()
    {
        return this.shifts;
    }

    public ArrayList<User> getLastUsersList()
    {
        return this.users;
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
                                Log.e(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }

        return users;
    }
}