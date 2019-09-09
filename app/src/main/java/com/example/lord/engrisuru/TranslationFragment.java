package com.example.lord.engrisuru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lord.engrisuru.abstract_module.ModuleSettings;
import com.example.lord.engrisuru.abstract_module.TranslationModule;
import com.example.lord.engrisuru.abstract_module.TranslationTask;
import com.example.lord.engrisuru.kanji_module.KanjiModule;
import com.example.lord.engrisuru.rft_module.ReversibleFileTranslationModule;


public class TranslationFragment extends Fragment {

    private static final int n = 10;
    private View rootView;
    private GridView translationOptions;
    private TextView askedWord;
    private String[] translations;
    private ArrayAdapter<String> adapter;
    private TranslationItemClickListener tcl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_translations, container, false);
        translationOptions = rootView.findViewById(R.id.possibleTranslations);
        askedWord = rootView.findViewById(R.id.askedWord);
        if (TranslationModule.selectedModule == null)
//            TranslationModule.selectedModule = ReversibleFileTranslationModule.initFromFile(this.getActivity());
            TranslationModule.selectedModule = new KanjiModule();
        TranslationModule.selectedModule.setSettings(ModuleSettings.getInstance(TranslationModule.selectedModule.getClass()));
        adapter = new ArrayAdapter<>(MainActivity.getMainActivity(), R.layout.translation, R.id.tvText);
        translationOptions.setAdapter(adapter);
        translationOptions.setNumColumns(2);
        translationOptions.setHorizontalSpacing(10);
        translationOptions.setVerticalSpacing(10);
        View[] children = new View[n];
        tcl = new TranslationItemClickListener(children, this::nextTranslationTask);
        nextTranslationTask();
        translationOptions.setOnItemClickListener(tcl);
        rootView.findViewById(R.id.add_trans_add_trans).setOnClickListener(this::addTranslation);
        //TODO fix multitouch problems
        return rootView;
    }

    private void nextTranslationTask() {
        TranslationTask tt = TranslationModule.selectedModule.getBufferedTranslationTask(n);
        translations = tt.translations;
        for (int i = 0; i < translations.length; i++) {
            translations[i] = Utils.StringUtils.hyphenate(translations[i]);
        }
        tcl.tt = tt;
        MainActivity.getMainActivity().runOnUiThread(() ->
        {
            askedWord.setText(tt.word);
            askedWord.invalidate();
            adapter.clear();
            adapter.addAll(translations);
            adapter.notifyDataSetChanged();
        });

    }

    private void addTranslation(View view) {
        ReversibleFileTranslationModule db = (ReversibleFileTranslationModule) TranslationModule.selectedModule;
        String word = ((EditText) rootView.findViewById(R.id.add_trans_word)).getText().toString();
        String translation = ((EditText) rootView.findViewById(R.id.add_trans_trans)).getText().toString();
        try {
            boolean success = db.addWord(word, translation);
            if (success && db.updateDatabase(true))
                Utils.toast("Added!"); // Only try to write to file if word added successfully
            else Utils.toast("Problem detected, translation not added!");
            ((EditText) rootView.findViewById(R.id.add_trans_trans)).getText().clear();
            ((EditText) rootView.findViewById(R.id.add_trans_word)).getText().clear();
        } catch (Exception ignored) {
        }
    }

}
