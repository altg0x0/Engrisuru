package com.example.lord.engrisuru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.lord.engrisuru.abstract_module.TranslationModule;
import com.example.lord.engrisuru.abstract_module.ui.ModuleSettingsFragment;
import com.example.lord.engrisuru.kanji_module.KanjiModuleSettingsFragment;
import com.example.lord.engrisuru.rft_module.ReversibleFileTranslationModule;

public class DatabaseManipulationFragment extends Fragment {
    private ModuleSettingsFragment settingsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_database_manipulation, container, false);
        initModuleSelectionSpinner(rootView);
        rootView.findViewById(R.id.export_database).setOnClickListener((__) -> exportJSONDatabase());
        rootView.findViewById(R.id.import_database).setOnClickListener((__) -> importJSONDatabase());
//        settingsFragment = new RFTModuleSettingsFragment();
        settingsFragment = new KanjiModuleSettingsFragment();
        getChildFragmentManager().beginTransaction().add(R.id.moduleSettingsFrame, settingsFragment).commit();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        TranslationModule.selectedModule.setSettings(settingsFragment.getSettingsFromUi());
        TranslationModule.selectedModule.getSettings().writeToSandbox();
    }

    private void initModuleSelectionSpinner(View rootView) {
        Spinner moduleSelectionSpinner = rootView.findViewById(R.id.moduleSelectionSpinner);
        final String[] moduleOptionsNames = new String[]{"Eng-Rus simple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, moduleOptionsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moduleSelectionSpinner.setAdapter(adapter);
    }

    // ROADMAP: refactor these UI-unrelated functions
    private boolean exportJSONDatabase() {
        return TranslationModule.selectedModule.exportModule();
    }

    private void importJSONDatabase() {
        String json = Utils.FS.readFileFromSD("import.json");
        if (json == null) {
            Utils.toast("Import failed TT");
            return;
        }
        TranslationModule.selectedModule = new ReversibleFileTranslationModule(json);
        TranslationModule.selectedModule.updateDatabase(true);
        Utils.toast("Import successful!");
    }

}
