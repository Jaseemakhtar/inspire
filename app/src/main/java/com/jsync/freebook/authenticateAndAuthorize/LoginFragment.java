package com.jsync.freebook.authenticateAndAuthorize;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jsync.freebook.CentralActivity;
import com.jsync.freebook.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by JSareen on 3/18/2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText edtPhone, edtPassword;
    Button btnLogin, btnToRegister;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    TextView txtError;
    Handler handler;
    FragmentManager fragmentManager;
    Fragment mContent;

    public LoginFragment(){

    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout,null);
        edtPhone = view.findViewById(R.id.edtPhoneLogin);
        edtPassword = view.findViewById(R.id.edtPasswordLogin);

        btnLogin = view.findViewById(R.id.btnLogin);
        btnToRegister = view.findViewById(R.id.btnToRegister);

        progressBar  = view.findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.GONE);

        errorLayout = view.findViewById(R.id.errorLayout);
        errorLayout.setVisibility(View.GONE);

        txtError = view.findViewById(R.id.txtError);
        handler = new Handler();

        btnLogin.setOnClickListener(this);
        btnToRegister.setOnClickListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.btnToRegister: goToRegister();
                break;
        }
    }

    private void goToSecondActivity() {
        Intent centralActivity = new Intent(getContext(),CentralActivity.class);
        startActivity(centralActivity);
    }

    void userLogin() {
        errorLayout.setVisibility(View.GONE);
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            ((EditText)focusView).selectAll();
            cancel = true;
        }else if (TextUtils.isEmpty(password)) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_field_required));
            focusView = edtPassword;
            cancel = true;
        }



        if (TextUtils.isEmpty(phone)) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_field_required));
            focusView = edtPhone;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_invalid_phone));
            focusView = edtPhone;
            ((EditText)focusView).selectAll();
            cancel = true;
        }else if (phone.length() != 10){
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_invalid_phone_length));
            focusView = edtPhone;
            ((EditText)focusView).selectAll();
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(phone, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e==null){
                        progressBar.setVisibility(View.GONE);
                        goToSecondActivity();
                    }else {
                        progressBar.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        txtError.setText("Server Error: " + e.getMessage());
                    }

                }
            });
        }
    }

    private void goToRegister() {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.right_enter,R.anim.left_out).replace(R.id.layoutContainer,new RegisterFragment()).commit();
    }

    private boolean isPhoneValid(String phone) {
        if (phone.length() != 10){
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
}
