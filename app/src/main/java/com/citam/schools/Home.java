package com.citam.schools;

import android.annotation.TargetApi;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;



public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    Fragment fragment = null;
    NavigationView navigationView;
    View contentView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


        drawer = findViewById(R.id.drawer_layout);
        contentView = findViewById(R.id.content_frame);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);

        toggle.syncState();


        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new Fragment_Home());
        tx.commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    // Handle navigation view item clicks

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        displaySelectedScreen(id);
        return true;
    }

    private void displaySelectedScreen(int itemId) {


        switch (itemId) {
            case R.id.nav_home:
                fragment = new Fragment_Home();

                break;
            case R.id.admission:
                fragment=new FragmentAdmission();

break;

            case R.id.nav_about:
                startActivity(new Intent(Home.this, AboutApp.class));
                break;

            case R.id.nav_contact:
                fragment = new Fragment_Contact();
                /*if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Contact Us");
                }*/
                break;


            case R.id.nav_share:
                String shareBody = "https://play.google.com/store/apps/details?id=com.citam.schools";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CITAM Schools");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, ("Share Using")));
                break;

            default:
                fragment = new Fragment_Home();

                break;

        }
        drawer.closeDrawer(GravityCompat.START);


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


}
