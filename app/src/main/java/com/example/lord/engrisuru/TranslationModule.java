package com.example.lord.engrisuru;

abstract class TranslationModule {
    static TranslationModule selectedModule;

    abstract ModuleSettings getSettings();
    abstract void setSettings(ModuleSettings settings);

    abstract TranslationTask nextTranslation(int n);

    abstract boolean updateDatabase(boolean... params); // Should return true if written successfully

    abstract boolean exportModule();

    abstract boolean modifyDataByAnswer(TranslationTask task);
}
