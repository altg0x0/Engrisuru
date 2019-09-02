package com.example.lord.engrisuru.rftm;

import com.example.lord.engrisuru.ModuleSettings;
import com.example.lord.engrisuru.Utils;

import org.json.JSONException;
import org.json.JSONObject;

class RFTModuleSettings extends ModuleSettings {
    boolean reversed = false;

    RFTModuleSettings(JSONObject json) // With meta-information
    {
        try {
            reversed = json.getJSONObject("settings").getBoolean("reversed");
        } catch (JSONException ex) {/* ROADMAP: do something with this exception*/}
    }

    RFTModuleSettings() {}

    @Override
    public JSONObject toJSONObject() {
        JSONObject ret = null;
        try {
            ret = new JSONObject().
                    put("reversed", reversed);
        }
        catch (Exception impossible) {/* Impossible */}
        return ret;
    }

    static RFTModuleSettings getSettingsFromFSWithDefault() {
        boolean settingsFileExists = Utils.FS.fileExistsInSandbox("settings.json"); //TODO variable filename
        if (settingsFileExists) {
            try {
                JSONObject json = new JSONObject(Utils.FS.readFromSandbox("settings.json"));
                return new RFTModuleSettings(json);
            } catch (JSONException ex) {/* Return default*/}
        }
        return new RFTModuleSettings();
    }
}
