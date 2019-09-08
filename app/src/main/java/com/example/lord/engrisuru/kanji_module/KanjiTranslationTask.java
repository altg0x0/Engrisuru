package com.example.lord.engrisuru.kanji_module;

import com.example.lord.engrisuru.abstract_module.TranslationTask;
import com.example.lord.engrisuru.japanese.Kanji;

class KanjiTranslationTask extends TranslationTask {
    Kanji askedKanji;

    KanjiTranslationTask(String word_arg, String[] translations_arg, String correctTranslation_arg, Kanji askedKanji) {
        super(word_arg, translations_arg, correctTranslation_arg);
        this.askedKanji = askedKanji;
    }

}
