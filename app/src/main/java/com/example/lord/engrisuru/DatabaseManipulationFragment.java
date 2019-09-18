package com.example.lord.engrisuru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.lord.engrisuru.abstract_module.TranslationModule;
import com.example.lord.engrisuru.abstract_module.ui.ModuleSettingsFragment;
import com.example.lord.engrisuru.kanji_module.KanjiModule;
import com.example.lord.engrisuru.rft_module.ReversibleFileTranslationModule;

public class DatabaseManipulationFragment extends Fragment {
    private ModuleSettingsFragment settingsFragment;
//    private boolean nullifySelectedModule = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_database_manipulation, container, false);
        initModuleSelectionSpinner(rootView);
        rootView.findViewById(R.id.export_database).setOnClickListener((__) -> exportSelectedModule());
        rootView.findViewById(R.id.import_database).setOnClickListener((__) -> importSelectedModule());
        setupSettingsFragment();
        return rootView;
    }

    private void setupSettingsFragment() {
        settingsFragment = ModuleSettingsFragment.getInstance(null);
        getChildFragmentManager().beginTransaction().replace(R.id.moduleSettingsFrame, settingsFragment).commit();
    }

    private void setupSettingsFragment(Class<? extends TranslationModule> moduleClass) {
        settingsFragment = ModuleSettingsFragment.getInstance(moduleClass);
        getChildFragmentManager().beginTransaction().replace(R.id.moduleSettingsFrame, settingsFragment).commit();
    }

    private void closeSettingsFragment() {
        getChildFragmentManager().beginTransaction().remove(settingsFragment).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        TranslationModule.selectedModule.setSettings(settingsFragment.getSettingsFromUi());
        TranslationModule.selectedModule.getSettings().writeToSandbox();
//        if (nullifySelectedModule) TranslationModule.selectedModule = null;
    }

    // TODO: allow more than one instance per module class
    // TODO: stop using spinner items indices for if statements
    private void initModuleSelectionSpinner(View rootView) {
        Spinner moduleSelectionSpinner = rootView.findViewById(R.id.moduleSelectionSpinner);
        moduleSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Class<? extends TranslationModule> moduleClass = ReversibleFileTranslationModule.class;
                if (position == 0) {
                    return;
                }
                else if (position == 1) {
                    moduleClass = ReversibleFileTranslationModule.class;
                } else if (position == 2) {
                    moduleClass = KanjiModule.class;
                }
                closeSettingsFragment();
                ApplicationPreferences.setSelectedTranslationModuleClass(moduleClass);
                TranslationModule.selectedModule = TranslationModule.getInstanceByPreferences();
                setupSettingsFragment(moduleClass);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });
    }

    // ROADMAP: refactor these UI-unrelated functions
    private boolean exportSelectedModule() {
        boolean ret = TranslationModule.selectedModule.exportModule();
        Utils.toast(ret ? "Export successful!" : "Export failed");
        return ret;
    }

    private boolean importSelectedModule() {
        boolean ret = TranslationModule.selectedModule.importModule();
        Utils.toast(ret ? "Import successful!" : "Import failed TT");
        return ret;
    }

}
