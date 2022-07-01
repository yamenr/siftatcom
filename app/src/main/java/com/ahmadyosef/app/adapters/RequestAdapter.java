package com.ahmadyosef.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ahmadyosef.app.R;
import com.ahmadyosef.app.data.Shift;
import com.ahmadyosef.app.data.ShiftRequest;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<ShiftRequest> mData;
    private LayoutInflater mInflater;
    private Context context;

    private final RequestAdapter.ItemClickListener mClickListener = new RequestAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ShiftRequest request = mData.get(position);

            // TODO: add switch to details activity
            /*
            Intent i = new Intent(context, RestDetailsActivity.class);
            i.putExtra("shift", shift);



            context.startActivity(i);*/
        }
    };

    // data is passed into the constructor
    public RequestAdapter(Context context, List<ShiftRequest> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
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
}
