package com.example.whatsupcolombo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //link to xml
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //to overcome the device rotation and activity destroys and recreate scenario
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ui_base, new Event_UI()).commit();
            navigationView.setCheckedItem(R.id.nav_event);
        }


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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        final Suggestion_UI sui = new Suggestion_UI();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ui_container) {
            logout();
            return true;
        }
        if (id == R.id.action_search) {
            Toast.makeText(HomeActivity.this,"dsdsd",Toast.LENGTH_LONG).show();
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    String x = s;
                    sui.firebaseSearch(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    //filter while type
                    sui.firebaseSearch(s);
                    return false;
                }
            });
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            //displays the particular fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.ui_base, new Profile_UI()).commit();
        } else if (id == R.id.nav_suggest) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ui_base, new Suggestion_UI()).commit();
        } else if (id == R.id.nav_event) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ui_base, new Event_UI()).commit();
        } else if (id == R.id.nav_restaurent) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ui_base, new Restaurent_UI()).commit();
        } else if (id == R.id.nav_logout) {
            Toast.makeText(HomeActivity.this, "Looged out", Toast.LENGTH_SHORT).show();
            logout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        Log.d("MyApp", "Logged out");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }
}
