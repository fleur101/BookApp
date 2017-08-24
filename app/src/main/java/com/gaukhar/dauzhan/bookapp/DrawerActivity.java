package com.gaukhar.dauzhan.bookapp;

import android.content.Intent;
import android.graphics.Typeface;
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


public class DrawerActivity extends AppCompatActivity  {

    private static final String TAG ="DRAWER_ACTIVITY_TAG";


    public FrameLayout mContentFrame;
    public DrawerLayout mDrawerLayout;
    public NavigationView mNavigationView;
    public ActionBarDrawerToggle mDrawerToggle;
    public static Typeface font_helvetica;
    public static Typeface font_kurale;
    public static Typeface font_ringbear;
    public static Typeface font_roboto;

    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        font_helvetica =  Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");
        font_kurale = Typeface.createFromAsset(getAssets(), "fonts/kurale.ttf");
      //  font_ringbear = Typeface.createFromAsset(getAssets(), "fonts/ringbear.ttf");
        font_roboto = Typeface.DEFAULT;
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
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                              @Override
                                                              public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Log.e(TAG, "onNavigationItemSelected: "+item.setChecked(true));
                int id = item.getItemId();
                Intent intent = null;
                switch(id){
                case R.id.bookcoaster:
                  Log.e(TAG, "selectItem: excerpts");
                  intent = new Intent(getApplicationContext(), ReadActivity.class);
                  break;
                case  R.id.quiz:
                    Log.e(TAG, "selectItem: mylist");
                    intent = new Intent(getApplicationContext(), QuizActivity.class);
                    break;
                case  R.id.my_books:
                  Log.e(TAG, "selectItem: mylist");
                  intent = new Intent(getApplicationContext(), MyListActivity.class);
                  break;
                case R.id.settings:
                  Log.e(TAG, "selectItem: settings");
                  intent = new Intent(getApplicationContext(), SettingsActivity.class);
                  break;
                case R.id.about_app:
                  Log.e(TAG, "selectItem: about app");
                  intent = new Intent(getApplicationContext(), AboutAppActivity.class);
                  break;
                default:
                  Log.e(TAG, "selectItem: other???");
                  return false;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(intent);
                return true;
                }
                });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//          mDrawerLayout.setStatusBarBackgroundColor(getColor(R.color.colorPrimary));
//
//        }
        setNavigationViewCheckedItem();
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        if (getSupportActionBar()!=null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar()!=null)getSupportActionBar().setHomeButtonEnabled(true);

        }

    private void setNavigationViewCheckedItem() {

        if (this.getClass().equals(ReadActivity.class)){
            mNavigationView.setCheckedItem(R.id.bookcoaster);
        } else if (this.getClass().equals(MyListActivity.class)){
            mNavigationView.setCheckedItem(R.id.my_books);
        }if (this.getClass().equals(SettingsActivity.class)){
            mNavigationView.setCheckedItem(R.id.settings);
        }if (this.getClass().equals(AboutAppActivity.class)){
            mNavigationView.setCheckedItem(R.id.about_app);
        }
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





}
