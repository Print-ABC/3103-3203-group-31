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
    @SerializedName("checkingId")
    @Expose
    private String checkingId;
    @SerializedName("cards")
    @Expose
    private List<String> cards = null;
    @SerializedName("role")
    @Expose
    private Integer role;
    @SerializedName("cardId")
    @Expose
    private String cardId;
    @SerializedName("friendsOp")
    @Expose
    private Boolean friendsOp;

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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getCheckingId() { return checkingId; }

    public void setCheckingId(String checkingId) { this.checkingId = checkingId; }

    public String getCardId() { return cardId; }

    public void setCardId(String cardId) { this.cardId = cardId; }

    public Boolean getFriendsOp() { return friendsOp; }

    public void setFriendsOp(Boolean friendsOp) { this.friendsOp = friendsOp; }
}
