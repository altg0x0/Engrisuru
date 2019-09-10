package com.example.lord.engrisuru.abstract_module.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lord.engrisuru.abstract_module.ModuleSettings;
import com.example.lord.engrisuru.abstract_module.TranslationModule;
import com.example.lord.engrisuru.kanji_module.KanjiModule;
import com.example.lord.engrisuru.kanji_module.KanjiModuleSettingsFragment;
import com.example.lord.engrisuru.rft_module.RFTModuleSettingsFragment;
import com.example.lord.engrisuru.rft_module.ReversibleFileTranslationModule;

public abstract class ModuleSettingsFragment extends Fragment {
    protected View rootView; // Is null before onCreateView(), beware.

    public static ModuleSettingsFragment getInstance(@Nullable Class<? extends TranslationModule> moduleClass) {
        if (moduleClass == null)
            moduleClass = TranslationModule.selectedModule.getClass();
        if (moduleClass == ReversibleFileTranslationModule.class) {
            return new RFTModuleSettingsFragment();
        }
        else if (moduleClass == KanjiModule.class) {
            return new KanjiModuleSettingsFragment();
        }
        return null; // Should never happen
    }


    @Override
    public void onPause() {
        super.onPause();
        getSettingsFromUi().writeToSandbox();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUiBySettings(ModuleSettings.getInstance(TranslationModule.selectedModule.getClass()));

    }

    public abstract ModuleSettings getSettingsFromUi();

    public abstract void setUiBySettings(ModuleSettings moduleSettings);

}
