package com.example.lord.engrisuru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class RFTModuleSettingsFragment extends ModuleSettingsFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_module_rft_settings, container, false);

        {
            ReversibleFileTranslationModule currModule = (ReversibleFileTranslationModule)TranslationModule.selectedModule;
            ((CheckBox)rootView.findViewById(R.id.reverseModuleCheckbox)).setChecked(currModule.isReversed());
            rootView.findViewById(R.id.reverseModuleCheckbox).setOnClickListener(view ->
                    currModule.settings = getSettings());

        } // TODO: bind settings to module instance

        this.rootView = rootView;
        return rootView;
    }

    RFTModuleSettings getSettings() {
        RFTModuleSettings ret = new RFTModuleSettings();
        ret.reversed = ((CheckBox)rootView.findViewById(R.id.reverseModuleCheckbox)).isChecked();
        return ret;
    }
}