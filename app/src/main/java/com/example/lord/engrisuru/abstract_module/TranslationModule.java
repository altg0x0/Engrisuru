package com.example.lord.engrisuru.abstract_module;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class TranslationModule {
    public static TranslationModule selectedModule;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<TranslationTask> buffered;

    public abstract ModuleSettings getSettings();

    public void setSettings(ModuleSettings settings) {
        applySettings(settings);
        buffered = null;
    }

    protected abstract void applySettings(ModuleSettings settings);

    protected abstract TranslationTask nextTranslation(int n);

    public abstract boolean updateDatabase(boolean... params); // Should return true if written successfully

    public abstract boolean exportModule();

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
