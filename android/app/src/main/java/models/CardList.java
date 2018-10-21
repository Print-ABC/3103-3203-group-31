package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardList {
    @SerializedName("cards")
    @Expose
    private List<String> cards = null;
    @SerializedName("orgCards")
    @Expose
    private List<Organization> orgCards = null;
    @SerializedName("stuCards")
    @Expose
    private List<Student> stuCards = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<Organization> getOrgCards() {
        return orgCards;
    }

    public void setOrgCards(List<Organization> orgCards) {
        this.orgCards = orgCards;
    }

    public List<Student> getStuCards() {
        return stuCards;
    }

    public void setStuCards(List<Student> stuCards) {
        this.stuCards = stuCards;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public List<String> getCards() { return cards; }

    public void setCards(List<String> cards) { this.cards = cards; }
}
