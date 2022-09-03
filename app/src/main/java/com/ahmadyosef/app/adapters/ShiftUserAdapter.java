package com.ahmadyosef.app.adapters;

import static com.ahmadyosef.app.CalendarUtils.selectedDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ahmadyosef.app.CalendarUtils;
import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.activities.AdminActivity;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.ShiftUser;
import com.ahmadyosef.app.fragments.CommonFragment;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
Adapter class for individual recycler in CommonFragment
 */
public class ShiftUserAdapter extends ArrayAdapter<ShiftUser>
{
    private Utilities utils;
    private FirebaseServices fbs;
    private Context context;
    private List<ShiftUser> shifts;
    private CommonFragment cf;

    public ShiftUserAdapter(@NonNull Context context, List<ShiftUser> shifts)
    {
        super(context, 0, shifts);
        this.context = context;
        this.shifts = shifts;
        utils = Utilities.getInstance();
        fbs = FirebaseServices.getInstance();
        cf = (CommonFragment)(((AdminActivity)getContext()).getFr());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ShiftUser shift = getItem(position);

        if (convertView == null)
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.common_shift_user_details, parent, false);

        TextView tvUsername = convertView.findViewById(R.id.tvCommonShiftUsername);
        ImageView ivShiftTypeLogo = convertView.findViewById(R.id.ivCommonShiftUserIconRow);
        Button btnEdit = convertView.findViewById(R.id.btnEditCommonShift);
        Button btnDelete = convertView.findViewById(R.id.btnDeleteCommonShift);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShiftEditDialog(position);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogDeleteItem(position);
            }
        });

        String shiftUser = shift.getUsername();
        tvUsername.setText(fbs.getNameOfUser(shiftUser));
        if (shift.getType() == ShiftType.Morning)
            Picasso.get().load(R.drawable.sunrise).into(ivShiftTypeLogo);
        else if (shift.getType() == ShiftType.Afternoon)
            Picasso.get().load(R.drawable.sun).into(ivShiftTypeLogo);
        else
            Picasso.get().load(R.drawable.night).into(ivShiftTypeLogo);

        return convertView;
    }


    public void showAlertDialogDeleteItem(int position)
    {
        AlertDialog.Builder builder                = new AlertDialog.Builder(context);
        builder.setTitle(R.string.are_you_sure_delete);
        final View customLayout
                = ((Activity)context).getLayoutInflater()
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
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which)
                    {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    private void removeItem(int i) {
        fbs.removeShiftFromUser2(shifts.get(i));
        shifts.remove(i);
        notifyDataSetChanged();
        cf.refreshCommon();
    }

    private void showShiftEditDialog(int i) {
        Spinner spShift, spUsers;
        CalendarView cal;

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(context);
        builder.setTitle(R.string.edit_user_shift_dialogue);

        // set the custom layout
        final View customLayout
                = ((Activity)context).getLayoutInflater()
                .inflate(
                        R.layout.dialogue_edit_shift,
                        null);
        spShift = customLayout.findViewById(R.id.spShiftTypeAddEditShiftDialogue);
        //setShiftTypeSpinnerSelection(spShift, shifts.get(i).getType());
        spUsers = customLayout.findViewById(R.id.spUserAddEditShiftDialogue);
        cal = customLayout.findViewById(R.id.calAddEditShiftDialogue);
        final LocalDate[] curDate = {selectedDate};
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                curDate[0] = LocalDate.of(year, month + 1, day);
            }
        });
        ShiftUser shift = shifts.get(i);
        LocalDate date = utils.convertLocalDate(shift.getDate()).minusMonths(1);
        cal.setDate(utils.getMilliSecsForCalendar(date), true, true);
        spShift.setAdapter(new ArrayAdapter<ShiftType>(context, android.R.layout.simple_list_item_1, ShiftType.values()));
        spShift.setSelection(shifts.get(i).getType().ordinal());
        ArrayList<String> userShifts = utils.usersList(cf.getLastUsersList());
        spUsers.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, userShifts));
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
        builder.setNegativeButton("Cancel",
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

    /*
    private void setShiftTypeSpinnerSelection(Spinner spShift, ShiftType type) {
        if (type == ShiftType.Morning)
            spShift.setSelection(0);
    } */

    private void sendDialogDataToActivity(String user, String shiftType, String date, int i)
    {
        if (shiftChanges(user, shiftType, date, i))
            updateShift(user, shiftType, date, i);
        else
            Toast.makeText(context, R.string.no_changes_made_to_shift, Toast.LENGTH_LONG).show();
    }

    private boolean shiftChanges(String user, String shiftType, String date, int i) {
        ShiftUser shift = shifts.get(i);
        if (!shift.getUsername().equals(user) || !shift.getDate().equals(date) || !shift.getType().equals(shiftType))
            return true;
        return false;
    }

    private void updateShift(String user, String shiftType, String date, int i) {
        // TODO: if user changes, add new shift to user, remove from current
        ShiftUser newShift = new ShiftUser(user, date, ShiftType.valueOf(shiftType));
        if (!shifts.get(i).getUsername().equals(user))
        {
            fbs.addShiftToUser(newShift);
            removeItem(i);
        }
        else
        {
            //cf.getLastUsershifts().get(i).setDate(date);
            //cf.getLastUsershifts().get(i).setType(ShiftType.valueOf(shiftType));
            ShiftUser original = shifts.get(i);
            fbs.editUserShift(newShift, original);
            cf.refreshCommon();
        }
    }
}