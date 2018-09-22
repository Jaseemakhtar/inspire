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
import android.widget.Toast;

import com.jsync.freebook.CentralActivity;
import com.jsync.freebook.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by JSareen on 3/18/2018.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    EditText edtPhone,edtUsername, edtPassword, edtRePassword, edtEmail;
    Button btnRegister, btnToLogin;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    TextView txtError;
    Handler handler;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_layout,null);

        btnRegister = view.findViewById(R.id.btnRegister);
        btnToLogin = view.findViewById(R.id.btnToLogin);
        edtEmail = view.findViewById(R.id.edtEmailRegister);
        edtPassword = view.findViewById(R.id.edtPasswordRegister);
        edtRePassword = view.findViewById(R.id.edtRePasswordRegister);
        edtUsername = view.findViewById(R.id.edtUsernameRegister);
        edtPhone = view.findViewById(R.id.edtPhoneRegister);
        progressBar  = view.findViewById(R.id.progressBarRegister);
        progressBar.setVisibility(View.GONE);

        errorLayout = view.findViewById(R.id.errorLayout);
        errorLayout.setVisibility(View.GONE);

        txtError = view.findViewById(R.id.txtError);
        handler = new Handler();

        btnRegister.setOnClickListener(this);
        btnToLogin.setOnClickListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();
        return view;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister: attemptRegister();
                break;
            case R.id.btnToLogin: goToLogin();
                break;
        }
    }

    private void goToLogin() {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.right_enter,R.anim.left_out).replace(R.id.layoutContainer,new LoginFragment()).commit();
    }

    private void attemptRegister() {

        errorLayout.setVisibility(View.GONE);

        String phone = edtPhone.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String repassword = edtRePassword.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(repassword) && !repassword.equals(password) ){
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_password_identical));
            focusView = edtRePassword;
            ((EditText)focusView).selectAll();
            cancel = true;
        }else if (TextUtils.isEmpty(repassword)){
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_field_required));
            focusView = edtRePassword;
            ((EditText)focusView).selectAll();
            cancel = true;
        }

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

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_invalid_email));
            focusView = edtEmail;
            ((EditText)focusView).selectAll();
            cancel = true;
        }

        //Check for a valid username
        if(!TextUtils.isEmpty(username) && !isUsernameValid(username)){
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_invalid_username));
            focusView = edtUsername;
            ((EditText)focusView).selectAll();
            cancel = true;
        }
        if(TextUtils.isEmpty(username)){
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_field_required));
            focusView = edtUsername;
            cancel = true;
        }

        //Check for valid Phone number
        if (!TextUtils.isEmpty(phone) && !isValidPhone(phone)){
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_invalid_phone));
            focusView = edtPhone;
            ((EditText)focusView).selectAll();
            cancel = true;
        }else if (TextUtils.isEmpty(phone)){
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(getString(R.string.error_field_required));
            focusView = edtPhone;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            ParseUser user = new ParseUser();
            user.setUsername(phone);
            user.setPassword(password);
            user.setEmail(email);
            user.put("name",username);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Register Successful " , Toast.LENGTH_SHORT).show();
                        goToSecondActivity();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"Register Fail "+ e.getMessage() ,Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private boolean isValidPhone(String phone) {
        if (phone.length() != 10){
            Toast.makeText(getContext(),"Not 10 digits",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void goToSecondActivity() {
        Intent secondActivity = new Intent(getContext(),CentralActivity.class);
        startActivity(secondActivity);
    }

    private boolean isUsernameValid(String userName) {
        char[] name = userName.toCharArray();

        if(name.length < 3){
            return false;
        }

        for(int i = 0; i < 3 ; i++){
            int num = name[i];
            if(((num >= 48 && num <= 57))){
                return false;
            }
        }


        for(int i = 0; i < name.length ; i++){
            int num = name[i];
            if(!((num >= 65 && num <= 90) || (num >= 97 && num <= 122) || (num >= 48 && num <= 57))){
                return false;
            }

        }
        return true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
}
