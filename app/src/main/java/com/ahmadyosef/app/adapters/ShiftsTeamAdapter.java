package com.ahmadyosef.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.ShiftUser;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

public class ShiftsTeamAdapter extends RecyclerView.Adapter<ShiftsTeamAdapter.ViewHolder> {

    private List<ShiftUser> mData;
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseServices fbs;

    private final ShiftsTeamAdapter.ItemClickListener mClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ShiftUser shift = mData.get(position);

            // TODO: add switch to details activity
            /*
            Intent i = new Intent(context, RestDetailsActivity.class);
            i.putExtra("shift", shift);



            context.startActivity(i);*/
        }
    };

    // data is passed into the constructor
    public ShiftsTeamAdapter(Context context, List<ShiftUser> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        Collections.sort(this.mData);
        this.context = context;
        fbs = FirebaseServices.getInstance();
    }

    // inflates the row layout from xml when needed
    @Override
    public ShiftsTeamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shifts_team_rows, parent, false);
        return new ShiftsTeamAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ShiftsTeamAdapter.ViewHolder holder, int position) {
        ShiftUser shift = mData.get(position);
        holder.tvTeamMemberRow.setText(fbs.getNameOfUser(shift.getUsername()));
        if (shift.getType() == ShiftType.Morning)
            Picasso.get().load(R.drawable.sunrise).into(holder.ivShiftsTeamIconRow);
        else if (shift.getType() == ShiftType.Afternoon)
            Picasso.get().load(R.drawable.sun).into(holder.ivShiftsTeamIconRow);
        else
            Picasso.get().load(R.drawable.night).into(holder.ivShiftsTeamIconRow);

        //Picasso.get().load(rest.getPhoto()).into(holder.ivPhoto);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrol    led off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTeamMemberRow;
        ImageView ivShiftsTeamIconRow;

        ViewHolder(View itemView) {
            super(itemView);
            tvTeamMemberRow = itemView.findViewById(R.id.tvTeamMemberRow);
            ivShiftsTeamIconRow = itemView.findViewById(R.id.ivShiftsTeamIconRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ShiftUser getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

/*
public class ShiftsTeamAdapter extends ArrayAdapter<ShiftUser>  {
    private Utilities utils;
    private FirebaseServices fbs;
    private Context context;
    private List<ShiftUser> shifts;
    private Todays tf;

    public ShiftsTeamAdapter(@NonNull Context context, List<ShiftUser> shifts)
    {
        super(context, 0, shifts);
        this.context = context;
        this.shifts = shifts;
        utils = Utilities.getInstance();
        fbs = FirebaseServices.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ShiftUser shift = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shifts_team_rows, parent, false);

        ImageView ivShiftTypeIcon = convertView.findViewById(R.id.ivShiftsTeamIconRow);
        TextView tvTeamMemberName = convertView.findViewById(R.id.tvTeamMemberRow);


        String shiftUser = shift.getUsername();
        tvTeamMemberName.setText(fbs.getNameOfUser(shiftUser));
        if (shift.getType() == ShiftType.Morning)
            Picasso.get().load(R.drawable.sunrise).into(ivShiftTypeIcon);
        else if (shift.getType() == ShiftType.Afternoon)
            Picasso.get().load(R.drawable.sun).into(ivShiftTypeIcon);
        else
            Picasso.get().load(R.drawable.night).into(ivShiftTypeIcon);

        return convertView;
    }

}
*/