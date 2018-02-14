package org.hssus.khel.hsskhel.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasadkhandat on 1/17/18.
 */

public class KhelListModel {

    public final String TAG = KhelListModel.class.getName();

    @SerializedName("data")
    @Expose
    private List<KhelModel> data = new ArrayList<KhelModel>();

    public List<KhelModel> getData() {
        return data;
    }

    public void setData(List<KhelModel> data) {
        this.data = data;
    }


    public static KhelListModel fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, KhelListModel.class);
    }

    public JSONObject toJson() {
        String jsonRepresentation = new Gson().toJson(this, KhelListModel.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonRepresentation);
        } catch (JSONException e) {
            Log.e(TAG, "Error converting to JSON: " + e.getMessage());
        }
        return jsonObject;
    }
}

