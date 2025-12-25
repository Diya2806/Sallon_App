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

public class Shop_navigation_menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar menutool;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_shop_navigation_menu);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, 0, systemBars.right, 0);
                return insets;
            });
            drawerLayout = findViewById(R.id.main);
            menutool = findViewById(R.id.shopmenubar);
            navigationView = findViewById(R.id.shopmenunavigation);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,menutool,R.string.menu_open,R.string.menu_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new ShopHomefragment());

            if(savedInstanceState==null){
              getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new ShopHomefragment()).commit();
                navigationView.setCheckedItem(R.id.navdashbord);
            }


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_LONG).show();

        if(menuItem.getTitle().equals("Dash Bord")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new ShopHomefragment()).commit();
        }

        if(menuItem.getTitle().equals("Services")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new Shop_Reg_Servicefragment()).commit();
        }
        if(menuItem.getTitle().equals("Customers")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new Shop_Reg_Customerfragment()).commit();

        }
        if (menuItem.getTitle().equals("Appointment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new Shop_Req_Appointmentfragment()).commit();

        }
        if(menuItem.getTitle().equals("FeedBack")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new Shop_Reg_Feedbackfragment()).commit();

        }
        if(menuItem.getTitle().equals("History")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new Shop_appointment_History_Fragment()).commit();

        }
        if(menuItem.getTitle().equals("Today Booking")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new Shop_Bookingfragment()).commit();

        }
        if(menuItem.getTitle().equals("Profile")){
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer,new Shop_Reg_profilefragment()).commit();

        }
        if(menuItem.getTitle().equals("LogOut")){
            Intent logout = new Intent(Shop_navigation_menu.this,Shop_login.class);
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

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.shopmenucontainer);
        if (currentFragment instanceof ShopHomefragment) {

            Intent main = new Intent(Shop_navigation_menu.this, Shop_login.class);
            startActivity(main);
            finish();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.shopmenucontainer, new ShopHomefragment()).commit();
        }
    }
}