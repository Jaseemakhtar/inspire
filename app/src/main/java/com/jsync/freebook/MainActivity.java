package com.jsync.freebook;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jsync.freebook.authenticateAndAuthorize.LoginFragment;

public class MainActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.layoutContainer,loginFragment);
        fragmentTransaction.commit();
    }




}
