package com.example.lord.engrisuru;

import com.example.lord.engrisuru.abstract_module.TranslationModule;
import com.example.lord.engrisuru.rft_module.ReversibleFileTranslationModule;

public final class ApplicationPreferences {

    // ROADMAP: use reflection, maybe?
    public static String getSelectedTranslationModuleClass() {
        String rftModuleName = ReversibleFileTranslationModule.class.getSimpleName();
        return Utils.getSharedPreferences().getString("selectedModuleClassName", rftModuleName);
    }

    private static void setSelectedTranslationModuleClassName(String moduleName) {
        Utils.getSharedPreferences().edit().
                putString("selectedModuleClassName", moduleName).
                apply();
    }

    public static void setSelectedTranslationModuleClass(Class<? extends TranslationModule> moduleClass) {
        setSelectedTranslationModuleClassName(moduleClass.getSimpleName());
    }
}

