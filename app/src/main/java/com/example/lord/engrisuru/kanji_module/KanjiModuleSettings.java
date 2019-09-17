package com.example.lord.engrisuru.kanji_module;

import android.util.Log;

import com.example.lord.engrisuru.Utils;
import com.example.lord.engrisuru.abstract_module.ModuleSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;

public class KanjiModuleSettings extends ModuleSettings {
    EnumSet<KanjiModuleTaskType> taskTypes = EnumSet.of(KanjiModuleTaskType.ENGLISH_MEANINGS);
    int minGrade = 1, maxGrade = 1;
    boolean gentleMode = false;

    public KanjiModuleSettings() {
    }

    public KanjiModuleSettings(JSONObject json) {
        try {
            gentleMode = json.getJSONObject("settings").getBoolean("gentleMode");
            minGrade = json.getJSONObject("settings").getInt("minGrade");
            maxGrade = json.getJSONObject("settings").getInt("maxGrade");
            taskTypes = Utils.Converter.stringToEnumSet(json.getJSONObject("settings").getString("taskTypes"), KanjiModuleTaskType.class);
        } catch (JSONException ex) {
            Log.i("Exception", "KanjiModuleSettings: Oh my GOD!");
            /* ROADMAP: do something with this exception*/
        }

    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject ret = null;
        try {
            ret = new JSONObject().
                    put("gentleMode", gentleMode).
                    put("minGrade", minGrade).
                    put("maxGrade", maxGrade).
                    put("taskTypes", Utils.Converter.enumSetToString(taskTypes));

        } catch (Exception impossible) {/* Impossible */}
        return ret;
    }

    @Override
    public String getModuleName() {
        return KanjiModule.class.getSimpleName();
    }
}
