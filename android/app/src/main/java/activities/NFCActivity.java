package activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.R;

import java.nio.charset.Charset;
import java.util.ArrayList;

import common.SessionHandler;
import common.Utils;
import fragments.CreateNCFragment;
import models.Session;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class NFCActivity extends AppCompatActivity
        implements NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.CreateNdefMessageCallback {
    //The array lists to hold our messages
    private ArrayList<String> messagesToSendArray = new ArrayList<>();
    private ArrayList<String> messagesReceivedArray = new ArrayList<>();
    private Session session;

    private TextView tvLABEL;
    private String sUid, myUid, sCard, myCard;
    private int sRole, myRole;
    private NfcAdapter mNfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Check if user is logged in
        session = SessionHandler.getSession();
        Utils.redirectToLogin(this.getApplicationContext());

        tvLABEL =  (TextView) findViewById(R.id.tvLABEL);

        //Change this for the connected phone
        myUid = session.getUser().getUid();
        myRole = session.getUser().getRole();
        myCard = session.getCardId();

        if (myCard == null){
            myCard = "";
        }

        Log.i("myUid", myUid);
        Log.i("myRole", String.valueOf(myRole));
        Log.i("myCard", myCard);

        messagesToSendArray.add(myUid);
        messagesToSendArray.add(String.valueOf(myRole));
        messagesToSendArray.add(myCard);

        //Check if the user has a Card ID
        //If don't have, bring back to home fragment(main activity)
        if(messagesToSendArray.get(2)==""){
            Toast.makeText(this, "No card id detected!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NFCActivity.this, MainActivity.class);
            startActivity(intent);

        }

        int listSize = messagesToSendArray.size();
        for (int i = 0; i < listSize; i++) {
            Log.i("messagesToSendArray -> ", messagesToSendArray.get(i));
        }

        //Check if NFC is available on device
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            tvLABEL.setText("Place phones back to back to start!");
            // Handle some NFC initialization here
            //This will refer back to createNdefMessage for what it will send
            mNfcAdapter.setNdefPushMessageCallback(this, this);

            //This will be called if the message is sent successfully
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

        } else {
            //Toast.makeText(this, "NFC not available on this device", Toast.LENGTH_SHORT).show();
            tvLABEL.setText("NFC not available on this device!");
        }
    }

    //Save our Array Lists of Messages for if the user navigates away
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("messagesToSend", messagesToSendArray);
        savedInstanceState.putStringArrayList("lastMessagesReceived",messagesReceivedArray);
    }

    //Load our Array Lists of Messages for when the user navigates back
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        messagesToSendArray = savedInstanceState.getStringArrayList("messagesToSend");
        messagesReceivedArray = savedInstanceState.getStringArrayList("lastMessagesReceived");
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        //This is called when the system detects that our NdefMessage was
        //Successfully sent.
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        //This will be called when another NFC capable device is detected.
        if (messagesToSendArray.size() == 0) {
            return null;
        }
        //We'll write the createRecords() method in just a moment
        NdefRecord[] recordsToAttach = createRecords();
        //When creating an NdefMessage we need to provide an NdefRecord[]
        return new NdefMessage(recordsToAttach);
    }

    public NdefRecord[] createRecords() {
        NdefRecord[] records = new NdefRecord[messagesToSendArray.size() + 1];
        //To Create Messages Manually if API is less than
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            for (int i = 0; i < messagesToSendArray.size(); i++){
                byte[] payload = messagesToSendArray.get(i).
                        getBytes(Charset.forName("UTF-8"));
                NdefRecord record = new NdefRecord(
                        NdefRecord.TNF_WELL_KNOWN,      //Our 3-bit Type name format
                        NdefRecord.RTD_TEXT,            //Description of our payload
                        new byte[0],                    //The optional id for our Record
                        payload);                       //Our payload for the Record

                records[i] = record;
            }
        }
        //Api is high enough that we can use createMime, which is preferred.
        else {
            for (int i = 0; i < messagesToSendArray.size(); i++){
                byte[] payload = messagesToSendArray.get(i).
                        getBytes(Charset.forName("UTF-8"));

                NdefRecord record = NdefRecord.createMime("text/plain",payload);
                records[i] = record;
            }
        }
        records[messagesToSendArray.size()] =
                NdefRecord.createApplicationRecord(getPackageName());
        return records;
    }

    private void handleNfcIntent(Intent NfcIntent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(receivedArray != null) {
                messagesReceivedArray.clear();
                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record:attachedRecords) {
                    String string = new String(record.getPayload());
                    //Make sure we don't pass along our AAR (Android Application Record)
                    if (string.equals(getPackageName())) { continue; }
                    messagesReceivedArray.add(string);
                }

                sUid = messagesReceivedArray.get(0);
                sRole = Integer.parseInt(messagesReceivedArray.get(1));
                sCard = messagesReceivedArray.get(2);

                Toast.makeText(this, "Received " + messagesReceivedArray.size() +
                        " Messages", Toast.LENGTH_LONG).show();


                //Check if the other person is opposite role of user
                if (myRole == sRole) {
                    Toast.makeText(this, "Cannot NFC with same role", Toast.LENGTH_SHORT).show();
                    Log.i("Received - Role:", "Cannot NFC with same role");
                }
                else {
                    //Add the RETROFIT HERE
                    //After Receiving
                    //This is to check if the card exist in user's collection
                    //If not, then straight update to DB.
                    Call<User> call = RetrofitClient
                            .getInstance()
                            .getUserApi()
                            .checkForCard(myUid, sCard);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Toast.makeText(NFCActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if(response.body().getSuccess()){
                                Log.i("onResponseeeee","Card Added!");

                                //If nfc only sends from one device then include this whole chunk
                                Call<User> callA = RetrofitClient
                                        .getInstance()
                                        .getUserApi()
                                        .checkForCard(sUid, myCard);
                                callA.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> callA, Response<User> responseA) {
                                        Toast.makeText(NFCActivity.this, responseA.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        if(responseA.body().getSuccess()){
                                            Log.i("onResponseeeee -1","Card Exist!");
                                        } else {
                                            Log.i("onResponseeeee -1","Card NOT Exist!");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> callA, Throwable t) {
                                        Log.i("onFailure","ERROR!");
                                    }
                                });
                            } else {
                                Log.i("onResponseeeee","Card NOT Added!");
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.i("onFailure","ERROR!");
                        }
                    });


                    Toast.makeText(this, "Card successfully added!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intentHome = new Intent(NFCActivity.this, MainActivity.class);
                startActivity(intentHome);
                break;

            case R.id.nav_logout:
                SessionHandler.logoutUser(this);
                Intent intent = new Intent(NFCActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
