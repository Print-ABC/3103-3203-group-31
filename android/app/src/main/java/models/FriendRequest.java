
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

    @SerializedName("recipient_id")
    @Expose
    private String recipient_id;

    @SerializedName("recipient")
    @Expose
    private String recipient;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public FriendRequest() {}

    public FriendRequest(String requester_id, String requester, String recipient_id, String recipient) {
        this.requester_id = requester_id;
        this.requester = requester;
        this.recipient_id = recipient_id;
        this.recipient = recipient;
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

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String getFriendStatus() {
//        return friendStatus;
//    }
//
//    public void setFriendStatus(String friendStatus) {
//        this.friendStatus = friendStatus;
//    }
//
//    public String getFriendActionUid() {
//        return friendActionUid;
//    }
//
//    public void setFriendActionUid(String friendActionUid) {
//        this.friendActionUid = friendActionUid;
//    }

}
