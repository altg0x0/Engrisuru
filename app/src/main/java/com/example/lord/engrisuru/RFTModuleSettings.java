package com.example.lord.engrisuru;

import org.json.JSONException;
import org.json.JSONObject;

class RFTModuleSettings extends ModuleSettings {
    boolean reversed = false;

    RFTModuleSettings(JSONObject json)
    {
        try {
            reversed = json.getJSONObject("settings").getBoolean("reversed");
        } catch (JSONException ex) {/* ROADMAP: do something with this exception*/}
    }

    @Override
    JSONObject toJSONObject() {
        JSONObject ret = null;
        try {
            ret = new JSONObject().
                    put("reversed", reversed);
        }
        catch (Exception impossible) {/* Impossible */}
        return ret;
    }
}
