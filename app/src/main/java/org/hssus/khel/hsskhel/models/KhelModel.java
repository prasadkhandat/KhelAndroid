package org.hssus.khel.hsskhel.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasadkhandat on 1/2/18.
 */

public class KhelModel {

    public final String TAG = KhelModel.class.getName();


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("formation")
    @Expose
    private String formation;
    @SerializedName("intensity")
    @Expose
    private String intensity;
    @SerializedName("audiance")
    @Expose
    private String audiance;
    @SerializedName("min_participants")
    @Expose
    private String minParticipants;
    @SerializedName("max_participants")
    @Expose
    private String maxParticipants;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("submited_date")
    @Expose
    private String submitedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getAudiance() {
        return audiance;
    }

    public void setAudiance(String audiance) {
        this.audiance = audiance;
    }

    public String getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(String minParticipants) {
        this.minParticipants = minParticipants;
    }

    public String getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(String maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getSubmitedDate() {
        return submitedDate;
    }

    public void setSubmitedDate(String submitedDate) {
        this.submitedDate = submitedDate;
    }

    public static KhelModel fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, KhelModel.class);
    }

    public JSONObject toJson() {
        String jsonRepresentation = new Gson().toJson(this, KhelModel.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonRepresentation);
        } catch (JSONException e) {
            Log.e(TAG, "Error converting to JSON: " + e.getMessage());
        }
        return jsonObject;
    }
}
