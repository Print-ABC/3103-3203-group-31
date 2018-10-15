
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Organization {

    @SerializedName("org_user_id")
    @Expose
    private String orgUserId;
    @SerializedName("org_card_name")
    @Expose
    private String orgCardName;
    @SerializedName("org_card_organization")
    @Expose
    private String orgCardOrganization;
    @SerializedName("org_card_contact")
    @Expose
    private String orgCardContact;
    @SerializedName("org_card_job_title")
    @Expose
    private String orgCardJobTitle;
    @SerializedName("org_card_email")
    @Expose
    private String orgCardEmail;

    public String getOrgUserId() {
        return orgUserId;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }

    public String getOrgCardName() {
        return orgCardName;
    }

    public void setOrgCardName(String orgCardName) {
        this.orgCardName = orgCardName;
    }

    public String getOrgCardOrganization() {
        return orgCardOrganization;
    }

    public void setOrgCardOrganization(String orgCardOrganization) {
        this.orgCardOrganization = orgCardOrganization;
    }

    public String getOrgCardContact() {
        return orgCardContact;
    }

    public void setOrgCardContact(String orgCardContact) {
        this.orgCardContact = orgCardContact;
    }

    public String getOrgCardJobTitle() {
        return orgCardJobTitle;
    }

    public void setOrgCardJobTitle(String orgCardJobTitle) {
        this.orgCardJobTitle = orgCardJobTitle;
    }

    public String getOrgCardEmail() {
        return orgCardEmail;
    }

    public void setOrgCardEmail(String orgCardEmail) {
        this.orgCardEmail = orgCardEmail;
    }

}
