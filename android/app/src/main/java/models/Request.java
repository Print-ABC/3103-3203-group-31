package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Request {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("cards")
    @Expose
    private List<String> cards = null;
    @SerializedName("requester")
    @Expose
    private String requester;

    public List<String> getCards() { return cards; }

    public void setCards(List<String> cards) { this.cards = cards; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
