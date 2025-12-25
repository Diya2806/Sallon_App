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

public class Customer_Navigation_menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar menutool;
    private NavigationView navigationView;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_customer_navigation_menu);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, 0, systemBars.right, 0);
                return insets;
            });
            drawerLayout = findViewById(R.id.main);
            menutool = findViewById(R.id.customermenubar);
            navigationView = findViewById(R.id.customermenunavigation);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,menutool,R.string.menu_open,R.string.menu_close);
            customerId = getIntent().getStringExtra("customerId");

            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.customermenucontainer,new Customer_Homefragment());


            if(savedInstanceState == null){
                Customer_Homefragment homeFragment = new Customer_Homefragment();
                Bundle bundle = new Bundle();
                bundle.putString("customerId", customerId);
                homeFragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.customermenucontainer, homeFragment)
                        .commit();

                navigationView.setCheckedItem(R.id.Cmenudashbord);
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();

        if(menuItem.getTitle().equals("Dash Bord")){

                Customer_Homefragment homeFragment = new Customer_Homefragment();
                Bundle bundle = new Bundle();
                bundle.putString("customerId", customerId);
                homeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.customermenucontainer, homeFragment).commit();

        }
        if(menuItem.getTitle().equals("Appointment")){
            Customer_Appointmentfragmnet appointmentFragment = new Customer_Appointmentfragmnet();
            Bundle bundle = new Bundle();
            bundle.putString("customerId", customerId);
            appointmentFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.customermenucontainer, appointmentFragment)
                    .commit();
        }
        if(menuItem.getTitle().equals("History")){
            Customer_Historyfragment history = new Customer_Historyfragment();
            Bundle bundle = new Bundle();
            bundle.putString("customerId", customerId);
            history.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.customermenucontainer, history)
                    .commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.customermenucontainer,new Customer_Historyfragment()).commit();

        }
        if(menuItem.getTitle().equals("Profile")){
            Customer_Profilefragment profile = new Customer_Profilefragment();

            Bundle bundle = new Bundle();
            bundle.putString("customerId", customerId);
            profile.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.customermenucontainer,profile).commit();

        }

        if(menuItem.getTitle().equals("FeedBack")){
            Customer_FeedBackFragment Feedback = new Customer_FeedBackFragment();

            Bundle bundle = new Bundle();
            bundle.putString("customerId", customerId);
            Feedback.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.customermenucontainer,Feedback).commit();
        }
        if (menuItem.getTitle().equals("LogOut")){
            Intent Homes = new Intent(Customer_Navigation_menu.this,MainActivity.class);
            startActivity(Homes);
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

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.customermenucontainer);

        if (currentFragment instanceof Homefragment) {

            Intent main = new Intent(Customer_Navigation_menu.this, Customer_Login.class);
            startActivity(main);
            finish();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.customermenucontainer, new Customer_Homefragment())
                    .commit();
        }

    }
}