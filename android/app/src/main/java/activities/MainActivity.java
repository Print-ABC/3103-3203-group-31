package activities;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import common.SessionHandler;
import common.Utils;
import fragments.CreateNCFragment;
import fragments.FriendsFragment;
import fragments.HomeFragment;
import fragments.NFCFragment;
import fragments.NameCardListFragment;
import fragments.ProfileFragment;
import models.Session;

import com.ncshare.ncshare.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Session session;
    private BottomNavigationItemView friendsMenu;

    /* Reference for bottom Navigation
    https://android.jlelse.eu/ultimate-guide-to-bottom-navigation-on-android-75e4efb8105f
    */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    getSupportActionBar().setTitle("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
                case R.id.nav_create_nc:
                    selectedFragment = new CreateNCFragment();
                    getSupportActionBar().setTitle("Create Name Card");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
                case R.id.nav_cards:
                    selectedFragment = new NameCardListFragment();
                    getSupportActionBar().setTitle("Name Cards");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
                case R.id.nav_nfc:
                    Intent intent = new Intent(MainActivity.this, NFCActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_friends:
                    selectedFragment = new FriendsFragment();
                    getSupportActionBar().setTitle("Friends");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = SessionHandler.getSession();

        Utils.redirectToLogin(this);
        setContentView(R.layout.activity_main);

        friendsMenu = (BottomNavigationItemView)findViewById(R.id.nav_friends);

        if (Utils.isOrganization(session)){
            friendsMenu.setVisibility(View.GONE);
        } else {
            friendsMenu.setVisibility(View.VISIBLE);
        }

        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                selectedFragment = new HomeFragment();
                getSupportActionBar().setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;

            case R.id.nav_profile:
                selectedFragment = new ProfileFragment();
                getSupportActionBar().setTitle("My Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.nav_logout:
                SessionHandler.logoutUser(this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
