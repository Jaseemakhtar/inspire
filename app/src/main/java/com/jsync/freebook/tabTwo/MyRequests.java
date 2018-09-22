package com.jsync.freebook.tabTwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.jsync.freebook.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyRequests extends AppCompatActivity {
    //TextView txtDesc, txtDonatorName, txtBookTitle;
    List<MyRequestsList> listItems;
    RecyclerView recyclerView;
    MyRequestsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        //txtDesc = findViewById(R.id.txtMyReqDesc);
        //txtBookTitle = findViewById(R.id.txtMyReqBookTitle);
        //txtDonatorName = findViewById(R.id.txtMyReqDonatorName);
        recyclerView = findViewById(R.id.recyclerViewMyReqs);

        listItems = new ArrayList<>();
        adapter = new MyRequestsListAdapter(listItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        loadRequests();
    }

    private void loadRequests() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ReqsFulfilled");
        query.whereEqualTo("requestedBy", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if ( e == null){
                    if (objects != null && objects.size() > 0){

                        adapter.removeAll();

                        Log.i("ReqsFulFilled","Reqs Fulfilled exists");
                        Toast.makeText(getApplicationContext(),"Your Requests are fulfilled",Toast.LENGTH_SHORT).show();

                        for (ParseObject object: objects){
                            String donator = "Donator: " + object.getString("donatorName") + ".";
                            String title = "Your requested is accepted.";
                            String desc = "Wait until the book reach you.";

                            MyRequestsList singleItem = new MyRequestsList(title,donator,desc);
                            adapter.addItem(singleItem);
                        }

                        ParseQuery<ParseObject> reqs = ParseQuery.getQuery("Requests");
                        reqs.whereEqualTo("phone",ParseUser.getCurrentUser().getUsername());
                        reqs.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e==null){
                                    if (object!=null){
                                        //txtBookTitle.setText("Your Request is not accepted.");
                                        //txtDesc.setText("Please wait for some time.");
                                        //txtDonatorName.setVisibility(View.GONE);

                                        String donator = "";
                                        String title = "Your requested is yet to be accepted.";
                                        String desc = "Wait for some time.";

                                        MyRequestsList singleItem = new MyRequestsList(title,donator,desc);
                                        adapter.addItem(singleItem);

                                    }
                                }
                            }
                        });



                    }else {
                        Toast.makeText(getApplicationContext(),"Requests not fulfilled yet",Toast.LENGTH_SHORT).show();
                        Log.i("ReqsFulFilled","Reqs Fulfilled not exists");
                        ParseQuery<ParseObject> reqs = ParseQuery.getQuery("Requests");
                        reqs.whereEqualTo("phone",ParseUser.getCurrentUser().getUsername());
                        reqs.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e==null){
                                    adapter.removeAll();
                                    if (object!=null){
                                        //txtBookTitle.setText("Your Request is not accepted.");
                                        //txtDesc.setText("Please wait for some time.");
                                        //txtDonatorName.setVisibility(View.GONE);

                                        String donator = "";
                                        String title = "Your requested is yet to be accepted.";
                                        String desc = "Wait for some time.";

                                        MyRequestsList singleItem = new MyRequestsList(title,donator,desc);
                                        adapter.addItem(singleItem);

                                    }
                                }
                            }
                        });
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Error : " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
