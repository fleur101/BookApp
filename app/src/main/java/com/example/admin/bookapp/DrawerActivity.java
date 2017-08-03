package com.example.admin.bookapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class DrawerActivity extends AppCompatActivity {

    private ListView mDrawerList;


    public FrameLayout mContentFrame;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;
    private String[] drawerListItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerListItems = new String[]{getString(R.string.bookcoaster), getString(R.string.my_list), getString(R.string.settings), getString(R.string.contact_developer)};

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
        mDrawerToggle.syncState();
    //    NavigationView navigationView = (NavigationView) findViewById(R.id.left_drawer);
     //   navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDrawerLayout.setStatusBarBackgroundColor(getColor(R.color.colorPrimaryDark));
        }
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);


        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.activity_drawer_header, null, false);
        mDrawerList.addHeaderView(listHeaderView);

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.activity_drawer_list_item, drawerListItems));
        mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//        CharSequence mDrawerTitle;
//        CharSequence mTitle = mDrawerTitle = getTitle();


        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e("ScreenSlide", "post create");
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
  //      getMenuInflater().inflate(R.menu.drawer, menu);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        Intent intent = null;
//        if (id == R.id.bookcoaster) {
//            intent = new Intent(this, ReadPagerActivity.class);
//        } else if (id == R.id.my_books) {
//            intent = new Intent(this, MyListActivity.class);
//        } else if (id == R.id.settings) {
//            intent = new Intent(this, MyListActivity.class);
//        } else if (id == R.id.nav_share) {
//            intent = new Intent(this, MyListActivity.class);
//        } else if (id == R.id.contact) {
//            intent = new Intent(this, MyListActivity.class);
//        }
//        startActivity(intent);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

        }


    }

    private void selectItem(int position){

        Intent intent = null;
        switch(position){
            case 1:
                intent = new Intent(this, ReadPagerActivity.class);
                break;
            case 2:
                intent = new Intent(this, MyListActivity.class);
                break;
            case 3:
                intent = new Intent(this, SettingsActivity.class);
                break;
            default:
                intent = new Intent (this, MyListActivity.class);
                break;

        }
        mDrawerList.setItemChecked(position, true);
        getSupportActionBar().setTitle(drawerListItems[position]);
        startActivity(intent);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
