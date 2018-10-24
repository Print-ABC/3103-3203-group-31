
package models;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("role")
    @Expose
    private Integer role;
    @SerializedName("cardId")
    @Expose
    private String cardId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("friendship")
    @Expose
    private List<String> friendship = null;
    @SerializedName("cards")
    @Expose
    private List<String> cards = null;
    @SerializedName("iat")
    @Expose
    private String iat;

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    @SerializedName("exp")
    @Expose

    private String exp;

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public Boolean getSuccess() { return success; }

    public void setSuccess(Boolean success) { this.success = success; }

    public Date getSessionExpiryDate() { return sessionExpiryDate; }

    public void setSessionExpiryDate(Date sessionExpiryDate) { this.sessionExpiryDate = sessionExpiryDate; }

    private Date sessionExpiryDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFriendship() {
        return friendship;
    }

    public void setFriendship(List<String> friendship) {
        this.friendship = friendship;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getCardId() { return cardId; }

    public void setCardId(String cardId) { this.cardId = cardId; }
}
