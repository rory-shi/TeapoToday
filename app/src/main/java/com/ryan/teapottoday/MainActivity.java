package com.ryan.teapottoday;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ryan.teapottoday.database.MyDatabaseHelper;
import com.ryan.teapottoday.fragments.CollectionFragment;
import com.ryan.teapottoday.fragments.FirstPageFragment;
import com.ryan.teapottoday.fragments.ProductionFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mToolbar;
   // private FloatingActionButton mFab;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private int drawerItemId;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatabase();

        initView();

        initEvent();
    }

    private void initDatabase() {
       /* dbHelper = new MyDatabaseHelper(this, "Teapot.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert()*/
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //mFab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private void initEvent() {
        //set toolbar
        setSupportActionBar(mToolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.contentFragment, new FirstPageFragment())
                .commit();



        //init FAB
        /*mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //sync Drawer
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        //init navi
        mNavigationView.setNavigationItemSelectedListener(this);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (drawerItemId) {
            default:
            case R.id.nav_first_page:
                menu.clear();
                menu.add(1,R.menu.main,0,"首页");
                break;
            case R.id.nav_collection:
                menu.clear();
                menu.add(1,R.menu.fragment_collection,0,"收藏");
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"首页",Toast.LENGTH_LONG).show();
            return true;
        }/*
        if (id == R.id.action_all_choose) {
            Toast.makeText(this,"全选",Toast.LENGTH_LONG).show();
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        drawerItemId = item.getItemId();
        //1 R.id.nav_first_page 2  R.id.nav_collection 3R.id.nav_history 4R.id.nav_manufacture 5R.id.nav_set R.id.nav_about

        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        switch (drawerItemId) {
            default:
            case R.id.nav_first_page:
                fragment = new FirstPageFragment();
                break;
            case R.id.nav_collection:
                fragment = new CollectionFragment();
                break;
            case R.id.nav_production:
                fragment = new ProductionFragment();
                break;

        }
        fragmentManager.beginTransaction()
                .replace(R.id.contentFragment,fragment)
                .commit();
        //fragmentManager.executePendingTransactions();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}


