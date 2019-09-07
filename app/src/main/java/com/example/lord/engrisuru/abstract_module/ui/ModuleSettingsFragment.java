package com.example.lord.engrisuru.abstract_module.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lord.engrisuru.abstract_module.ModuleSettings;
import com.example.lord.engrisuru.abstract_module.TranslationModule;

public abstract class ModuleSettingsFragment extends Fragment {
    protected View rootView; // Is null before onCreateView(), beware.

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUiBySettings(ModuleSettings.getInstance(TranslationModule.selectedModule.getClass()));

    }

    public abstract ModuleSettings getSettingsFromUi();

    public abstract void setUiBySettings(ModuleSettings moduleSettings);

}
