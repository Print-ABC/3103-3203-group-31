package common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import activities.LoginActivity;
import models.Result;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.Decryptor;
import security.Encryptor;
import security.SecurityUtils;
import services.RetrofitClient;


public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EMPTY = "";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERNAME_IV = "usernameIV";
    private static final String KEY_UID = "uid";
    private static final String KEY_UID_IV = "uidIV";
    private static final String KEY_CARDID = "card_ID";
    private static final String KEY_CARDID_IV = "card_IDIV";
    private static final String KEY_NAME = "full_name";
    private static final String KEY_NAME_IV = "full_nameIV";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TOKEN_IV = "tokenIV";
    private static final String KEY_ROLE = "role";
    private static final String KEY_ROLE_IV = "roleIV";
    private static final String KEY_FRIENDLIST = "friends";
    private static final String KEY_FRIENDLIST_IV = "friendsIV";
    private static final String KEY_CARDLIST = "card_list";
    private static final String KEY_CARDLIST_IV = "card_listIV";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private User user;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }


    /**
     * Logs in the user by saving user details and setting session
     *
     * @param role
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loginUser(String uid, String name, String username, String token, Integer role, String cardId, List<String> friends, List<String> cards) {

        // Covert list to string + delimiter "-"
        String cardList = Utils.listToString(cards);
        String friendList = Utils.listToString(friends);

        // Encrypt each string with their IV and store into sharedpreference
        storeIntoSP(SecurityUtils.UID_ALIAS,uid, KEY_UID, KEY_UID_IV);
        storeIntoSP(SecurityUtils.NAME_ALIAS, name, KEY_NAME, KEY_NAME_IV);
        storeIntoSP(SecurityUtils.USERNAME_ALIAS, username, KEY_USERNAME, KEY_USERNAME_IV);
        storeIntoSP(SecurityUtils.TOKEN_ALIAS, token, KEY_TOKEN, KEY_TOKEN_IV);
        storeIntoSP(SecurityUtils.ROLE_ALIAS, role.toString(), KEY_ROLE, KEY_ROLE_IV);
        storeIntoSP(SecurityUtils.CARDID_ALIAS, cardId, KEY_CARDID, KEY_CARDID_IV);
        storeIntoSP(SecurityUtils.FRIENDLIST_ALIAS, friendList, KEY_FRIENDLIST, KEY_FRIENDLIST_IV);
        storeIntoSP(SecurityUtils.CARDLIST_ALIAS, cardList, KEY_CARDLIST, KEY_CARDLIST_IV);

        Date date = new Date();

        //Set user session for next 20 minutes
        long millis = date.getTime() + (1200000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void storeIntoSP(String alias, String input, String spKey, String spIvKey) {
        try {
            Encryptor encryptor = new Encryptor();
            encryptor.encryptText(alias, input);
            String encryptedIv = encryptor.getIv();
            String encryptedString = encryptor.getEncryption();

            mEditor.putString(spKey, encryptedString);
            mEditor.putString(spIvKey, encryptedIv);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String retrieveFromSP(String alias, String spKey, String spIvKey) {
        String decrypted = null;
        try {
            String encryptedString = mPreferences.getString(spKey, KEY_EMPTY);
            String encryptedIv = mPreferences.getString(spIvKey, KEY_EMPTY);
            Decryptor decryptor = new Decryptor();
            decrypted = decryptor.decryptData(alias, encryptedString, encryptedIv);
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If millis does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }

        Date expiryDate = new Date(millis);

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
        String cardList = retrieveFromSP(SecurityUtils.CARDLIST_ALIAS, KEY_CARDLIST, KEY_CARDLIST_IV);
        String friendList = retrieveFromSP(SecurityUtils.FRIENDLIST_ALIAS, KEY_FRIENDLIST, KEY_FRIENDLIST_IV);
        cardList = Utils.removeDelimiter(cardList);
        friendList = Utils.removeDelimiter(friendList);
        List<String> cards = Arrays.asList(cardList.split("-"));
        List<String> friends = Arrays.asList(friendList.split("-"));

        user.setUid(retrieveFromSP(SecurityUtils.UID_ALIAS, KEY_UID, KEY_UID_IV));
        user.setToken(retrieveFromSP(SecurityUtils.TOKEN_ALIAS, KEY_TOKEN, KEY_TOKEN_IV));
        user.setCardId(retrieveFromSP(SecurityUtils.CARDID_ALIAS, KEY_CARDID, KEY_CARDID_IV));
        user.setRole(Integer.parseInt(retrieveFromSP(SecurityUtils.ROLE_ALIAS, KEY_ROLE, KEY_ROLE_IV)));
        user.setName(retrieveFromSP(SecurityUtils.NAME_ALIAS, KEY_NAME, KEY_NAME_IV));
        user.setUsername(retrieveFromSP(SecurityUtils.USERNAME_ALIAS, KEY_USERNAME, KEY_USERNAME_IV));
        user.setFriendship(friends);
        user.setCards(cards);

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(String token, String uid, final Context context) {
        Call<Result> call = RetrofitClient.getInstance().getActiveUsersApi().logout(token, uid);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                mEditor.clear();
                mEditor.commit();
                // redirect user to Login activity
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addCardToList(String cardId){
        String cardList = retrieveFromSP(SecurityUtils.CARDLIST_ALIAS, KEY_CARDLIST, KEY_CARDLIST_IV);
        cardList = Utils.removeDelimiter(cardList);
        ArrayList<String> cards = new ArrayList<>(Arrays.asList(cardList.split("-")));
        cards.add(cardId);
        String cardsString = Utils.listToString(cards);
        storeIntoSP(SecurityUtils.CARDLIST_ALIAS, cardsString, KEY_CARDLIST, KEY_CARDLIST_IV);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void removeFriendFromList(String uid){
        String friendList = retrieveFromSP(SecurityUtils.FRIENDLIST_ALIAS, KEY_FRIENDLIST, KEY_FRIENDLIST_IV);
        friendList = Utils.removeDelimiter(friendList);
        ArrayList<String> friends = new ArrayList<>(Arrays.asList(friendList.split("-")));
        friends.remove(uid);
        String cardsString = Utils.listToString(friends);
        storeIntoSP(SecurityUtils.FRIENDLIST_ALIAS, cardsString, KEY_FRIENDLIST, KEY_FRIENDLIST_IV);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addFriendToList(String uid){
        String friendList = retrieveFromSP(SecurityUtils.FRIENDLIST_ALIAS, KEY_FRIENDLIST, KEY_FRIENDLIST_IV);
        friendList = Utils.removeDelimiter(friendList);
        ArrayList<String> friends = new ArrayList<>(Arrays.asList(friendList.split("-")));
        friends.add(uid);
        String cardsString = Utils.listToString(friends);
        storeIntoSP(SecurityUtils.FRIENDLIST_ALIAS, cardsString, KEY_FRIENDLIST, KEY_FRIENDLIST_IV);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCardId(String cardId){
        storeIntoSP(SecurityUtils.CARDID_ALIAS, cardId, KEY_CARDID, KEY_CARDID_IV);
    }
}
