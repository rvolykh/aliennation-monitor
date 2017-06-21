package mu.zz.pikaso.aliennationmonitor;



import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import mu.zz.pikaso.aliennationmonitor.adapters.FindCharAdapter;
import mu.zz.pikaso.aliennationmonitor.representation.Character;
import mu.zz.pikaso.aliennationmonitor.services.ServiceRunner;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FindCharAdapter.ListCharsSelector,
        FavoritesFragment.CharacterFavoritesInteraction
{

    private Fragment charInfoFragment,searchFragment,settingsFragment,favoritesFragment;
    private ServiceRunner service;

    public MainActivity(){
        searchFragment = new FindCharFragment();
        charInfoFragment = new CharInfoFragment();
        settingsFragment = new SettingsFragment();
        favoritesFragment = new FavoritesFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, favoritesFragment);
        ft.commit();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.landscapeView);
        } else {
            setContentView(R.layout.portraitView);
        }
        */
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_search) {
            //SEARCH FORM
            while(getSupportFragmentManager().getBackStackEntryCount()>0)
                getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, searchFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_refresh) {
            //REFRESH ACTION
            if(charInfoFragment.isVisible()){
                ((CharInfoFragment)charInfoFragment).refresh();
            }else if (favoritesFragment.isVisible()){
                ((FavoritesFragment) favoritesFragment).refresh();
            }else if(searchFragment.isVisible()){
                ((FindCharFragment) searchFragment).refresh();
            }

        }
        else if (id == R.id.nav_main){
            //FAVORITES CHARACTERS LIST
            while(getSupportFragmentManager().getBackStackEntryCount()>0)
                getSupportFragmentManager().popBackStackImmediate();
            if(!favoritesFragment.isVisible()){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, favoritesFragment);
                ft.commit();
            }
        }else if (id == R.id.nav_none3){
            //START NOTIFICATION SERVICE
            service = new ServiceRunner(this);
            service.execute();
        }else if (id == R.id.nav_none4) {
            //STOP NOTIFICATION SERVICE
            if(service==null)
                service = new ServiceRunner(this);
            service.cancel();
        }else if (id == R.id.nav_settings){
            //SEARCH FORM
            while(getSupportFragmentManager().getBackStackEntryCount()>0)
                getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, settingsFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onCharacterAdd(Character character) {
        ((FavoritesFragment) favoritesFragment).addCharacter2Favorites(character);
        Toast.makeText(this,"Character '"+character.getName()+"' saved as Favorite!",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void openCharInfo(Character character) {
        //set fragment on
        ((CharInfoFragment)charInfoFragment).setCharacter(character);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, charInfoFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
