package security;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class SecurityUtils {

    public static final String UID_ALIAS = "uid_alias";
    public static final String USERNAME_ALIAS = "username_alias";
    public static final String ROLE_ALIAS = "role_alias";
    public static final String CARDID_ALIAS = "cardid_alias";
    public static final String CARDLIST_ALIAS = "cardlist_alias";
    public static final String FRIENDLIST_ALIAS = "friendlist_alias";
    public static final String TOKEN_ALIAS = "token_alias";
    public static final String NAME_ALIAS = "name_alias";

    public static String decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.e("JWT_DECODED", "Body: " + getJson(split[1]));
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            //Error
        }
        return "";
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static String manipulateToken(String token) {
        StringBuilder sb = new StringBuilder(token);
        // remove at position 149
        sb = sb.deleteCharAt(149);
        // remove at position 343
        sb = sb.deleteCharAt(343);
        // remove at position 3
        sb = sb.deleteCharAt(3);
        return sb.toString();
    }
}

