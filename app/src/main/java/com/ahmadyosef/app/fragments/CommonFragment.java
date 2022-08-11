package com.ahmadyosef.app.fragments;

import static com.ahmadyosef.app.CalendarUtils.daysInWeekArray;
import static com.ahmadyosef.app.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ahmadyosef.app.CalendarUtils;
import com.ahmadyosef.app.R;
import com.ahmadyosef.app.adapters.CommonAdapter;
import com.ahmadyosef.app.adapters.ShiftUserAdapter;
import com.ahmadyosef.app.data.Company;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftUser;
import com.ahmadyosef.app.data.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

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
    private Map<String, User> users;
    private ArrayList<ShiftUser> shifts;
    private FirebaseServices fbs;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView shiftListView;


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
        fbs = FirebaseServices.getInstance();
        users = fbs.getUsersMapByCompany();
        shifts = prepareShiftsListOrder(users);
        initWidgets();
        setWeekView();
    }

    // TODO: move to FirebaseServices/Utilities
    private ArrayList<ShiftUser> prepareShiftsListOrder(Map<String, User> users) {
        shifts = new ArrayList<>();

        for(User user: users.values())
        {
            for(Shift shift: user.getShifts())
            {
                shifts.add(new ShiftUser(user.getUsername(), shift.getDate(), shift.getType()));
            }
        }

        return shifts;
    }

    private void setWeekView() {
        CalendarUtils.selectedDate = LocalDate.now();
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CommonAdapter calendarAdapter = new CommonAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setShiftsUsersAdpater();
    }

    private void initWidgets() {
        calendarRecyclerView = getActivity().findViewById(R.id.calendarRecyclerView);
        monthYearText = getActivity().findViewById(R.id.monthYearTV);
        shiftListView = getActivity().findViewById(R.id.shiftListView);
    }


    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
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
        setShiftsUsersAdpater();
    }

    private void setShiftsUsersAdpater()
    {
        // TODO: set adapter f   or shifts

        ShiftUserAdapter shiftUserAdapter = new ShiftUserAdapter(getActivity(), shifts);
        shiftListView.setAdapter(shiftUserAdapter);
    }

    public void newEventAction(View view)
    {
        //startActivity(new Intent(this, EventEditActivity.class));
    }
}