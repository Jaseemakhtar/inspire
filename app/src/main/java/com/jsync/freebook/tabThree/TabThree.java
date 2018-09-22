package com.jsync.freebook.tabThree;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jsync.freebook.CustomDialog;
import com.jsync.freebook.ImageCropperActivity;
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

import static android.app.Activity.RESULT_OK;


public class TabThree extends Fragment {
    FloatingActionButton mFab;
    RecyclerView recyclerView;

    View v;

    public static final int IMAGE_REQ = 425;
    public static final int IMAGE_CROP_REQ = 454;

    byte[] bytes;

    OldBooksListRecyclerViewAdapter adapter;
    List<OldBookListItems> listItems;

    Handler handler;
    boolean firstTime = true;
    int itemCount;

    public TabThree() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_three,container,false);
        mFab = view.findViewById(R.id.fabAddOldBook);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog();
                customDialog.setmListener(onClickListener);
                customDialog.setCancelable(false);
                customDialog.show(getFragmentManager(),"OldBook");

            }
        });
        v = view;
        /*recyclerView = view.findViewById(R.id.oldBooksRecyclerView);*/
        listItems = new ArrayList<>();
        handler = new Handler();
        loadRecyclerView();
        return view;
    }

    CustomDialog.CustomDialogClickListener onClickListener = new CustomDialog.CustomDialogClickListener() {
        @Override
        public void onClickListener(View view,String oldBookName, String oldBookDesc) {

            switch (view.getId()){
                case R.id.btnOldBookUpload:
                    uploadOldBook(oldBookName,oldBookDesc);

                    break;
                case R.id.btnOldBookSelect:
                    Intent imageSelect = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageSelect,IMAGE_REQ);
                    break;


            }
        }
    };

    OldBooksListRecyclerViewAdapter.OldBooksRecyclerViewClickListener mRecyclerClickListener = new OldBooksListRecyclerViewAdapter.OldBooksRecyclerViewClickListener() {
        @Override
        public void onRecyclerViewClick(View view, final int pos) {

            if (view.getId() == R.id.btnOldBookGet) {
                ParseObject oldBookReq = new ParseObject("OldBookRequests");

                oldBookReq.put("bookTitle", listItems.get(pos).getBookTitle());
                oldBookReq.put("bookDesc", listItems.get(pos).getBookDesc());
                oldBookReq.put("donatorName", listItems.get(pos).getDonatorName());
                oldBookReq.put("requestedBy", ParseUser.getCurrentUser().getUsername());

                oldBookReq.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            /*Toast.makeText(getContext(), "Your request was successfull", Toast.LENGTH_SHORT).show();*/
                            ParseQuery<ParseObject> submit = ParseQuery.getQuery("OldBooks");
                            submit.whereEqualTo("donatorName", listItems.get(pos).getDonatorName());
                            submit.whereEqualTo("bookTitle", listItems.get(pos).getBookTitle());
                            submit.setLimit(1);
                            submit.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects != null && objects.size() > 0) {
                                            for (ParseObject object : objects) {
                                                object.put("requestedBy", ParseUser.getCurrentUser().getUsername());
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Toast.makeText(getContext(), "Successfully Requested", Toast.LENGTH_SHORT).show();
                                                            adapter.removeItem(pos);
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
                });

            }
        }
    };

    private void uploadOldBook(String oldBookName, String oldBookDesc) {
        if (oldBookName != null && oldBookDesc != null ) {
            if (bytes == null) {
                Toast.makeText(getContext(), "You didn't Selected an Image", Toast.LENGTH_SHORT).show();
            }
            if (!TextUtils.isEmpty(oldBookDesc) && !TextUtils.isEmpty(oldBookName) && bytes != null) {

                ParseObject oldBookObject = new ParseObject("OldBooks");

                ParseFile oldBookImage = new ParseFile("oldbookimage.jpg", bytes);

                oldBookObject.put("bookTitle", oldBookName);
                oldBookObject.put("bookDesc", oldBookDesc);
                oldBookObject.put("donatorName", ParseUser.getCurrentUser().getString("name"));
                oldBookObject.put("donatorPhone",ParseUser.getCurrentUser().getUsername());
                oldBookObject.put("bookImage", oldBookImage);
                oldBookObject.put("requestedBy", "");
                oldBookObject.saveInBackground( new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "Uploaded Your book successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IMAGE_REQ:
                if (resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    Intent imageCropper = new Intent(getContext(),ImageCropperActivity.class);
                    imageCropper.putExtra("URI",uri.toString());
                    startActivityForResult(imageCropper,IMAGE_CROP_REQ);
                }
                break;
            case IMAGE_CROP_REQ:

                if (resultCode == RESULT_OK){
                    bytes = data.getByteArrayExtra("IMAGEBYTESIMAGEBYTES");

                }
                break;

        }

    }

    public void loadRecyclerView(){

        ParseQuery<ParseObject> loadQuery = ParseQuery.getQuery("OldBooks");
        loadQuery.whereEqualTo("requestedBy","");
        loadQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e ==  null){
                    if (objects!=null && objects.size() > 0){
                        if (firstTime){
                            firstTime = false;
                            itemCount = objects.size();
                            listItems.clear();
                            for (ParseObject object: objects){
                                String bookTitle = object.getString("bookTitle");
                                String bookDesc = object.getString("bookDesc");
                                String donatorName = object.getString("donatorName");
                                ParseFile oldBookImage = object.getParseFile("bookImage");

                                OldBookListItems singleItem = new OldBookListItems(bookTitle,bookDesc,donatorName,oldBookImage);
                                listItems.add(singleItem);
                            }
                            recyclerView = v.findViewById(R.id.oldBooksRecyclerView);
                            adapter = new OldBooksListRecyclerViewAdapter(getContext(), listItems);
                            adapter.setRecyclerViewClickListener(mRecyclerClickListener);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter);

                        }else{
                            if (itemCount != objects.size()){
                                itemCount = objects.size();
                                listItems.clear();
                                for (ParseObject object: objects){
                                    String bookTitle = object.getString("bookTitle");
                                    String bookDesc = object.getString("bookDesc");
                                    String donatorName = object.getString("donatorName");
                                    ParseFile oldBookImage = object.getParseFile("bookImage");

                                    OldBookListItems singleItem = new OldBookListItems(bookTitle,bookDesc,donatorName,oldBookImage);
                                    listItems.add(singleItem);
                                }
                                recyclerView = v.findViewById(R.id.oldBooksRecyclerView);
                                adapter = new OldBooksListRecyclerViewAdapter(getContext(), listItems);
                                adapter.setRecyclerViewClickListener(mRecyclerClickListener);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadRecyclerView();
                            }
                        },7000);


                    }else {
                        Toast.makeText(getContext(),"No data to load",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
