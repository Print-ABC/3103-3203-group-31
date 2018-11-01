
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
    private SessionHandler session;
    private User user;

    private TextView tvUsername, tvRole, tvCardID, tvMsg;

    private TextView tvLABEL;
    private String sUid, myUid, sCard, myCard;
    private int sRole, myRole;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nfc);

        // Check if user is logged in
        session = new SessionHandler(this);
        Utils.redirectToLogin(this.getApplicationContext());
        user = session.getUserDetails();

        if (user.getCardId() == null || (user.getCardId().equals("none"))) {
            Toast.makeText(getBaseContext(), R.string.error_no_card_detected, Toast.LENGTH_SHORT).show();
            finish();
        }

        tvLABEL =  (TextView) findViewById(R.id.tvLABEL);
        tvUsername =  (TextView) findViewById(R.id.tvUsername1);
        tvRole =  (TextView) findViewById(R.id.tvRole1);
        tvCardID =  (TextView) findViewById(R.id.tvCardID1);
        tvMsg =  (TextView) findViewById(R.id.tvMsg1);

        myUid = user.getUid();
        myRole = user.getRole();
        myCard = user.getCardId();

        messagesToSendArray.add(myUid);
        messagesToSendArray.add(String.valueOf(myRole));
        messagesToSendArray.add(myCard);
/*
        tvUsername.setText(messagesToSendArray.get(0));
        tvRole.setText(messagesToSendArray.get(1));
        tvCardID.setText(messagesToSendArray.get(2));
        tvMsg.setText("Sending");
*/
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
            tvLABEL.setText("NFC not available on this device!");
           // Toast.makeText(this, "NFC not available on this device", Toast.LENGTH_SHORT).show();
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
        //Successfully sent.\
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
/*
                tvUsername.setText(sUid);
                tvRole.setText(sRole+"");
                tvCardID.setText(sCard);
                tvMsg.setText("Received");
*/
                //Check if the other person is opposite role of user
                if (myRole == sRole) {
                    Toast.makeText(this, "Cannot NFC with same role", Toast.LENGTH_SHORT).show();
                    tvMsg.setText("NOT OPPOSITE USER!!");
                }
                else {
                    Log.i("NFC ---------------", "sending now");
                    final User user = session.getUserDetails();
                    //Add the RETROFIT HERE
                    //After Receiving
                    //This is to check if the card exist in user's collection
                    //If not, then straight update to DB.
                    Call<User> call = RetrofitClient
                            .getInstance()
                            .getUserApi()
                            .checkForCard(user.getToken(), myUid, sCard);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            switch (response.code()) {
                                case 200:
                                    if (myCard != session.getUserDetails().getCardId()){
                                        session.addCardToList(myCard);
                                    }
                                    else{
                                        session.addCardToList(sCard);
                                    }
                                    Toast.makeText(NFCActivity.this, "Card Added!", Toast.LENGTH_SHORT).show();
                                    //If nfc only sends from one device then include this whole chunk
                                    Call<User> callA = RetrofitClient
                                            .getInstance()
                                            .getUserApi()
                                            .checkForCard(user.getToken(), sUid, myCard);
                                    callA.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> callA, Response<User> responseA) {
                                            switch (responseA.code()) {
                                                case 200:
                                                    if (myCard != session.getUserDetails().getCardId()){
                                                        session.addCardToList(myCard);
                                                    }
                                                    else{
                                                        session.addCardToList(sCard);
                                                    }
                                                    Toast.makeText(NFCActivity.this, R.string.msg_card_added, Toast.LENGTH_SHORT).show();
                                                    break;
                                                case 406:

                                                    Toast.makeText(NFCActivity.this, R.string.error_nc_exists, Toast.LENGTH_SHORT).show();
                                                    break;
                                                default:
                                                    Toast.makeText(NFCActivity.this, R.string.msg_error, Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<User> callA, Throwable t) {
                                        }
                                    });
                                    break;
                                case 500:
                                    Toast.makeText(NFCActivity.this, "Card NOT Added!", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(NFCActivity.this, "Error transferring card", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                        }
                    });

                    Toast.makeText(this, "Card successfully added!", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(this, "NFC Failed.", Toast.LENGTH_LONG).show();
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
                session.logoutUser(user.getToken(), user.getUid(), this);
                Intent intent = new Intent(NFCActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}