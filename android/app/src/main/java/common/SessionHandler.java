package common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.List;

import activities.LoginActivity;
import models.Session;
import models.User;


public class SessionHandler {
    private static Session session;

    public static Session getSession() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public static void setSession(Session session) {
        SessionHandler.session = session;
    }

    public static void setSessionUserObj(User user){
        SessionHandler.session.setUser(user);
    }

    public static User getSessionUserObj(){
        return SessionHandler.session.getUser();
    }

    /**
     * Add card ID to session
     * @param cardId
     */
    public void addCardToSession(String cardId) {
        SessionHandler.session.setCardId(cardId);
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
        return SessionHandler.session.getCardId();
    }


    /**
     * Logs in the user by saving user details and setting session
     * @param role
     */
    public static void loginUser(String uid, String name, String username, String token, Integer role, String cardId, List<String> friends, List<String> cards) {
        User user = new User();
        user.setUid(uid);
        user.setName(name);
        user.setUsername(username);
        user.setToken(token);
        user.setRole(role);
        user.setCardId(cardId);
        user.setFriendship(friends);
        user.setCards(cards);
        setSessionUserObj(user);
        Date date = new Date();
        //Set user session for next 20 minutes
        SessionHandler.session.setMillis(date.getTime() + (1200000));
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public static boolean isLoggedIn() {
        Date currentDate = new Date();

        /* If millis does not have a value
         then user is not logged in
         */
        if (SessionHandler.session.getMillis() == 0) {
            return false;
        }

        Date expiryTime = new Date(SessionHandler.session.getMillis());

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
        return getSessionUserObj();
    }

    /**
     * Logs out user by clearing the session
     */
    public static void logoutUser(Context context){
        session = null;
        SessionHandler.session.deleteSession();
        // redirect user to Login activity
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
