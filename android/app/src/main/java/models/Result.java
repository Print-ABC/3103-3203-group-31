package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("cardId")
    @Expose
    private String cardId;

    public Result() {
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardId() { return cardId; }

    public void setCardId(String cardId) { this.cardId = cardId; }
}
