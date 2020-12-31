package com.puspak.habraCarService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingactionbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerlayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        View navView = navigationView.getHeaderView(0);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        floatingactionbutton = findViewById(R.id.btFab);

        setupToolbar();

        setupNavigationDrawer();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavigationDrawer() {
        drawerToggle = new ActionBarDrawerToggle(
                this,drawerlayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}