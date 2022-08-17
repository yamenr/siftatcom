package com.ahmadyosef.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.common_shift_user_details, parent, false);

        TextView tvUsername = convertView.findViewById(R.id.tvCommonShiftUsername);
        Button btnEdit = convertView.findViewById(R.id.btnEditCommonShift);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // TODO: Fix string date parsing
        String shiftUser = shift.getUsername();
        tvUsername.setText(shiftUser);
        return convertView;
    }
}