
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {

    @SerializedName("user_name")
    @Expose
    private String name;
    @SerializedName("user_username")
    @Expose
    private String username;
    @SerializedName("user_password")
    @Expose
    private String password;
    @SerializedName("user_contact")
    @Expose
    private String contact;
    @SerializedName("user_role")
    @Expose
    private String userRole;
    @SerializedName("user_friend_id")
    @Expose
    private String uidFriend;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_id")
    @Expose
    private String uid;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private Date sessionExpiryDate;

    public Date getSessionExpiryDate() { return sessionExpiryDate; }

    public void setSessionExpiryDate(Date sessionExpiryDate) { this.sessionExpiryDate = sessionExpiryDate; }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUidFriend() {
        return uidFriend;
    }

    public void setUidFriend(String uidFriend) {
        this.uidFriend = uidFriend;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserEmail() {return userEmail;}

    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}

}
