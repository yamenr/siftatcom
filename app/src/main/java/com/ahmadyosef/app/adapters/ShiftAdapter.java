package com.ahmadyosef.app.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftRequestType;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {

    private List<Shift> mData;
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseServices fbs;

    private final ShiftAdapter.ItemClickListener mClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Shift shift = mData.get(position);

            showSubmitDeleteShiftRequestDialogue(shift);

            // TODO: add switch to details activity
            /*
            Intent i = new Intent(context, RestDetailsActivity.class);
            i.putExtra("shift", shift);



            context.startActivity(i);*/
        }

    };

    // data is passed into the constructor
    public ShiftAdapter(Context context, List<Shift> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        Collections.sort(this.mData);
        this.context = context;
        this.fbs = FirebaseServices.getInstance();
    }

    // inflates the row layout from xml when needed
    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shift_row, parent, false);
        return new ShiftAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ShiftAdapter.ViewHolder holder, int position) {
        Shift shift = mData.get(position);
        holder.tvDate.setText(shift.getDate());
        if (shift.getType() == ShiftType.Morning)
            Picasso.get().load(R.drawable.sunrise).into(holder.ivPhoto);
        else if (shift.getType() == ShiftType.Afternoon)
            Picasso.get().load(R.drawable.sun).into(holder.ivPhoto);
        else
            Picasso.get().load(R.drawable.night).into(holder.ivPhoto);

        //Picasso.get().load(rest.getPhoto()).into(holder.ivPhoto);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrol    led off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDate;
        ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivPhoto = itemView.findViewById(R.id.ivShiftIconRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // convenience method for getting data at click position
    Shift getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void showSubmitDeleteShiftRequestDialogue(Shift shift) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(context);
        builder.setTitle(R.string.submit_request_to_remove_shift);
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
                                ShiftRequest sr = new ShiftRequest(fbs.getAuth().getCurrentUser().getEmail(), shift, ShiftRequestType.Delete);
                                fbs.addShiftRequest(sr);
                            }
                        });
                builder.setNegativeButton(
                        "Cancel",
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
}
