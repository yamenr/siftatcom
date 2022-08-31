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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.Utilities;
import com.ahmadyosef.app.data.FirebaseServices;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;
import com.ahmadyosef.app.data.ShiftRequestType;
import com.ahmadyosef.app.data.ShiftType;
import com.ahmadyosef.app.data.ShiftUser;
import com.ahmadyosef.app.data.User;
import com.ahmadyosef.app.fragments.AdminFragment;
import com.ahmadyosef.app.interfaces.RequestDialogueCallback;
import com.ahmadyosef.app.interfaces.UsersMapCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<ShiftRequest> mData;
    private LayoutInflater mInflater;
    private Context context;
    private RequestDialogueCallback rdc;
    private UsersMapCallback umc;
    private Utilities utils;
    private Map<String, User> users;
    private Map<String, ShiftRequest> requests;
    private FirebaseServices fbs;
    private View current;
    private static final String TAG = "RequuestAdapter";

    private final RequestAdapter.ItemClickListener mClickListener = new RequestAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ShiftRequest request = mData.get(position);
            current = view;
            rdc = new RequestDialogueCallback() {
                @Override
                public void onCallback(boolean b) {
                    if (b == true) {
                        removeRequest(request, position);
                        //addShiftToUser(request, position);
                    }
                }
            };
            if (request.getType() == ShiftRequestType.New)
                showApproveNewShiftrequest(view, request);
            else if (request.getType() == ShiftRequestType.Delete)
            {
                boolean isBefore = LocalTime.now().isBefore(utils.getShiftStartTime(request.getShift().getType()));
                int interval = LocalTime.now().getHour() - utils.getShiftStartTime(request.getShift().getType()).getHour();
                if (isBefore && (interval > 3))
                {
                    showRemoveShiftRequest(view, request, position);
                }
                else
                {
                }
            }
        }
    };

    private void addShiftToUser(ShiftRequest request, int position) {
        for(Map.Entry<String, User> user: users.entrySet())
        {
            if (user.getValue().getUsername().equals(request.getUsername()))
            {
                user.getValue().getShifts().add(request.getShift());
                fbs.getFire().collection("users_").
                                document(user.getKey()).
                                set(user.getValue()).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                removeRequest(request, position);
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


    private void removeRequest(ShiftRequest request, int position) {
        for(Map.Entry<String, ShiftRequest> requestEntry: requests.entrySet())
        {
            if (requestEntry.getValue().getShift().getId().equals(request.getShift().getId()))
            {
                fbs.getFire().collection("requests").
                        document(requestEntry.getKey()).
                        delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                removeAt(position);
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

    public void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    // data is passed into the constructor
    public RequestAdapter(Context context, List<ShiftRequest> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.fbs = FirebaseServices.getInstance();
        this.utils = Utilities.getInstance();
        umc = new UsersMapCallback() {
            @Override
            public void onCallback(Map<String, User> users) {

            }
        };
        users = getUsersMap();
        requests = fbs.getRequestsMap();
        current = null;
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
        holder.tvUser.setText(fbs.getNameOfUser(request.getUsername()));
        holder.tvDate.setText(request.getShift().getDate());
        if (request.getType() == ShiftRequestType.New)
        {
            holder.tvType.setText(String.valueOf(ShiftRequestType.New));
            holder.tvType.setTextColor(context.getResources().getColor(R.color.blue));
        }
        else if (request.getType() == ShiftRequestType.Delete)
        {
            holder.tvType.setText(String.valueOf(ShiftRequestType.Delete));
            holder.tvType.setTextColor(context.getResources().getColor(R.color.red));
        }
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
        TextView tvType;
        //ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUsernameRequestRow);
            tvDate = itemView.findViewById(R.id.tvDateRequestRow);
            tvType = itemView.findViewById(R.id.tvTypeRequestRow);
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

    public void showApproveNewShiftrequest(View view, ShiftRequest sr)
    {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.approve_shift_request);
        final View customLayout
                = ((Activity)view.getContext()).getLayoutInflater()
                .inflate(
                        R.layout.dialogue_approve_shift,
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
                                //
                                // fbs.addShiftRequest(sr);
                                ShiftUser su = new ShiftUser(sr.getUsername(), sr.getShift().getDate(), sr.getShift().getType());
                                fbs.addShiftToUser(su);
                                rdc.onCallback(true);
                            }
                        });
        builder
                .setNegativeButton("Cancel",
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

    public void showRemoveShiftRequest(View view, ShiftRequest sr, int position)
    {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.delete_shift_request);
        final View customLayout
                = ((Activity)view.getContext()).getLayoutInflater()
                .inflate(
                        R.layout.dialogue_approve_shift,
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
                                ShiftUser su = new ShiftUser(sr.getUsername(), sr.getShift().getDate(), sr.getShift().getType());
                                fbs.removeShiftFromUser2(su);
                                removeRequest(sr, position);
                            }
                        });
        builder
                .setNegativeButton("Cancel",
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

    public Map<String, User> getUsersMap()
    {
        Map<String, User> users = new HashMap<>();

        try {
            users.clear();
            fbs.getFire().collection("users_")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    users.put(document.getId(), document.toObject(User.class));
                                }
                                umc.onCallback(users);
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Log.e("getUsersMap(): ", e.getMessage());
        }

        return users;
    }
}
