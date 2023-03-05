package edu.ucsd.cse110.cse110_team17_project;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class UserInfo {

    @PrimaryKey
    @SerializedName("privateCode")
    @NonNull
    public String privateCode;

    @SerializedName("label")
    @NonNull
    public String label;

    @SerializedName(value = "latitude")
    @NonNull
    public double latitude;

    @SerializedName(value = "longitude")
    @NonNull
    public double longitude;

    public UserInfo(String privateCode, String label) {
        this.privateCode = privateCode;
        this.label = label;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public UserInfo(String privateCode, String label, Double latitude, Double longitude) {
        this.privateCode = privateCode;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static UserInfo formJSON(String json) {
        return new Gson().fromJson(json, UserInfo.class);
        // May cause a problem, fix later
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

}
