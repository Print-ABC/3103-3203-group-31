package common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import activities.LoginActivity;
import activities.MainActivity;

public class Utils {
    public static final String STUDENT = "Student";
    public static final String ORGANIZATION = "Organization";
    public static final String STUDENT_ROLE = "0";
    public static final String ORGANIZATION_ROLE = "1";

    /**
     * Check if user is logged in, redirect to LoginActivity if not logged in
     * @param session
     * @param context
     */
    public static void redirectToLogin(SessionHandler session, Context context){
        if (!session.isLoggedIn()){
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }
}
