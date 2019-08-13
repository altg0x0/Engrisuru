package com.example.lord.engrisuru;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DatabaseManipulationFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_database_manipulation, container, false);
        initModuleSelectionSpinner(rootView);
        rootView.findViewById(R.id.export_database).setOnClickListener((__) -> exportJSONDatabase());
        rootView.findViewById(R.id.import_database).setOnClickListener((__) -> importJSONDatabase());
        getChildFragmentManager().beginTransaction().add(R.id.moduleSettingsFrame, new RFTModuleSettingsFragment()).commit();
        return rootView;
    }

    private void initModuleSelectionSpinner(View rootView)
    {
        Spinner moduleSelectionSpinner = rootView.findViewById(R.id.moduleSelectionSpinner);
        final String[] moduleOptionsNames = new String[]{"Eng-Rus simple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),  android.R.layout.simple_spinner_item, moduleOptionsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moduleSelectionSpinner.setAdapter(adapter);
    }

    // ROADMAP: refactor these UI-unrelated functions
    boolean exportJSONDatabase()
    {
        return  TranslationModule.selectedModule.exportModule();
    }

    void importJSONDatabase() {
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
