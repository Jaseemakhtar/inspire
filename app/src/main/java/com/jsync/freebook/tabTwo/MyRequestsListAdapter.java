package com.jsync.freebook.tabTwo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsync.freebook.R;

import java.util.List;

/**
 * Created by JSareen on 3/20/2018.
 */

public class MyRequestsListAdapter extends RecyclerView.Adapter<MyRequestsListAdapter.ViewHolder> {
    List<MyRequestsList> listItems;

    public MyRequestsListAdapter(List<MyRequestsList> listItems){
        this.listItems = listItems;
    }

    public void addItem(MyRequestsList singleItem){
        listItems.add(singleItem);
        notifyDataSetChanged();
    }
    public void remove(int pos){
        listItems.remove(pos);
        notifyDataSetChanged();
    }
    public void removeAll(){
        listItems.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_requests_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyRequestsList singleItem = listItems.get(position);

        holder.txtTitle.setText(singleItem.getTitle());
        holder.txtDonatorName.setText(singleItem.getDonatorName());
        holder.txtDesc.setText(singleItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDesc, txtDonatorName;
        public ViewHolder(View itemView) {
            super(itemView);
            txtDesc = itemView.findViewById(R.id.txtMyReqDesc);
            txtDonatorName = itemView.findViewById(R.id.txtMyReqDonatorName);
            txtTitle = itemView.findViewById(R.id.txtMyReqBookTitle);
        }
    }
}
