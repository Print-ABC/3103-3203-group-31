package common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;
import java.util.List;

import activities.LoginActivity;
import models.Result;
import models.User;
import retrofit2.Call;
import services.RetrofitClient;

public class Session {
    private User user;
    private String cardId;
    private static Session session;
    private long millis = 0;

    private Session(){
        user = new User();
    }

    public static Session getSession() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    /**
     * Add card ID to session
     * @param cardId
     */
    public void addCardToSession(String cardId){
        this.cardId = cardId;
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
        return user.getCardId();
    }


    /**
     * Logs in the user by saving user details and setting session
     * @param role
     */
    public void loginUser(String uid, String token, Integer role, String cardId, List<String>friends, List<String> cards) {
        user.setUid(uid);
        user.setToken(token);
        user.setRole(role);
        user.setCardId(cardId);
        Date date = new Date();

        //Set user session for next 30 minutes
        millis = date.getTime() + (1 * 1 * 30 * 60 * 1000);
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        /* If millis does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryTime = new Date(millis);

        /* Check if millis is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryTime);
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
        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(Context context){
        session = null;
        // redirect user to Login activity
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
