package org.hssus.khel.hsskhel.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prasadkhandat on 2/14/18.
 */

public class ErrorMessage {

    @SerializedName("Message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrorMessage fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, ErrorMessage.class);
    }

    public JSONObject toJson() {
        String jsonRepresentation = new Gson().toJson(this, ErrorMessage.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonRepresentation);
        } catch (JSONException e) {
            Log.e(ErrorMessage.class.getName(), "Error converting to JSON: " + e.getMessage());
        }
        return jsonObject;
    }
}
