package com.ahmadyosef.app.fragments;

import static com.ahmadyosef.app.CalendarUtils.daysInWeekArray;
import static com.ahmadyosef.app.CalendarUtils.monthYearFromDate;
import static com.ahmadyosef.app.CalendarUtils.selectedDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadyosef.app.CalendarUtils;
import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.adapters.CommonAdapter;
import com.ahmadyosef.app.adapters.ShiftUserAdapter;
import com.ahmadyosef.app.adapters.UserAdapter;
import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.ShiftUser;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.interfaces.UsersCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommonFragment extends Fragment  implements CommonAdapter.OnItemListener {

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
    private ListView shiftListView;
    private Button btnAddShift;
    private UsersCallback ucall;
    private Button btnPrevious, btnNext;
    private static final String TAG = "CommonFragment";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommonFragment newInstance(String param1, String param2) {
        CommonFragment fragment = new CommonFragment();
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
        return inflater.inflate(R.layout.fragment_common, container, false);
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

        CommonAdapter calendarAdapter = new CommonAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        shifts = prepareShiftsListOrder(users);
        setShiftsUsersAdpater();
    }

    private void initWidgets() {
        btnPrevious = getActivity().findViewById(R.id.btnPreviousCommonFragment);
        btnNext = getActivity().findViewById(R.id.btnNextCommonFragment);
        calendarRecyclerView = getActivity().findViewById(R.id.calendarRecyclerView);
        monthYearText = getActivity().findViewById(R.id.monthYearTV);
        shiftListView = getActivity().findViewById(R.id.shiftListView);
        btnAddShift = getActivity().findViewById(R.id.btnAddShiftCommon);
        btnAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShiftAddDialog();
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
    }

    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //setShiftsUsersAdpater();
    }

    private void setShiftsUsersAdpater()
    {
        // TODO: set adapter for shifts
        ShiftUserAdapter shiftUserAdapter = new ShiftUserAdapter(getActivity(), shifts);
        shiftListView.setAdapter(shiftUserAdapter);
        shiftListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: show popup are you sure
                showAlertDialogDeleteItem(i);
                return false;
            }
        });
        /*
        shiftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showShiftEditDialog(i);
            }
        }); */

    }

    private void removeItem(int i) {
        fbs.removeShiftFromUser2(shifts.get(i));
        shifts.remove(i);
        refreshCommon();
    }

    private void refreshCommon()
    {
        users = getUsers();
        ((ShiftUserAdapter)(shiftListView.getAdapter())).notifyDataSetChanged();
    }

    private void showShiftEditDialog(int i) {
        Spinner spShift, spUsers;
        CalendarView cal;

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.edit_user_shift_dialogue);

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
                curDate[0] = LocalDate.of(year, month, day);
            }
        });
        ShiftUser shift = shifts.get(i);
        LocalDate date = utils.convertLocalDate(shift.getDate());
        cal.setDate(utils.getMilliSecsForCalendar(date), true, true);
        spShift.setAdapter(new ArrayAdapter<ShiftType>(getActivity(), android.R.layout.simple_list_item_1, ShiftType.values()));
        ArrayList<String> userShifts = utils.usersList(users);
        spUsers.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userShifts));
        spUsers.setSelection(userShifts.indexOf(shift.getUsername()));
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
                                sendDialogDataToActivity(spUsers.getSelectedItem().toString(),
                                        spShift.getSelectedItem().toString(), curDate[0].toString(), i);
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    private void sendDialogDataToActivity(String user, String shiftType, String date, int i)
    {
        if (shiftChanges(user, shiftType, date, i))
            updateShift(user, shiftType, date, i);
        else
            Toast.makeText(getActivity(), R.string.no_changes_made_to_shift, Toast.LENGTH_LONG).show();
    }

    private boolean shiftChanges(String user, String shiftType, String date, int i) {
        ShiftUser shift = shifts.get(i);
        if (!shift.getUsername().equals(user) || !shift.getDate().equals(date) || !shift.getType().equals(shiftType))
            return true;
        return false;
    }

    private void updateShift(String user, String shiftType, String date, int i) {
        // TODO: if user changes, add new shift to user, remove from current
        if (!shifts.get(i).getUsername().equals(user))
        {
            ShiftUser shiftsOld = shifts.get(i);
            removeItem(i);
            // TODO: check process
            ShiftUser newShift = new ShiftUser(user, date, ShiftType.valueOf(shiftType));
            fbs.addShiftToUser(newShift);
            removeItem(i);
        }
        else
        {
            shifts.get(i).setDate(date);
            shifts.get(i).setType(ShiftType.valueOf(shiftType));
            users = getUsers();
            ((ShiftUserAdapter)(shiftListView.getAdapter())).notifyDataSetChanged();
        }
    }

    private void showShiftAddDialog() {
        Spinner spShift, spUsers;
        CalendarView cal;

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.edit_user_shift_dialogue);

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
                curDate[0] = LocalDate.of(year, month, day);
            }
        });
        //ShiftUser shift = shifts.get(i);
        //LocalDate date = utils.convertLocalDate(shift.getDate());
        cal.setDate(utils.getMilliSecsForCalendar(curDate[0]), true, true);
        spShift.setAdapter(new ArrayAdapter<ShiftType>(getActivity(), android.R.layout.simple_list_item_1, ShiftType.values()));
        ArrayList<String> userShifts = utils.usersList(users);
        spUsers.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userShifts));
        /*
        TextView errorText = (TextView)spUsers.getSelectedView();
        try {
            errorText.setError(getResources().getString(R.string.must_choose_user), getResources().getDrawable(R.drawable.stop));
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(R.string.must_choose_user);
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        } */
        //spUsers.setSelection(userShifts.indexOf(shift.getUsername()));
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
                                fbs.addShiftToUser(newShiftUser);
                                refreshCommon();
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    private void sendAddDialogDataToActivity(String user, String shiftType, String date)
    {

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

    public void showAlertDialogDeleteItem(int position)
    {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.are_you_sure_delete);
        final View customLayout
                = getActivity().getLayoutInflater()
                .inflate(
                        R.layout.dialogue_are_you_sure_delete,
                        null);
        builder.setView(customLayout);
        builder
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {
                                removeItem(position);
                            }
                        });

        AlertDialog dialog
                = builder.create();
        dialog.show();
    }
}