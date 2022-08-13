package com.ahmadyosef.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ahmadyosef.app.CalendarUtils;
import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.ShiftUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ShiftUserAdapter extends ArrayAdapter<ShiftUser>
{
    public ShiftUserAdapter(@NonNull Context context, List<ShiftUser> shifts)
    {
        super(context, 0, shifts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ShiftUser shift = getItem(position);

        if (convertView == null)
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.tvShiftCalendarCell);

        // TODO: Fix string date parsing
        String shiftUser = shift.getUsername() +" "+ shift.getDate(); //CalendarUtils.formattedTime(LocalTime.parse(shift.getDate()));
        eventCellTV.setText(shiftUser);
        return convertView;
    }
}