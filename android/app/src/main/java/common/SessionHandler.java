package common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

import models.Login;
import models.Result;
import models.User;
import retrofit2.Call;
import services.RetrofitClient;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_ROLE = "role";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_CARD_ID = "cardId";
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
     * Add card ID to session
     * @param cardId
     */
    public void addCardToSession(String cardId){
        mEditor.putString(KEY_CARD_ID, cardId);
        mEditor.commit();
    }

    /**
     * Retrieve card ID from session
     * Output may be null if user is not logged in
     * Output is "none" if user has not created a card
     */
    public String getCardFromSession(){
        if (!isLoggedIn()) {
            return null;
        }
        return mPreferences.getString(KEY_UID, KEY_EMPTY);
    }


    /**
     * Logs in the user by saving user details and setting session
     * @param role
     */
    public void loginUser(String uid, String token, Integer role, String cardId) {
        mEditor.putString(KEY_UID, uid);
        mEditor.putInt(KEY_ROLE, role);
        mEditor.putString(KEY_TOKEN, token);
        mEditor.putString(KEY_CARD_ID, cardId);
        Date date = new Date();

        //Set user session for next 1 hour
        long millis = date.getTime() + (1 * 1 * 60 * 60 * 1000);
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
    public Login getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        Login user = new Login();
        user.setUid(mPreferences.getString(KEY_UID, KEY_EMPTY));
        user.setRole(mPreferences.getInt(KEY_ROLE, 0));
        user.setToken(mPreferences.getString(KEY_TOKEN, KEY_EMPTY));
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
