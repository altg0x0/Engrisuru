package com.example.lord.engrisuru;

import android.util.Log;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by lord on 23/02/18.
 */

public class DataBase {
    public static DataBase currentDataBase;

    private String[] keys;
    private String[] values;
    private Double[] weights;
    JSONObject dict;
    private Random rnd = new Random();
    private EnumeratedDistribution<String> wordsWeighted;

    DataBase(String json)
    {
        try {
            this.dict = new JSONObject(json);
        }
        catch (JSONException ex) {
            Log.e("JSON", "Very bad JSON");
        }
        updateDatabase();
    }
    void updateDatabase(boolean... params)
    {
        boolean save = (params.length >= 1) && params[0];
        int length = dict.length();
        keys = new String[length];
        values = new String[length];
        weights = new Double[length];
        Iterator<String> iterator = dict.keys();
        int i = 0;
        while (iterator.hasNext())
        {
            try {
                keys[i] = iterator.next();
                JSONObject wordObj = dict.getJSONObject(keys[i]);
                values[i] = (String) wordObj.get("V");
                weights[i] = wordObj.getDouble("P");
                i++;
            }
            catch (JSONException ex) {/*10 GOTO HELL;*/}
        }
        wordsWeighted = new EnumeratedDistribution<String>(Utils.zip(keys, weights));
        if (!save) return;
        try {
            Utils.FS.writeToSandbox("db.json", dict.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    boolean addWord(String word, String trans) {
        if (Objects.equals(word, "") || Objects.equals(trans, "")) return false;
        JSONObject value = new JSONObject();
        try {
            value.put("P", 1);
            value.put("V", trans);
            dict.put(word, value);
            return true;
        } catch (JSONException ex) {return false;}
    }

    TranslationTask nextTranslation(int n)
    {
        try {
            String word = wordsWeighted.sample();
            String correctTranslation = (String) dict.getJSONObject(word).get("V");
            List<String> translations = pickNRandom(values, n);
            if (!translations.remove(correctTranslation))
                translations.remove(translations.size() - 1);
            translations.add(correctTranslation);
            Collections.shuffle(translations, rnd);
            Object[] objectArray = translations.toArray();
            String[] translationsArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);
            return new TranslationTask(word, translationsArray, correctTranslation);
        }
        catch (JSONException ex) {/* FWCK YOU*/}
        return null;
    }

    private static List<String> pickNRandom(String[] src, int n)
    {
        List<String> lst = Arrays.asList(src);
        List<String> copy = new LinkedList<String>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }

    void multiplyProb(String word, double coef) {
        try {
            JSONObject wordObj = dict.getJSONObject(word);
            wordObj.put("P", wordObj.getDouble("P") * coef);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateDatabase(true); //TODO DB should not be overwritten!
        try {
            Log.e("multiplyProb: ", dict.getJSONObject(word).get("P").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
