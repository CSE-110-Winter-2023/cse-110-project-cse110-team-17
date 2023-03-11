package edu.ucsd.cse110.cse110_team17_project.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class UserInfo {

    @PrimaryKey
    @SerializedName("private_code")
    @NonNull
    public String private_code;

    @SerializedName("public_code")
    @NonNull
    public String public_code;

    @SerializedName("label")
    @NonNull
    public String label;

    @SerializedName(value = "latitude")
    @NonNull
    public double latitude;

    @SerializedName(value = "longitude")
    @NonNull
    public double longitude;

    public UserInfo(String private_code, String label, String public_code) {
        this.private_code = private_code;
        this.public_code = public_code;
        this.label = label;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    @Ignore
    public UserInfo(String private_code, String public_code,String label, Double latitude, Double longitude) {
        this.private_code = private_code;
        this.public_code = public_code;
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
