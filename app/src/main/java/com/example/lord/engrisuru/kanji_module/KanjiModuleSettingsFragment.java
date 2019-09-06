package com.example.lord.engrisuru.kanji_module;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lord.engrisuru.R;
import com.example.lord.engrisuru.abstract_module.ModuleSettings;
import com.example.lord.engrisuru.abstract_module.ui.ModuleSettingsFragment;

public class KanjiModuleSettingsFragment extends ModuleSettingsFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_module_kanji_settings, container, false);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public ModuleSettings getSettingsFromUI() {
        KanjiModuleSettings settings = new KanjiModuleSettings();
        com.appyvet.materialrangebar.RangeBar rangeBar = rootView.findViewById(R.id.kanji_grade_range_picker);
        settings.minGrade = rangeBar.getLeftIndex() + 1;
        settings.maxGrade = rangeBar.getRightIndex() + 1;
        return settings;
    }
}