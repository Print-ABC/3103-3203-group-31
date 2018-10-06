package com.ncshare.ncshare;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    Context context;
    final String REG_URL ="http://10.0.2.2/ncshare/register.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    BackgroundTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        // Store the method in the variable
        String method = params[0];

        // Account Registration
        if(method.equals("register")){
            String username = params[1];
            String email = params[2];
            String password = params[3];
            String name = params[4];
            String contact = params[5];
            String role = params[6];

            List<NameValuePair> params1 = new ArrayList<>();
            params1.add(new BasicNameValuePair("username", username));
            params1.add(new BasicNameValuePair("email", email));
            params1.add(new BasicNameValuePair("password", password));
            params1.add(new BasicNameValuePair("name", name));
            params1.add(new BasicNameValuePair("contact", contact));
            params1.add(new BasicNameValuePair("role", role));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(REG_URL, "POST", params1);

            Log.d("Create Response", json.toString());

            try{
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);

                return message;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
