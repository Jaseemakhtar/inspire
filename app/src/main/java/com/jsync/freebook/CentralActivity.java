package com.jsync.freebook;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.jsync.freebook.tabOne.TabOne;
import com.jsync.freebook.tabThree.TabThree;
import com.jsync.freebook.tabTwo.MyRequests;
import com.jsync.freebook.tabTwo.TabTwo;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class CentralActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView txtName, txtEmail;
    Handler handler = new Handler();
    boolean backState = false;

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Press once again to exit",Toast.LENGTH_SHORT).show();
        if (backState){
            finishAffinity();
        }else {
            backState = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    backState = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        View header = navigationView.getHeaderView(0);
        txtEmail = header.findViewById(R.id.txtLEmail);
        txtName = header.findViewById(R.id.txtLName);

        txtName.setText(ParseUser.getCurrentUser().getString("name"));
        txtEmail.setText(ParseUser.getCurrentUser().getEmail());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btnLogout){
                    ParseUser.logOutInBackground(new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Intent logout = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(logout);
                                finish();
                            }
                        }
                    });
                }else if (item.getItemId() == R.id.btnMyRequests){
                    Intent myreqs = new Intent(CentralActivity.this,MyRequests.class);
                    startActivity(myreqs);
                }
                Toast.makeText(getApplicationContext(),item.getTitle() + " selected",Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                return true;
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

           switch (position){
               case 0 : TabOne tabOne = new TabOne();
                   return tabOne;

               case 1 : TabTwo tabTwo = new TabTwo();
                    return  tabTwo;

               case 2 :
                   TabThree tabThree = new TabThree();
                   return tabThree;
           }
           return null;
        }

        @Override
        public int getCount() {

            return 3;
        }
    }
}