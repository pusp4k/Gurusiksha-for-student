package com.androweb.voyage;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androweb.voyage.CustomDialog.CustomProgressDialog;
import com.androweb.voyage.Fragment.FragmentHome;
import com.androweb.voyage.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private CircleImageView imgUser;
    private TextView txtUserName;
    private TextView txtUserMobile;
    private CustomProgressDialog customDialog;
    private FragmentManager fragmentManager;
    private ContentFrameLayout container;
    private ImageView ivCover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerlayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        View navView = navigationView.getHeaderView(0);
        ivCover = navView.findViewById(R.id.ivCover);
        imgUser = navView.findViewById(R.id.ivLogo);
        txtUserName = navView.findViewById(R.id.userName);
        txtUserMobile = navView.findViewById(R.id.userPhone);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        container = findViewById(R.id.content_frame);

        customDialog = new CustomProgressDialog(this);
        customDialog.showProgress();

        setupToolbar();

        setupNavigationDrawer();

        setupNavigationHeaderUi();

        fragmentManager = getSupportFragmentManager();

        openHomeFragment();
    }

    private void openHomeFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //FragmentExplore fragmentExplore = FragmentExplore.newInstance(this);
        FragmentHome fragmentHome = FragmentHome.newInstance();
        //ft.replace(R.id.content_frame, fragmentExplore);
        ft.replace(R.id.content_frame, fragmentHome);
        ft.commit();
        customDialog.processFinished();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    private void setupNavigationDrawer() {
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        drawerlayout = findViewById(R.id.drawerLayout);
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_menu_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationHeaderUi() {
        Picasso.get()
                .load(R.drawable.app_header_bg)
                .into(ivCover);

        Picasso.get()
                .load(R.drawable.ic_user_default) // TODO CHANGE WITH USER PHOTO
                .placeholder(R.drawable.ic_user_default)
                .error(R.drawable.ic_user_default)
                .into(imgUser);

        String userName = Utils.getUserName(this);
        String userMobile = Utils.getUserMobile(this);

        if (userName == null || userName.equals("")) {
            txtUserName.setText("Voyagers");
        } else {
            txtUserName.setText(userName);
        }

        if (userMobile == null || userMobile.equals("")) {
            txtUserMobile.setText("9876500001");
        } else {
            txtUserMobile.setText(userMobile);
        }
    }
}