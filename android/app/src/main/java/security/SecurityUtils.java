package security;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.security.auth.x500.X500Principal;

public class SecurityUtils {

    public static final String SESSION_ALIAS = "UserSession";
    public static final String UID_ALIAS = "uid_alias";
    public static final String USERNAME_ALIAS = "username_alias";
    public static final String ROLE_ALIAS = "role_alias";
    public static final String CARDID_ALIAS = "cardid_alias";
    public static final String CARDLIST_ALIAS = "cardlist_alias";
    public static final String FRIENDLIST_ALIAS = "friendlist_alias";
    public static final String TOKEN_ALIAS = "token_alias";
    public static final String NAME_ALIAS = "name_alias";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final String TAG = "SecurityUtils";

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

