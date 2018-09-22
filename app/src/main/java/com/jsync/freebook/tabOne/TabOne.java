package com.jsync.freebook.tabOne;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jsync.freebook.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabOne extends Fragment {
    RecyclerView recyclerView;
    List<BooksListItems> booksList;
    BooksListRecyclerViewAdapter adapter;
    String uniqueID,bookName,bookPrice,bookId,userName,phone;
    ParseFile bookImage;
    ProgressBar progressBar;

    public TabOne() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_one, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        booksList = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBarTabOne);
        loadBooks();
        return view;
    }

    private void loadBooks() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects != null && objects.size() > 0 ) {
                        booksList.clear();

                        for (ParseObject object : objects) {
                            BooksListItems singleItem = new BooksListItems(
                                    object.getString("bookAuthor"),
                                    object.getParseFile("bookImage"),
                                    object.getString("bookTitle"),
                                    object.getString("bookId"),
                                    object.getString("bookPrice")
                            );

                            booksList.add(singleItem);

                        }

                            adapter = new BooksListRecyclerViewAdapter(getActivity(), booksList);
                            adapter.setOnItemClickListener(recyclerViewItemClickListener);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter);

                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    BooksListRecyclerViewAdapter.onRecyclerViewItemClickListener recyclerViewItemClickListener = new BooksListRecyclerViewAdapter.onRecyclerViewItemClickListener() {
        @Override
        public void onItemClickListener(View view, int pos) {
            switch (view.getId()) {
                case R.id.btnRequest:
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    phone = ParseUser.getCurrentUser().getUsername();
                    userName = ParseUser.getCurrentUser().getString("name");
                    bookName = booksList.get(pos).getBookName();
                    bookId = booksList.get(pos).getBookId();
                    bookPrice = booksList.get(pos).getBookPrice();
                    bookImage = booksList.get(pos).getBookImage();

                    uniqueID = UUID.randomUUID().toString();
                    final ArrayList<String> ids = new ArrayList<>();

                    ParseQuery<ParseObject> reqsQuery = ParseQuery.getQuery("Requests");
                    reqsQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects != null && objects.size() > 0) {
                                    ids.clear();
                                    for (ParseObject object : objects) {
                                        String id = object.getString("requestId");
                                        ids.add(id);
                                    }

                                    boolean whileTerminator = true;
                                    while (whileTerminator) {
                                        if (!ids.contains(uniqueID)) {
                                            whileTerminator = false;
                                        } else {
                                            uniqueID = UUID.randomUUID().toString();
                                        }
                                    }
                                    createRequest(phone,bookId,false,userName,bookName,bookImage,bookPrice,uniqueID);

                                } else {
                                    Log.i("Check Id", "No requests");
                                    createRequest(phone,bookId,false,userName,bookName,bookImage,bookPrice,uniqueID);

                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                Log.i("Check Id", e.getMessage());
                            }
                        }
                    });
            }


        }
    };

    private void createRequest( String phone, String bookId, boolean b, String username, String bookTitle, ParseFile bookImage,String bookPrice, String uniqueID) {
        ParseObject requestsObject = new ParseObject("Requests");
        requestsObject.put("requestId",uniqueID);
        requestsObject.put("phone",phone);
        requestsObject.put("bookId",bookId);
        requestsObject.put("isDonated",false);
        requestsObject.put("donatorName","");
        requestsObject.put("name",username);
        requestsObject.put("bookTitle",bookTitle);
        requestsObject.put("bookImage",bookImage);
        requestsObject.put("bookPrice",bookPrice);

        requestsObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null){
                    Toast.makeText(getContext(),"Request was sent successfully",Toast.LENGTH_SHORT).show();
                    loadBooks();
                    Log.i("Parse","Request was sent successfully");
                }else {
                    Toast.makeText(getContext(),"Request wasn\'t sent successfully",Toast.LENGTH_SHORT).show();
                    Log.i("Parse","Request wasn\'t sent successfull");
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
