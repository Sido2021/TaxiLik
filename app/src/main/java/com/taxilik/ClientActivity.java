package com.taxilik;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.client.home.offre.ClientOffreFragment;
import com.taxilik.client.profile.ClientProfileFragment;
import com.taxilik.driver.home.DriverHomeFragment;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ClientActivity extends AppCompatActivity implements ClientHomeFragment.OnFragmentInteractionListener
  , ClientOffreFragment.OnFragmentInteractionListener,ClientProfileFragment.OnFragmentInteractionListener{

    ImageView menuDrawer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.parent_fragment_container, new ClientHomeFragment());
        ft.commit();

        menuDrawer = findViewById(R.id.menu_drawer);
        menuDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.open();
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.nav_home :{
                        ft.replace(R.id.parent_fragment_container, new ClientHomeFragment());
                        break;
                    }
                    case R.id.nav_profile :{

                        ft.replace(R.id.parent_fragment_container, new ClientProfileFragment());
                        break;
                    }
                    case R.id.nav_settings :{

                        //ft.replace(R.id.parent_fragment_container, new DriverHomeFragment());
                        break;
                    }
                    case R.id.nav_about:{

                        //ft.replace(R.id.parent_fragment_container, new DriverHomeFragment());
                        break;
                    }
                }

                ft.commit();
                return true;
            }
        });
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toptoolbar, menu);
        return true;
    }

    @Override
    public void messageFromParentFragment(Uri uri) {

    }

    @Override
    public void messageFromChildFragment(Uri uri) {

    }
}