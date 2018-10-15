
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relationship {

    @SerializedName("friend_one_id")
    @Expose
    private String friendOneId;
    @SerializedName("friend_two_id")
    @Expose
    private String friendTwoId;
    @SerializedName("friend_status")
    @Expose
    private String friendStatus;
    @SerializedName("friend_action_uid")
    @Expose
    private String friendActionUid;

    public String getFriendOneId() {
        return friendOneId;
    }

    public void setFriendOneId(String friendOneId) {
        this.friendOneId = friendOneId;
    }

    public String getFriendTwoId() {
        return friendTwoId;
    }

    public void setFriendTwoId(String friendTwoId) {
        this.friendTwoId = friendTwoId;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String getFriendActionUid() {
        return friendActionUid;
    }

    public void setFriendActionUid(String friendActionUid) {
        this.friendActionUid = friendActionUid;
    }

}
