
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendRequest {

    @SerializedName("requester_id")
    @Expose
    private String requester_id;
    @SerializedName("requester")
    @Expose
    private String requester;
    @SerializedName("requester_username")
    @Expose
    private String requester_username;
    @SerializedName("recipient_id")
    @Expose
    private String recipient_id;
    @SerializedName("recipient")
    @Expose
    private String recipient;
    @SerializedName("recipient_username")
    @Expose
    private String recipient_username;

    public FriendRequest() {}

    public FriendRequest(String requester_id, String requester, String requester_username, String recipient_id, String recipient, String recipient_username) {
        this.requester_id = requester_id;
        this.requester = requester;
        this.requester_username = requester_username;
        this.recipient_id = recipient_id;
        this.recipient = recipient;
        this.recipient_username = recipient_username;
    }

    public String getRequester_id() {
        return requester_id;
    }

    public void setRequester_id(String requester_id) {
        this.requester_id = requester_id;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public String getRecipient_username() {
        return recipient_username;
    }

    public void setRecipient_username(String recipient_username) {
        this.recipient_username = recipient_username;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRequester_username() {
        return requester_username;
    }

    public void setRequester_username(String requester_username) {
        this.requester_username = requester_username;
    }
}
