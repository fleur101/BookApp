package com.gaukhar.dauzhan.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="DRAWER_ACTIVITY_TAG";


    public FrameLayout mContentFrame;
    public DrawerLayout mDrawerLayout;
    public NavigationView mNavigationView;
    public ActionBarDrawerToggle mDrawerToggle;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//          mDrawerLayout.setStatusBarBackgroundColor(getColor(R.color.colorPrimary));
//
//        }
          mContentFrame = (FrameLayout) findViewById(R.id.content_frame);



        mDrawerToggle.setDrawerIndicatorEnabled(true);

        if (getSupportActionBar()!=null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar()!=null)getSupportActionBar().setHomeButtonEnabled(true);

        }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce || getFragmentManager().getBackStackEntryCount()!=0) {
                moveTaskToBack(true);
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Нажмите еще раз 'Назад', чтобы выйти", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 3000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
            return super.onOptionsItemSelected(item);
        // Handle your other action bar items...
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.bookcoaster) {
            // Call your Action
            Log.e(TAG, "selectItem: excerpts");
            intent = new Intent(this, ReadActivity.class);
        } else if (id == R.id.my_books) {
            // Call your Action
            Log.e(TAG, "selectItem: mylist");
            intent = new Intent(this, MyListActivity.class);
        } else if (id == R.id.settings) {
            // Call your Action
            Log.e(TAG, "selectItem: settings");
            intent = new Intent(this, SettingsActivity.class);
        } else if (id == R.id.about_app){
            Log.e(TAG, "selectItem: about app");
            intent = new Intent(this, AboutAppActivity.class);
        } else {
            Log.e(TAG, "selectItem: other");
            intent = new Intent(this, ReadActivity.class);
        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }



}
