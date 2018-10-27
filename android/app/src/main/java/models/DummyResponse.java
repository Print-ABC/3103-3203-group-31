
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DummyResponse {

    @SerializedName("team")
    @Expose
    private String token;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
