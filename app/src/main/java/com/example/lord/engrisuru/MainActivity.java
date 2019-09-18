package com.example.lord.engrisuru;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.lord.engrisuru.db.EngrisuruDatabase;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final boolean LEARNING_MODE = true;
    private static MainActivity mainActivity; // No memory leak because only one MainActivity may exist at given time

    public static Context getAppContext() {
        return mainActivity.getApplicationContext();
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        final File dbFile = this.getDatabasePath("engrisurudb.sqlite");
        if (!dbFile.exists()) {
            InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.kanjidic);
            Utils.FS.writeStreamToFile(is, dbFile);
        }
        EngrisuruDatabase.init();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null) return; // Avoid double fragment creation
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TranslationFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        if (true) return true;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_databases) {
            Log.i("ItemSelection", "Databases selected");
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, new DatabaseManipulationFragment())
                    .commit();
        } else if (id == R.id.nav_edit_translations) {

        } else if (id == R.id.nav_translate) {

            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.content_frame, new TranslationFragment()).
                    commit();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
