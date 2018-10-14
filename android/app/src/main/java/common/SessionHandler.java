package common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import models.User;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_ROLE = "role";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_UID = "userID";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }


    /**
     * Logs in the user by saving user details and setting session
     * @param role
     */
    public void loginUser(String uid, String role) {
        mEditor.putString(KEY_UID, uid);
        mEditor.putString(KEY_ROLE, role);
        Date date = new Date();

        //Set user session for next 24 hours
        long millis = date.getTime() + (1 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUid(mPreferences.getString(KEY_UID, KEY_EMPTY));
        user.setUserRole(mPreferences.getString(KEY_ROLE, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }
}
