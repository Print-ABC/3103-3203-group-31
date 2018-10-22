package com.ncshare.ncshare;

public class FriendsModel {
    //TODO: remove these variables
    private String mName, mSchool;

    private String requester_id, requester, recipient_id, recipient;

    //TODO: remove these methods
    public String getName() {
        return mName;
    }
    public void setName(String mName) {
        this.mName = mName;
    }
    public String getSchool() {
        return mSchool;
    }
    public void setSchool(String mSchool) {
        this.mSchool = mSchool;
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
}
