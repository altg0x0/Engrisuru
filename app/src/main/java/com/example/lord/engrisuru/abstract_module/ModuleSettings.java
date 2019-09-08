package com.example.lord.engrisuru.abstract_module;

import com.example.lord.engrisuru.Utils;
import com.example.lord.engrisuru.kanji_module.KanjiModule;
import com.example.lord.engrisuru.kanji_module.KanjiModuleSettings;
import com.example.lord.engrisuru.rft_module.RFTModuleSettings;
import com.example.lord.engrisuru.rft_module.ReversibleFileTranslationModule;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ModuleSettings {
    // ROADMAP: consider moving this to another file
    // ROADMAP: use reflection, maybe?
    public static ModuleSettings getInstance(Class<? extends TranslationModule> moduleClass) {
        String settingsFilename = moduleClass.getSimpleName() + "_settings.json";
        boolean settingsFileExists = Utils.FS.fileExistsInSandbox(settingsFilename);
        JSONObject json = null;
        if (settingsFileExists) {
            try {
                json = new JSONObject(Utils.FS.readFromSandbox(settingsFilename));
            } catch (JSONException ex) {/* Return default*/}
        }
        ModuleSettings ret = null;
        if (moduleClass == ReversibleFileTranslationModule.class) {
            ret = json != null ? new RFTModuleSettings(json) : new RFTModuleSettings();
        } else if (moduleClass == KanjiModule.class) {
            ret = json != null ? new KanjiModuleSettings(json) : new KanjiModuleSettings();
        }
        return ret;
    }

    public abstract JSONObject toJSONObject(); // Should only return settings, not timestamps or other meta-information

    public boolean writeToSandbox() {
        return Utils.FS.writeToSandbox(this.getClass().getSimpleName().
                replace("Settings", "") + "_settings.json", this.toString()); // File should be named like "KanjiModule_settings.json"
    }

    @Override
    public String toString() // Includes meta-information
    {
        try {
            final JSONObject finalJson = new JSONObject().
                    put("className", this.getClass().getSimpleName()).
                    put("settings", toJSONObject());


            return finalJson.toString(4);
        } catch (JSONException ex) { /* Should never happen */}
        return null;
    }
}
