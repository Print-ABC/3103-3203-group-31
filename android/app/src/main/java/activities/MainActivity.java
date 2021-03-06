package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ncshare.ncshare.R;

import common.SessionHandler;
import common.Utils;
import fragments.CreateNCFragment;
import fragments.FriendsFragment;
import fragments.HomeFragment;
import fragments.NameCardListFragment;
import models.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SessionHandler session;
    private BottomNavigationItemView friendsMenu;
    private String cardID;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(this);
        user = session.getUserDetails();

        Log.i("NameCard -----------", user.getCardId());
        Log.i("Cards -----------", String.valueOf(user.getCards().size()));
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
                    getSupportActionBar().setTitle(R.string.title_home);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
                case R.id.nav_create_nc:
                    selectedFragment = new CreateNCFragment();
                    getSupportActionBar().setTitle(R.string.title_create_nc);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
                case R.id.nav_cards:
                    selectedFragment = new NameCardListFragment();
                    getSupportActionBar().setTitle(R.string.title_view_nc);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
                case R.id.nav_nfc:
                    /*SessionHandler session = new SessionHandler();
                    User user = session.getUserDetails();
                    if (user.getCardId() == null || (user.getCardId().equals("none"))) {
                        Toast.makeText(getBaseContext(), R.string.error_no_card_detected, Toast.LENGTH_SHORT).show();
                    }
                    else{*/
                    Intent intent = new Intent(MainActivity.this, NFCActivity.class);
                    startActivity(intent);
                    //}
                    break;
                case R.id.nav_friends:
                    selectedFragment = new FriendsFragment();
                    getSupportActionBar().setTitle(R.string.title_friends);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;
            }

            return true;
        }
    };

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
                getSupportActionBar().setTitle(R.string.title_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.nav_logout:
                session.logoutUser(user.getToken(), user.getUid(), this);
                break;
        }
        return true;
    }
}
