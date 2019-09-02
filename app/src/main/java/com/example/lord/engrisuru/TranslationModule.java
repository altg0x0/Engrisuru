package com.example.lord.engrisuru;

public abstract class TranslationModule {
    public static TranslationModule selectedModule;

    public abstract ModuleSettings getSettings();
    public abstract void setSettings(ModuleSettings settings);

    public abstract TranslationTask nextTranslation(int n);

    public abstract boolean updateDatabase(boolean... params); // Should return true if written successfully

    public abstract boolean exportModule();

    public abstract boolean modifyDataByAnswer(TranslationTask task);
}
