package com.example.lord.engrisuru;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DatabaseManipulationFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_database_manipulation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_database_manipulation, container, false);
        rootView.findViewById(R.id.export_database).setOnClickListener(this::exportJSONDatabase);
        rootView.findViewById(R.id.import_database).setOnClickListener(this::importJSONDatabase);
        return rootView;
    }

    public boolean exportJSONDatabase(View view)
    {
//        Log.e("123", "Trying to export..." );
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return false;
        try {
            String data = DataBase.currentDataBase.dict.toString(4);
            writeToFile("database.json", data);
            return true;
        } catch (JSONException ex) {return false;}
    }

    public void importJSONDatabase(View view) {
        String json = Utils.FS.readFileFromSD("import.json");
        if (json == null) {
            Utils.toast("Import failed TT");
            return;
        }
        DataBase.currentDataBase = new DataBase(json);
        DataBase.currentDataBase.updateDatabase(true);
        Utils.toast("Import successful!");
    }

    private void writeToFile(String relativePath, String data) {
        String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/engrisuru/";
        File file = new File(basePath + relativePath);
        File dir = new File(file.getParent());
        Log.e("PATH", file.getAbsolutePath());
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }

        try (FileOutputStream outputStreamWriter = new FileOutputStream(file)) {
            outputStreamWriter.write(data.getBytes());
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
