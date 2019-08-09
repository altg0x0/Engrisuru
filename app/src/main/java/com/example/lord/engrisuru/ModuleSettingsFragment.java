package com.example.lord.engrisuru;

import android.view.View;

import androidx.fragment.app.Fragment;

abstract class ModuleSettingsFragment extends Fragment {
    protected View rootView; // Is null before onCreateView(), beware.

    @Override
    public void onPause() {
        super.onPause();

    }

    abstract ModuleSettings getSettings();

}
