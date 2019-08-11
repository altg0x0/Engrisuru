package com.example.lord.engrisuru;

import org.json.JSONException;
import org.json.JSONObject;

abstract class ModuleSettings {
    abstract JSONObject toJSONObject(); // Should only return settings, not timestamps or other meta-information

    boolean writeToSandbox()
    {
        return Utils.FS.writeToSandbox("settings.json", this.toString()); //TODO non-constant filename
    }

    //ROADMAP: create an abstract factory for moduleSettings

    @Override
    public String toString() // Includes meta-information
    {
        try {
            final JSONObject finalJson = new JSONObject().
                    put("className", this.getClass().getSimpleName()).
                    put("settings", toJSONObject());


            return finalJson.toString(4);
        }
        catch (JSONException ex) { /* Should never happen */}
        return null;
    }
}
