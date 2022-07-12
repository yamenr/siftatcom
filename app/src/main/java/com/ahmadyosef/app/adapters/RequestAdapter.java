package com.ahmadyosef.app.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.interfaces.RequestDialogueCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<ShiftRequest> mData;
    private LayoutInflater mInflater;
    private Context context;
    private RequestDialogueCallback rdc;
    private ArrayList<User> users;
    private FirebaseServices fbs;

    private final RequestAdapter.ItemClickListener mClickListener = new RequestAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ShiftRequest request = mData.get(position);
            rdc = new RequestDialogueCallback() {
                @Override
                public void onCallback(boolean b) {
                    if (b == true)
                    {
                        addShiftToUser(request);
                    }
                }
            };
            showAlertDialogButtonClicked(view);
        }
    };

    private void addShiftToUser(ShiftRequest request) {
        Map<String, User> users = fbs.getUsersMap();
        for(Map.Entry<String, User> user: users.entrySet())
        {
            if (user.getValue().getUsername().equals(request.getUsername()))
            {
                user.getValue().getShifts().add(request.getShift());
                fbs.getFire().collection("users").
                                document(user.getKey()).
                                set(user.getValue()).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                removeRequest(request);
                                Log.i("addShiftToUser: ", "User shift added successfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("addShiftToUser: ", "User shift failed to be added to Firestore! " + e.getMessage());
                    }
                });
            }
        }
    }

    private void removeRequest(ShiftRequest request) {
        Map<String, ShiftRequest> requests = fbs.getRequestsMap();
        for(Map.Entry<String, ShiftRequest> requestEntry: requests.entrySet())
        {
            if (requestEntry.getValue().getUsername().equals(request.getUsername()))
            {
                fbs.getFire().collection("requests").
                        document(requestEntry.getKey()).
                        delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("addShiftToUser: ", "Request removed successfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("addShiftToUser: ", "Failed to remove request from Firestore!" + e.getMessage());
                            }
                        });
            }
        }
    }



    // data is passed into the constructor
    public RequestAdapter(Context context, List<ShiftRequest> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.fbs = FirebaseServices.getInstance();
    }

    // inflates the row layout from xml when needed
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.request_row, parent, false);
        return new RequestAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder holder, int position) {
        ShiftRequest request = mData.get(position);
        holder.tvUser.setText(request.getUsername());
        holder.tvDate.setText(request.getShift().getDate());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrol    led off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUser;
        TextView tvDate;
        //ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUsernameRequestRow);
            tvDate = itemView.findViewById(R.id.tvDateRequestRow);
            //ivPhoto = itemView.findViewById(R.id.ivPhotoRestRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ShiftRequest getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void showAlertDialogButtonClicked(View view)
    {
        //TextView
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.approve_shift_request);

        // set the custom layout
        final View customLayout
                = ((Activity)view.getContext()).getLayoutInflater()
                .inflate(
                        R.layout.dialogue_approve_shift,
                        null);
        //spShiftType = customLayout.findViewById(R.id.spShiftTypeBookShiftDialogue);
        //spShiftType.setAdapter(new ArrayAdapter<ShiftType>(getActivity(), android.R.layout.simple_list_item_1, ShiftType.values()));
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
                                rdc.onCallback(true);
                                // send data from the
                                // AlertDialog to the Activity
                                /*
                                sendDialogDataToActivity(
                                        spShiftType
                                                .getSelectedItem().toString()
                                                .toString()); */
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();

    }


    // Do something with the data
    // coming from the AlertDialog
    /*
    private void sendDialogDataToActivity(String data)
    {
        selectedShiftType = ShiftType.valueOf(data);
        scall.onCallback(selectedShiftType);
        refreshShiftsInList();
        Toast.makeText(getActivity(), getResources().getString(R.string.shift_request_sent), Toast.LENGTH_LONG).show();
    } */

    /*
    private void refreshShiftsInList()
    {
        getUsers();
        User user = findUsingIterator(fbs.getAuth().getCurrentUser().getEmail(), users);

        /*
        for (User u : users
             ) {
            if (u.getUsername() == fbs.getAuth().getCurrentUser().getEmail())
                shifts = u.getShifts();
        }
        if (user != null) {
            adapter = new ShiftAdapter(getContext(), shifts);
            rv.setAdapter(adapter);
        }
    }*/

}
