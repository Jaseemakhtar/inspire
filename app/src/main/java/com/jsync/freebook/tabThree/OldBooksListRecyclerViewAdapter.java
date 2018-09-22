package com.jsync.freebook.tabThree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsync.freebook.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

/**
 * Created by JSareen on 3/14/2018.
 */

public class OldBooksListRecyclerViewAdapter extends RecyclerView.Adapter<OldBooksListRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<OldBookListItems> listItems;
    OldBooksRecyclerViewClickListener mListener;

    public OldBooksListRecyclerViewAdapter(Context context, List<OldBookListItems> listItems){
        this.context = context;
        this.listItems = listItems;
    }

    public void setRecyclerViewClickListener(OldBooksRecyclerViewClickListener mListener){
        this.mListener = mListener;
    }
    public void removeItem(int pos){
        listItems.remove(pos);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.old_books_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        OldBookListItems singleBookDetail = listItems.get(position);
        holder.txtBookTitle.setText(singleBookDetail.getBookTitle());
        holder.txtBookDesc.setText(singleBookDetail.getBookDesc());
        holder.txtBookDonator.setText(singleBookDetail.getDonatorName());

        ParseFile oldBookImage = singleBookDetail.getOldBookImage();
        oldBookImage.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e ==null){
                    if (data!=null && data.length > 0){
                        Bitmap image = BitmapFactory.decodeByteArray(data,0,data.length);
                        holder.imgOldBook.setImageBitmap(image);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface OldBooksRecyclerViewClickListener{
        void onRecyclerViewClick(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtBookTitle, txtBookDesc, txtBookDonator;
        ImageView imgOldBook;
        Button btnGet;
        public ViewHolder(View itemView) {
            super(itemView);
            txtBookDesc = itemView.findViewById(R.id.txtOldBookDescription);
            txtBookTitle = itemView.findViewById(R.id.txtOldBookTitle);
            txtBookDonator = itemView.findViewById(R.id.txtOldBookDonator);
            imgOldBook = itemView.findViewById(R.id.imgOldBooksPic);
            btnGet = itemView.findViewById(R.id.btnOldBookGet);
            btnGet.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onRecyclerViewClick(view,getAdapterPosition());
        }
    }
}
