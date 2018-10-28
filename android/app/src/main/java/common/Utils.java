package common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.ncshare.ncshare.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import activities.LoginActivity;

public class Utils {
    public static final String STUDENT = "Student";
    public static final String ORGANIZATION = "Organization";
    public static final int STUDENT_ROLE = 0;
    public static final int ORGANIZATION_ROLE = 1;
    public static final String NO_CARD = "none";
    public static final String CONTACT = "contact";
    public static final String COURSE = "course";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String JOB_TITLE = "job title";
    public static final String USER_ROLE = "user role";
    public static final String ORGANIZATION_NAME = "organization name";


    /**
     * Check if user is logged in, redirect to LoginActivity if not logged in
     * @param context
     */
    public static void redirectToLogin(Context context){
        SessionHandler session = new SessionHandler(context);
        if (!session.isLoggedIn()){
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

    /**
     * Check if user is from an organization or a student
     * @param session
     * @return
     */
    public static boolean isOrganization(SessionHandler session){
        if (session.getUserDetails().getRole() == ORGANIZATION_ROLE){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks for a valid name input
     * @param name
     * @param etName
     * @param context
     * @return
     */
    public static boolean validateName(String name, EditText etName, Context context) {
        String regx = "^[\\p{L} .'-]{1,30}+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        if (name.isEmpty()) {
            etName.setError(context.getString(R.string.empty_name_error));
            return false;
        } else if (!matcher.find()) {
            etName.setError(context.getString(R.string.invalid_name_error));
            return false;
        } else {
            etName.setError(null);
            return true;
        }
    }

    /**
     * Checks for a valid email input
     * @param email
     * @param etEmail
     * @param context
     * @return
     */
    public static boolean validateEmail(String email, EditText etEmail, Context context) {
        String regx = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (email.isEmpty()) {
            etEmail.setError(context.getString(R.string.empty_email_error));
            return false;
        } else if (!matcher.find()) {
            etEmail.setError(context.getString(R.string.invalid_email_error));
            return false;
        } else {
            etEmail.setError(null);
            return true;
        }

    }

    /**
     * Checks for a valid contact number input
     * @param contact
     * @param etContact
     * @param context
     * @return
     */
    public static boolean validateContact(String contact, EditText etContact, Context context) {
        String regx = "(6|8|9)[0-9]{7}";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(contact);

        if (contact.isEmpty()) {
            etContact.setError(context.getString(R.string.empty_contact_error));
            return false;
        }
        if (!matcher.find()) {
            etContact.setError(context.getString(R.string.invalid_contact_error));
            return false;
        } else {
            etContact.setError(null);
            return true;
        }
    }

    public static String listToString(List<String> list){
        StringBuilder sb = new StringBuilder();
        for (String s : list)
        {
            sb.append(s);
            sb.append("-");
        }
        return sb.toString();
    }

    public static String removeDelimiter(String text){
        if (text.length() != 0){
            text = text.substring(1);
            text = text.substring(0, text.length() - 1);
        }
        return text.toString();
    }
}
