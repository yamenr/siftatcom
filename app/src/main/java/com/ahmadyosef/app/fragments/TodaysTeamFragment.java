package com.ahmadyosef.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.adapters.ShiftAdapter;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.interfaces.ShiftTypeCallback;
import com.ahmadyosef.app.interfaces.UsersCallback;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodaysTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysTeamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodaysTeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodaysTeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodaysTeamFragment newInstance(String param1, String param2) {
        TodaysTeamFragment fragment = new TodaysTeamFragment();
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
        return inflater.inflate(R.layout.fragment_todays_team, container, false);
    }
}