package com.taxilik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class indexUser extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_user);
        // bottom navigation
        BottomNavigationView BottomNav = findViewById(R.id.bottom_navigation);
        BottomNav.setOnNavigationItemSelectedListener(navListener);
        // header
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //listView
        ListView listTxi=(ListView) findViewById(R.id.listViewTaxi);

        Car cars1 =new Car(1,"icon2","chaimae taj","mat:23565356");
        Car cars2 =new Car(1,"icon2","hello hello","mat:23556");
        Car cars3 =new Car(1,"icon2","chaimae taj","mat:235454556");
        Car cars4 =new Car(1,"icon2","hello hello","mat:23565356");
        Car cars5 =new Car(1,"icon2","chaimae taj","mat:23565356");
        Car cars6 =new Car(1,"icon2","chaimae taj","mat:23565356");
        Car[] listcars={cars1,cars2,cars3,cars4,cars5,cars6};
        //intialise wmlfile
        int xmlFile=R.layout.car_offre_layout;
        //call adapter
        //OffreAdapter adapter=new OffreAdapter(indexUser.this,xmlFile,listcars);
        //listTxi.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toptoolbar,menu);
        return true;
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



    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.nav_home:
                    Toast.makeText(getApplicationContext(),"Acceil user",Toast.LENGTH_SHORT).show();
                    // reflech to
                    break;

                case R.id.nav_historique:
                    Toast.makeText(getApplicationContext(),"Historique user",Toast.LENGTH_SHORT).show();
                /*    Intent intent = new Intent(MainActivity.this, historiqueUser.class);
                    startActivity(intent);*/
                    // reflech to
                    break;
                case R.id.nav_profile:
                    Toast.makeText(getApplicationContext(),"profile user",Toast.LENGTH_SHORT).show();
                    // Intent intent2 = new Intent(MainActivity.this, ProfileUser.class);
                    // startActivity(intent2);
                    // reflech to
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }


            return true;
        }
    };
}