package com.jsync.freebook.tabTwo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jsync.freebook.PaymentActivity;
import com.jsync.freebook.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TabTwo extends Fragment{
    RecyclerView reqRecyclerView;
    RequestsListRecyclerViewAdapter adapter;
    List<RequestListItems> itemsList;

    String userName,bookTitle,bookPrice,phone;
    ParseFile bookImage;
    Handler handler;
    int listSize;
    boolean firstTime= true;

    public static final int PAYMENT_ACTIVITY_CODE = 726;

    public TabTwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_two, container, false);
        itemsList = new ArrayList<>();

        reqRecyclerView = view.findViewById(R.id.reqRecyclerView);
        handler = new Handler();
        loadRequest();
        return view;
    }

    private void loadRequest() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    if(objects != null && objects.size() > 0){
                        if(firstTime){
                            /*Toast.makeText(getContext(),"Old list available",Toast.LENGTH_SHORT).show();*/
                            listSize = objects.size();
                            firstTime = false;

                            itemsList.clear();
                            for (int i=0 ; i < objects.size(); i++){
                                bookImage = objects.get(i).getParseFile("bookImage");
                                userName = objects.get(i).getString("name");
                                bookTitle = objects.get(i).getString("bookTitle");
                                bookPrice = objects.get(i).getString("bookPrice");
                                phone = objects.get(i).getString("phone");

                                RequestListItems singleItem = new RequestListItems(
                                        bookImage,
                                        userName,
                                        bookTitle,
                                        bookPrice,
                                        phone);

                                itemsList.add(singleItem);
                            }

                            adapter = new RequestsListRecyclerViewAdapter(getActivity(), itemsList);
                            adapter.setRecyclerViewItemClickListener(recyclerViewItemClick);
                            reqRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            reqRecyclerView.setAdapter(adapter);
                        }else {
                            if (listSize != objects.size()){
                                itemsList.clear();
                                listSize = objects.size();
                                /*Toast.makeText(getContext(),"New list available",Toast.LENGTH_SHORT).show();*/
                                for (int i=0 ; i < objects.size(); i++){
                                    bookImage = objects.get(i).getParseFile("bookImage");
                                    userName = objects.get(i).getString("name");
                                    bookTitle = objects.get(i).getString("bookTitle");
                                    bookPrice = objects.get(i).getString("bookPrice");
                                    phone = objects.get(i).getString("phone");

                                    RequestListItems singleItem = new RequestListItems(
                                            bookImage,
                                            userName,
                                            bookTitle,
                                            bookPrice,
                                            phone);

                                    itemsList.add(singleItem);
                                }
                                adapter = new RequestsListRecyclerViewAdapter(getActivity(), itemsList);
                                adapter.setRecyclerViewItemClickListener(recyclerViewItemClick);
                                reqRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                reqRecyclerView.setAdapter(adapter);
                            }
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadRequest();
                            }
                        },7000);
                    }
                }
            }
        });

    }

    RequestsListRecyclerViewAdapter.OnRecyclerViewItemClick recyclerViewItemClick = new RequestsListRecyclerViewAdapter.OnRecyclerViewItemClick() {
       @Override
       public void onRecyclerViewItemClickListener(View view, int pos) {

            switch (view.getId()){
                case R.id.btnDonate:
                    /*Toast.makeText(getContext(),"CLicked on item no. " + pos, Toast.LENGTH_SHORT).show();*/
                    startForPayment(pos);
                    break;
            }
       }
   };

    public void startForPayment(int pos){

        Intent payment = new Intent(getContext(),PaymentActivity.class);
        payment.putExtra("AMOUNT",itemsList.get(pos).getBookPrice());
        payment.putExtra("NAME",ParseUser.getCurrentUser().getString("name"));
        payment.putExtra("PHONE",ParseUser.getCurrentUser().getUsername());
        payment.putExtra("ITEM_NUMBER",pos);
        startActivityForResult(payment,PAYMENT_ACTIVITY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PAYMENT_ACTIVITY_CODE: if (resultCode == RESULT_OK){
                int number = data.getIntExtra("ITEM_NUMBER",0);
                deleteDonated(itemsList.get(number).getPhone(),number);

                /*Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();*/
            }else {
                Toast.makeText(getContext(),"Payment Failed!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteDonated(final String phone, final int number){
        ParseQuery<ParseObject> delQue = ParseQuery.getQuery("Requests");
        delQue.whereEqualTo("phone",phone);
        delQue.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects!=null && objects.size()>0) {
                        for (ParseObject object : objects) {

                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null){
                                        adapter.removeItem(number);
                                        /*Toast.makeText(getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();*/
                                        ParseObject processed = new ParseObject("ReqsFulfilled");
                                        processed.put("requestedBy",phone);
                                        processed.put("donatorPhone",ParseUser.getCurrentUser().getUsername());
                                        processed.put("donatorName",ParseUser.getCurrentUser().getString("name"));
                                        processed.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null){
                                                    Log.i("Parse","Request Fulfilled");
                                                }else {
                                                    Log.i("Parse","Request Fulfill error: " + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
