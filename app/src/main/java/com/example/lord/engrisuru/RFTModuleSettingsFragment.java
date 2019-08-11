package com.example.lord.engrisuru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

class RFTModuleSettingsFragment extends ModuleSettingsFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_module_rft_settings, container, false);

        {
            ReversibleFileTranslationModule currModule = (ReversibleFileTranslationModule)TranslationModule.selectedModule;
            ((CheckBox)rootView.findViewById(R.id.reverseModuleCheckbox)).setChecked(currModule.isReversed());
            rootView.findViewById(R.id.reverseModuleCheckbox).setOnClickListener(view ->
                    currModule.settings = getSettingsFromUI()
            );

        } // TODO: bind settings to module instance

        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        TranslationModule.selectedModule.getSettings().writeToSandbox();
    }

    RFTModuleSettings getSettingsFromUI() {
        RFTModuleSettings ret = null;
        try {
            ret = new RFTModuleSettings(new JSONObject(Utils.FS.readFromSandbox("settings.json")));
            ret.reversed = ((CheckBox)rootView.findViewById(R.id.reverseModuleCheckbox)).isChecked();

        } catch (JSONException ex) {/* TODO: change this mess*/}
        return ret;
    }
}
