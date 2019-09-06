package com.example.lord.engrisuru.abstract_module.ui;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.lord.engrisuru.abstract_module.ModuleSettings;

public abstract class ModuleSettingsFragment extends Fragment {
    protected View rootView; // Is null before onCreateView(), beware.

    @Override
    public void onPause() {
        super.onPause();

    }

    public abstract ModuleSettings getSettingsFromUI();

}
