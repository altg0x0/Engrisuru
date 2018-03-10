package com.example.lord.engrisuru;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by lord on 23/02/18.
 */

public class DataBase {
    public static DataBase currentDataBase;

    private String[] keys;
    private String[] values;
    JSONObject dict;
    private Random rnd = new Random();

    DataBase(String json)
    {
        try {
            this.dict = new JSONObject(json);
        }
        catch (JSONException ex) {
            Log.e("JSON", "Very bad JSON");
        }
        int length = dict.length();
        keys = new String[length];
        values = new String[length];
        Iterator<String> iterator = dict.keys();
        int i = 0;
        while (iterator.hasNext())
        {
            try {
                keys[i] = iterator.next();
                values[i] = (String) dict.get(keys[i]);
                i++;
            }
            catch (JSONException ex) {/*10 GOTO HELL;*/}
        }
    }

    public TranslationTask nextTranslation(int n)
    {
        try {
            String word = getRandom(keys);
            String correctTranslation = (String) dict.get(word);
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

    static List<String> pickNRandom(String[] src, int n)
    {
    List<String> lst =Arrays.asList(src);
    List<String> copy = new LinkedList<String>(lst);
    Collections.shuffle(copy);
    return copy.subList(0, n);
    }

    public String getRandom(String[] array) {
    int random = rnd.nextInt(array.length);
    return array[random];
}


}
