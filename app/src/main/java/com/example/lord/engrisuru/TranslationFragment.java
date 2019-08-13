package com.example.lord.engrisuru;

import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

import de.mfietz.jhyphenator.HyphenationPattern;
import de.mfietz.jhyphenator.Hyphenator;


public class TranslationFragment extends Fragment {

    private View rootView;

    private GridView translationOptions;
    private TextView askedWord;
    private String[] translations;
    private static final int n = 10;
    private ArrayAdapter<String> adapter;
    TranslationClickListener tcl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_translations, container, false);
        translationOptions = rootView.findViewById(R.id.possibleTranslations);

        TranslationModule.selectedModule = ReversibleFileTranslationModule.initFromFile(this.getActivity());

        final TranslationTask tt = TranslationModule.selectedModule.nextTranslation(n);
        translations = tt.translations;
        askedWord = rootView.findViewById(R.id.askedWord);
        askedWord.setText(tt.word);
        ArrayList<String> translationsList = new ArrayList<>(Arrays.asList(translations));
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
                if (child == null) return;
                child.setOnClickListener(tcl);
            }
            tcl.refreshButtons(getActivity());
        });
        rootView.findViewById(R.id.add_trans_add_trans).setOnClickListener(this::addTranslation);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    void nextTranslationTask()
    {
        TranslationTask tt = TranslationModule.selectedModule.nextTranslation(n);
        translations = tt.translations;
        for (int i = 0; i < translations.length; i++) {
            translations[i] = TextUtils.join("\u00AD", Hyphenator.getInstance(HyphenationPattern.RU).hyphenate(translations[i])); // FIXME: only for testing!
        }
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

    void addTranslation(View view)
    {
        ReversibleFileTranslationModule db = (ReversibleFileTranslationModule)TranslationModule.selectedModule;
        String word = ((EditText)rootView.findViewById(R.id.add_trans_word)).getText().toString();
        String translation = ((EditText)rootView.findViewById(R.id.add_trans_trans)).getText().toString();
        try {
            boolean success = db.addWord(word, translation);
            if (success && db.updateDatabase(true)) Utils.toast("Added!"); // Only try to write to file if word added successfully
            else Utils.toast("Problem detected, translation not added!");
            ((EditText)rootView.findViewById(R.id.add_trans_trans)).getText().clear();
            ((EditText)rootView.findViewById(R.id.add_trans_word)).getText().clear();
        } catch (Exception ignored) {}
    }

}
