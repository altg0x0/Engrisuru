package com.example.lord.engrisuru;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        rootView.findViewById(R.id.export_database).setOnClickListener((__) -> exportJSONDatabase());
        rootView.findViewById(R.id.import_database).setOnClickListener((__) -> importJSONDatabase());
        return rootView;
    }

    public boolean exportJSONDatabase()
    {
        return  TranslationModule.selectedModule.exportModule();
    }

    public void importJSONDatabase() {
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
