package com.jsync.freebook.tabTwo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsync.freebook.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by JSareen on 2/11/2018.
 */

public class RequestsListRecyclerViewAdapter extends RecyclerView.Adapter<RequestsListRecyclerViewAdapter.ViewHolder>{
    private Context context;
    int btnPostion = -1;
    private List<RequestListItems> itemsList;
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    private OnRecyclerViewItemClick listener;
    ParseQuery<ParseObject> reqQuery;

    public RequestsListRecyclerViewAdapter(Context context,List<RequestListItems> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public void setRecyclerViewItemClickListener(OnRecyclerViewItemClick listener){
        this.listener = listener;
    }

    public void removeItem(int pos){
        itemsList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public RequestsListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_list_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RequestsListRecyclerViewAdapter.ViewHolder holder, final int position) {

        RequestListItems singleItem = itemsList.get(position);
        holder.reqBookTitleTextView.setText(singleItem.getBookTitle());
        Double price = Double.valueOf(singleItem.getBookPrice());
        holder.reqBookPriceTextView.setText(numberFormat.format(price));
        holder.reqUserNameTextView.setText(singleItem.getUserName());

        ParseFile imageFile = singleItem.getImageFile();
        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null) {
                    if (data != null) {
                        Bitmap imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.reqBookImageView.setImageBitmap(imageBitmap);
                    }
                }
            }
        });
      //  if (reqQuery == null) {
            reqQuery = ParseQuery.getQuery("Requests");
            reqQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        if (objects != null && objects.size() > 0) {

                          for (int i=0; i<objects.size(); i++){
                           String userId = objects.get(i).getString("phone");
                           if (userId != null && userId.equals(ParseUser.getCurrentUser().getUsername())){
                               btnPostion = i;
                           }
                           if (btnPostion == position){
                               holder.btnDonate.setEnabled(false);
                           }
                          }

                        } else {
                            Log.i("DonateButtonHidingLogic", "Current users request doesn\'t exists");
                        }

                    } else {
                        Log.i("DonateButtonHidingLogic", "Error: " + e.getMessage());
                    }
                }
            });

      //  }

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public interface OnRecyclerViewItemClick{
        void onRecyclerViewItemClickListener(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView reqBookImageView;
        TextView reqUserNameTextView;
        TextView reqBookTitleTextView;
        TextView reqBookPriceTextView;
        Button btnDonate;

        public ViewHolder(View itemView) {
            super(itemView);
            reqBookImageView = itemView.findViewById(R.id.reqBookImageView);
            reqUserNameTextView = itemView.findViewById(R.id.reqUserNameTextView);
            reqBookTitleTextView = itemView.findViewById(R.id.reqBookNameTextView);
            reqBookPriceTextView = itemView.findViewById(R.id.reqBookPriceTextView);
            btnDonate = itemView.findViewById(R.id.btnDonate);
            btnDonate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener!=null){
                listener.onRecyclerViewItemClickListener(view,getAdapterPosition());
            }

        }
    }
}
