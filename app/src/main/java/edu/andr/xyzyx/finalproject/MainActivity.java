package edu.andr.xyzyx.finalproject;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.andr.xyzyx.MyUtil.ConstantArgument;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ConstantArgument{
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
        checkandcreatefile();
//        PackageManager packageManager = null;
//        ApplicationInfo applicationInfo = null;
//        try {
//            packageManager = getApplicationContext().getPackageManager();
//            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            applicationInfo = null;
//        }
//        String applicationName =
//                (String) packageManager.getApplicationLabel(applicationInfo);
//        Log.i("test",applicationName);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        fragmentManager=getFragmentManager();
    }

    private void checkandcreatefile() {
        for (int i=0;i<TEST_FILE_NUM;i++) {
            try {
                FileOutputStream fos1 = getApplicationContext().openFileOutput(DESOUT[i], Context.MODE_PRIVATE);
                FileOutputStream fos2 = getApplicationContext().openFileOutput(AESOUT[i], Context.MODE_PRIVATE);
                FileOutputStream fos3 = getApplicationContext().openFileOutput(TDESOUT[i], Context.MODE_PRIVATE);
                FileOutputStream fos4 = getApplicationContext().openFileOutput(RSAOUT[i], Context.MODE_PRIVATE);
                FileOutputStream fos5 = getApplicationContext().openFileOutput(CHAOUT[i], Context.MODE_PRIVATE);
                fos1.write("".getBytes());
                fos1.close();
                fos2.write("".getBytes());
                fos2.close();
                fos3.write("".getBytes());
                fos3.close();
                fos4.write("".getBytes());
                fos4.close();
                fos5.write("".getBytes());
                fos5.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
