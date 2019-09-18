package com.example.lord.engrisuru.abstract_module;

import android.util.Log;

import com.example.lord.engrisuru.ApplicationPreferences;
import com.example.lord.engrisuru.MainActivity;
import com.example.lord.engrisuru.kanji_module.KanjiModule;
import com.example.lord.engrisuru.rft_module.ReversibleFileTranslationModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class TranslationModule {
    public static TranslationModule selectedModule;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<TranslationTask> buffered;

    public static  TranslationModule getInstanceByPreferences() {
        final String rftModuleName = ReversibleFileTranslationModule.class.getSimpleName();
        final String kanjiModuleName = KanjiModule.class.getSimpleName();

        String targetModuleName = ApplicationPreferences.getSelectedTranslationModuleClass();
        TranslationModule ret = null;
        if (targetModuleName.equals(kanjiModuleName)) {
            ret = new KanjiModule();
        } else if(targetModuleName.equals(rftModuleName)) {
            ret = ReversibleFileTranslationModule.initFromFile(MainActivity.getAppContext());
        }
        return ret;
    }


    public abstract ModuleSettings getSettings();

    public void setSettings(ModuleSettings settings) {
        applySettings(settings);
        buffered = null;
    }

    protected abstract void applySettings(ModuleSettings settings);

    protected abstract TranslationTask nextTranslation(int n);

    public abstract boolean updateDatabase(boolean... params); // Should return true if written successfully

    public abstract boolean exportModule();

    public abstract boolean importModule();

    public abstract void modifyDataByAnswer(TranslationTask task);

    public TranslationTask getBufferedTranslationTask(int n) {
        if (buffered == null)
            buffered = executor.submit(() -> nextTranslation(n));
        try {
            TranslationTask ret = buffered.get(10000, TimeUnit.SECONDS);
            buffered = executor.submit(() -> nextTranslation(n));
            return ret;
        } catch (Exception ex) {
            Log.e("TranslationModule", "getBufferedTranslationTask: ", ex);
        }
        return null; // Should never happen
    }
}
