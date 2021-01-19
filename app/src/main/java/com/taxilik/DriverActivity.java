package com.taxilik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.taxilik.about.AboutFragment;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.driver.home.DriverHomeFragment;
import com.taxilik.driver.home.request.DriverRequestFragment;
import com.taxilik.driver.profile.DriverProfileFragment;
import com.taxilik.settings.SettingsFragment;

import static com.taxilik.Data.CurrentUser;

public class DriverActivity extends AppCompatActivity implements DriverHomeFragment.OnFragmentInteractionListener ,
        DriverRequestFragment.OnFragmentInteractionListener {

    ImageView menuDrawer ;

    FirebaseUser currentUser ;
    TextView userName ;
    ImageView userImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        userName = header.findViewById(R.id.text_view_username);
        userImage = header.findViewById(R.id.profile_image);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.parent_fragment_container, new DriverHomeFragment());
        ft.commit();

        menuDrawer = findViewById(R.id.menu_drawer);
        menuDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.nav_home :{
                        ft.replace(R.id.parent_fragment_container, new DriverHomeFragment());
                        break;
                    }
                    case R.id.nav_profile :{
                        ft.replace(R.id.parent_fragment_container, new DriverProfileFragment());
                        break;
                    }
                    case R.id.nav_settings :{
                        ft.replace(R.id.parent_fragment_container, new SettingsFragment());
                        break;
                    }

                    case R.id.nav_about:{
                        ft.replace(R.id.parent_fragment_container, new AboutFragment());
                        break;
                    }
                    case R.id.nav_logout:{
                        logout();
                        break;
                    }
                }

                ft.commit();
                drawer.close();
                return true;
            }
        });

        userName.setText(CurrentUser.getFullName());
        if(!CurrentUser.getImage().equals("") && CurrentUser.getImage()!=null) Picasso.get().load(CurrentUser.getImage()).into(userImage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.partager){ Toast.makeText(getApplicationContext(),"partager",Toast.LENGTH_SHORT).show();}
        if (id == R.id.about){Toast.makeText(getApplicationContext(),"A propos",Toast.LENGTH_SHORT).show();}
        if (id == R.id.exit){Toast.makeText(getApplicationContext(),"Quitter",Toast.LENGTH_SHORT).show();}
        if (id == R.id.search){Toast.makeText(getApplicationContext(),"Rechercher",Toast.LENGTH_SHORT).show();}
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toptoolbar, menu);
        return true;
    }

    @Override
    public void messageFromParentFragment(Uri uri) {

    }

    @Override
    public void messageFromChildFragment(Uri uri) {

    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LoginActivity2.class));
        finish();
    }
}