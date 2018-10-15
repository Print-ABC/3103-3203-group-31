
package models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    @Expose
    private String name;
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
    private String role;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("friendship")
    @Expose
    private List<String> friendship = null;
    @SerializedName("cards")
    @Expose
    private List<String> cards = null;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

}
