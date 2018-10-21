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
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.R;

import java.nio.charset.Charset;
import java.util.ArrayList;

import fragments.CreateNCFragment;

public class NFCActivity extends AppCompatActivity
        implements NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.CreateNdefMessageCallback {
    //The array lists to hold our messages
    //The array lists to hold our messages
    private ArrayList<String> messagesToSendArray = new ArrayList<>();
    private ArrayList<String> messagesReceivedArray = new ArrayList<>();

    public TextView tvUsername, tvRole, tvCardID, tvMsg;
    private String sUsername, myUsername, sRole, myRole, sCard, myCard;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvRole =  (TextView) findViewById(R.id.tvRole);
        tvCardID =  (TextView) findViewById(R.id.tvCardID);
        tvMsg =  (TextView) findViewById(R.id.tvMsg);

        //Change this for the connected phone
        myUsername = "Usernameeeeee";
        myRole = "Org";
        myCard = "";

        messagesToSendArray.add(myUsername);
        messagesToSendArray.add(myRole);
        messagesToSendArray.add(myCard);

        tvUsername.setText(messagesToSendArray.get(0));
        tvRole.setText(messagesToSendArray.get(1));
        tvCardID.setText(messagesToSendArray.get(2));
        tvMsg.setText("Sending");

        //Check if the user has a Card ID
        //If dont have, bring to create name card fragment.
        if(messagesToSendArray.get(2).isEmpty() || messagesToSendArray.get(2)==""){
            Toast.makeText(this, "No card id detected!", Toast.LENGTH_SHORT).show();
            Fragment createNC = new CreateNCFragment();
            getSupportActionBar().setTitle("Create Namemcard");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, createNC).commit();

        }

        int listSize = messagesToSendArray.size();
        for (int i = 0; i < listSize; i++) {
            Log.i("messagesToSendArray -> ", messagesToSendArray.get(i));
        }

        //Check if NFC is available on device
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            // Handle some NFC initialization here
            //This will refer back to createNdefMessage for what it will send
            mNfcAdapter.setNdefPushMessageCallback(this, this);

            //This will be called if the message is sent successfully
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        } else {
            Toast.makeText(this, "NFC not available on this device", Toast.LENGTH_SHORT).show();
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

                sUsername = messagesReceivedArray.get(0);
                sRole = messagesReceivedArray.get(1);
                sCard = messagesReceivedArray.get(2);

                tvUsername.setText(sUsername);
                tvRole.setText(sRole);
                tvCardID.setText(sCard);
                tvMsg.setText("Received");

                Toast.makeText(this, "Received " + messagesReceivedArray.size() +
                        " Messages", Toast.LENGTH_LONG).show();

                //Check if the other person is opposite role of user
                if (myRole.equals(sRole)) {
                    //  Toast.makeText(this, "Cannot NFC with same role", Toast.LENGTH_SHORT).show();
                    Log.i("Received - Role:", "Cannot NFC with same role");
                    tvMsg.setText("NOT OPPOSITE USER!!");
                }
                //Check for duplicates?
                else if (sCard.equals("Card ID")) {
                    //  Toast.makeText(this, "Already has the card!", Toast.LENGTH_SHORT).show();
                    Log.i("Received - CARDID:", "Already has the card!");
                    tvMsg.setText("CARD ID ALR EXISTED!!");
                } else {
                    tvMsg.setText("Card Successfully Added!");
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
}
