package com.example.lord.engrisuru;

/**
 * Created by lord on 23/02/18.
 */

public class TranslationTask {
    String word;
    String[] translations;
    String correctTranslation;

    public TranslationTask(String word_arg, String[] translations_arg, String correctTranslation_arg)
    {
        word = word_arg;
        translations = translations_arg;
        correctTranslation = correctTranslation_arg;
    }
}
