package com.ahmadyosef.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
Adapter class for user list recycler in UserListFragment
*/
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;
    private Context context;

    private final UserAdapter.ItemClickListener mClickListener = new UserAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            User user = mData.get(position);
            //tvName.setText(user.getName());
            // TODO: Adding dialogue for user's data
                // create xml: including all text fields of the user
                // add function the builds and shows dialogue (like the add shift)
                TextView id,name,username,addres,phone,type;
                //ShiftUserAdapter
                //-> showShiftEditDialog
                CalendarView cal;
                // Create an alert builder
                AlertDialog.Builder builder
                        = new AlertDialog.Builder(context);
                builder.setTitle("info user");
                // set the custom layout
                final View customLayout
                        = ((Activity)context).getLayoutInflater()
                        .inflate(
                                R.layout.datauser,
                                null);

                name=customLayout.findViewById(R.id.name);
                username=customLayout.findViewById(R.id.username);
                addres=customLayout.findViewById(R.id.addres);
                phone=customLayout.findViewById(R.id.phone);
                type=customLayout.findViewById(R.id.type);

                name.setText(user.getName());
                username.setText(user.getUsername());
                addres.setText(user.getAddress());
                phone.setText(user.getPhone());
                type.setText(String.valueOf(user.getType()));


                builder.setView(customLayout);
                builder
                        .setNegativeButton("close",
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
    };

    // data is passed into the constructor
    public UserAdapter(Context context, List<User> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_row, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        User user = mData.get(position);
        holder.tvName.setText(user.getName());
        if (user.getPhoto().isEmpty())
            Picasso.get().load(R.drawable.user).into(holder.ivPhoto);
        else
            Picasso.get().load(user.getPhoto()).into(holder.ivPhoto);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameUserRow);
            ivPhoto = itemView.findViewById(R.id.imgUserUserRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    User getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
