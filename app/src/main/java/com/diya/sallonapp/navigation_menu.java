package com.diya.sallonapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class navigation_menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar menutool;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_navigation_menu);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, 0, systemBars.right, 0);
                return insets;


            });

            drawerLayout = findViewById(R.id.main);
            menutool = findViewById(R.id.menubar);
            navigationView =findViewById(R.id.menunavigation);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,menutool,R.string.menu_open,R.string.menu_close);

            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();



            navigationView.setNavigationItemSelectedListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer,new Homefragment());


            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer,new Homefragment()).commit();
                navigationView.setCheckedItem(R.id.navdashbord);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_LONG).show();

        if(menuItem.getTitle().equals("Dash Bord")){
            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Homefragment()).commit();
        }
        if(menuItem.getTitle().equals("Shop Owner")){

            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Shopfragment()).commit();
        }
        if(menuItem.getTitle().equals("Customers")){

            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Customerfragment()).commit();
        }
        if(menuItem.getTitle().equals("Service Type")){

            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new ServiceList_fragment()).commit();
        }
        if(menuItem.getTitle().equals("Service Sub Type")){
            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new ServiceSubList_fragment()).commit();
        }
        if(menuItem.getTitle().equals("Booking")){
            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer,new TotalBookingfragment()).commit();
        }
//        if(menuItem.getTitle().equals("State")){
//            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Statefragment()).commit();
//
//        }
//        if(menuItem.getTitle().equals("City")){
//            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Cityfragment()).commit();
//        }
        if(menuItem.getTitle().equals("Reports")){
            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Reportfragment()).commit();
        }
        if(menuItem.getTitle().equals("Profile")){
            getSupportFragmentManager().beginTransaction().replace(R.id.menucontainer, new Admin_Profilefragment()).commit();
        }
        if(menuItem.getTitle().equals("LogOut")){
            Intent logout = new Intent(navigation_menu.this,MainActivity.class);
            startActivity(logout);
        }


        drawerLayout.closeDrawer(GravityCompat.START);


        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.menucontainer);

        if (currentFragment instanceof Homefragment) {

            Intent main = new Intent(navigation_menu.this, MainActivity.class);
            startActivity(main);
            finish();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menucontainer, new Homefragment())
                    .commit();
        }

    }
}