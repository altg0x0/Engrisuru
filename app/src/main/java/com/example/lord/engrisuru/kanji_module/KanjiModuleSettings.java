package com.example.lord.engrisuru.kanji_module;

import android.util.Log;

import com.example.lord.engrisuru.abstract_module.ModuleSettings;

import org.json.JSONException;
import org.json.JSONObject;

public class KanjiModuleSettings extends ModuleSettings {
//    EnumSet<KanjiModuleTaskType> taskTypes = EnumSet.of(KanjiModuleTaskType.ENGLISH_MEANINGS);
    int minGrade = 1, maxGrade = 1;

    public KanjiModuleSettings (){}

    public KanjiModuleSettings (JSONObject json) {
        try {
            minGrade = json.getJSONObject("settings").getInt("minGrade");
            maxGrade = json.getJSONObject("settings").getInt("maxGrade");
        } catch (JSONException ex) {
            Log.i("Exception", "KanjiModuleSettings: Oh my GOD!");
            /* ROADMAP: do something with this exception*/}

    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject ret = null;
        try {
            ret = new JSONObject().
                    put("minGrade", minGrade).
                    put("maxGrade", maxGrade);
        }
        catch (Exception impossible) {/* Impossible */}
        return ret;
    }

}