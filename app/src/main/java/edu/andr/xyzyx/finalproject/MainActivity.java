package edu.andr.xyzyx.finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SlidingLeftView slidingLeftView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private void initview(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        fragmentManager=getFragmentManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        setSupportActionBar(toolbar);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragment=new DES_fragment();
        fragmentTransaction.add(R.id.fg_container,fragment);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
        drawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        fragmentTransaction=fragmentManager.beginTransaction();
        if (id == R.id.nav_des) {
            fragment=new DES_fragment();
            fragmentTransaction.replace(R.id.fg_container,fragment);
            // Handle the camera action
        } else if (id == R.id.nav_tdes) {
            fragment=new TriDES_fragment();
            fragmentTransaction.replace(R.id.fg_container,fragment);
        } else if (id == R.id.nav_aes) {
            fragment=new AES_fragment();
            fragmentTransaction.replace(R.id.fg_container,fragment);
        } else if (id == R.id.nav_ras) {
            fragment=new RSA_fragment();
            fragmentTransaction.replace(R.id.fg_container,fragment);
        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "share",
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(getApplicationContext(), "about",
                    Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_chacha) {
            fragment=new ChaCha_fragment();
            fragmentTransaction.replace(R.id.fg_container,fragment);
        }
        fragmentTransaction.commit();
        DrawerLayout drawer=(DrawerLayout)findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
