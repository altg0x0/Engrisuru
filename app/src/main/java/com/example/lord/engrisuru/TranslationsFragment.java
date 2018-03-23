package com.example.lord.engrisuru;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class TranslationsFragment extends Fragment {

    private View rootView;

    private GridView translationOptions;
    private DataBase db;
    private TextView askedWord;
    private String[] translations;
    private static final int n = 16;
    private ArrayAdapter<String> adapter;
    TranslationClickListener tcl;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.fragment_translations, container, false);
//        return rootView;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_translations, container, false);
        translationOptions = rootView.findViewById(R.id.possibleTranslations);
        String json = readJsonFromRes("defaultjson");
        db = new DataBase(json);
        DataBase.currentDataBase = db;
        final TranslationTask tt = db.nextTranslation(n);
        translations = tt.translations;
        askedWord = rootView.findViewById(R.id.askedWord);
        askedWord.setText(tt.word);
        ArrayList<String> translationsList = new ArrayList<String>(Arrays.asList(translations));
        adapter = new ArrayAdapter<>(getActivity(), R.layout.translation, R.id.tvText, translationsList);
        translationOptions.setAdapter(adapter);
        translationOptions.setNumColumns(2);
        translationOptions.setHorizontalSpacing(10);
        translationOptions.setVerticalSpacing(10);

        translationOptions.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (getActivity() == null) return;
            View[] children = new View[n];
            if (tcl == null)
                tcl = new TranslationClickListener(tt, children, this::nextTranslationTask);
            else tcl.translationsLayouts = children;
            for (int i = 0; i < n; i++) {
                View child = translationOptions.getChildAt(i);
                children[i] = child;
                if (child != null)
                    child.setOnClickListener(tcl);
                else return;
            }
            tcl.refreshButtons(getActivity());
        });
        rootView.findViewById(R.id.add_trans_add_trans).setOnClickListener(this::addTranslation);
        return rootView;
    }

    void nextTranslationTask()
    {
        TranslationTask tt = db.nextTranslation(n);
        translations = tt.translations;
        tcl.tt = tt;
        getActivity().runOnUiThread(()->
        {
            askedWord.setText(tt.word);
            askedWord.invalidate();
            adapter.clear();
            adapter.addAll(translations);
            adapter.notifyDataSetChanged();
        });

    }

    String readJsonFromRes(String filename)
    {
        try {
            InputStream ins = getResources().openRawResource(
                    getResources().getIdentifier(filename,
                            "raw", getActivity().getPackageName()));
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ins.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String json = result.toString("UTF-8");
            return json;
        } catch (Exception ignored) {}
        return null;
    }

    void addTranslation(View view)
    {
        JSONObject dict = DataBase.currentDataBase.dict;
        String word = ((EditText)rootView.findViewById(R.id.add_trans_trans)).getText().toString();
        String translation = ((EditText)rootView.findViewById(R.id.add_trans_word)).getText().toString();
        if (Objects.equals(word, "") || Objects.equals(translation, "")) return;
        if (dict.has(word)) return;
        try {
            dict.put(word, translation);
            DataBase.currentDataBase.updateDatabase();
            Toast toast = Toast.makeText(rootView.getContext(), "Added!", Toast.LENGTH_SHORT);
            toast.show();
            ((EditText)rootView.findViewById(R.id.add_trans_trans)).getText().clear();
            ((EditText)rootView.findViewById(R.id.add_trans_word)).getText().clear();
        } catch (Exception ignored) {}


    }

}
