package com.jsync.freebook.tabOne;

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

import java.util.List;

/**
 * Created by JasS on 2/2/2018.
 */

public class BooksListRecyclerViewAdapter extends RecyclerView.Adapter<BooksListRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<BooksListItems> itemsList;
    private java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance();
    onRecyclerViewItemClickListener listener;

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    public BooksListRecyclerViewAdapter(Context context, List<BooksListItems> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_list_row,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BooksListItems listItem = itemsList.get(position);

        holder.txtDesc.setText(listItem.getBookAuthor());
        holder.txtName.setText(listItem.getBookName());
        float price = Float.parseFloat(listItem.getBookPrice());
        holder.txtPrice.setText(numberFormat.format(price));

        ParseFile imageFile = listItem.getBookImage();

        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null){
                    if (data!=null){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                        holder.imgImage.setImageBitmap(bitmap);
                    }
                }
            }
        });

        ParseQuery<ParseObject> reqQuery = ParseQuery.getQuery("Requests");
        reqQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects!=null && objects.size()>0) {


                        for (ParseObject object: objects){
                            String username = object.getString("phone");
                            Log.i("Parse","User Name: " + username);
                            if (username.equals(ParseUser.getCurrentUser().getUsername())){
                                Log.i("Parse","User exists");
                                holder.btnReq.setEnabled(false);
                            }
                        }

                    }else {
                        Log.i("ButtonDisablingLogic","No requests exists");
                    }
                }else {
                    Log.i("ButtonDisablingLogic","Error: " + e.getMessage());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public interface  onRecyclerViewItemClickListener{
        void onItemClickListener(View view,int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgImage;
        TextView txtName;
        TextView txtDesc;
        TextView txtPrice;
        Button btnReq;


        public ViewHolder(View itemView) {
            super(itemView);
            imgImage = itemView.findViewById(R.id.imgImage);
            txtName = itemView.findViewById(R.id.txtName);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnReq = itemView.findViewById(R.id.btnRequest);
            btnReq.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener != null){
                listener.onItemClickListener(view,getAdapterPosition());
            }
        }
    }
}
