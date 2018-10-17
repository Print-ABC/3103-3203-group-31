package activities;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import common.SessionHandler;
import common.Utils;
import fragments.FriendsFragment;
import fragments.HomeFragment;
import fragments.NFCFragment;
import fragments.NamecardsFragment;
import fragments.ProfileFragment;
import com.ncshare.ncshare.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SessionHandler session;
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
                    break;
                case R.id.nav_cards:
                    selectedFragment = new NamecardsFragment();
                    getSupportActionBar().setTitle("Name Cards");
                    break;
                case R.id.nav_nfc:
                    selectedFragment = new NFCFragment();
                    getSupportActionBar().setTitle("NFC");
                    break;
                case R.id.nav_friends:
                    selectedFragment = new FriendsFragment();
                    getSupportActionBar().setTitle("Friends");
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        Utils.redirectToLogin(session, this);
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
            case R.id.nav_profile:
                selectedFragment = new ProfileFragment();
                getSupportActionBar().setTitle("My Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.nav_logout:
                session.logoutUser();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
