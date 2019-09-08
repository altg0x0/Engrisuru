package com.example.lord.engrisuru.kanji_module;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appyvet.materialrangebar.RangeBar;
import com.example.lord.engrisuru.R;
import com.example.lord.engrisuru.abstract_module.ModuleSettings;
import com.example.lord.engrisuru.abstract_module.ui.ModuleSettingsFragment;

import java.util.EnumSet;

public class KanjiModuleSettingsFragment extends ModuleSettingsFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_module_kanji_settings, container, false);
        this.rootView = rootView;
        com.appyvet.materialrangebar.RangeBar rangeBar = rootView.findViewById(R.id.kanji_grade_range_picker);
        rangeBar.setTickTopLabels(getResources().getStringArray(R.array.kanji_grade_labels));
        return rootView;
    }

    // ROADMAP: USE REFLECTION, MAYBE?
    @Override
    public ModuleSettings getSettingsFromUi() {
        KanjiModuleSettings settings = new KanjiModuleSettings();
        com.appyvet.materialrangebar.RangeBar rangeBar = rootView.findViewById(R.id.kanji_grade_range_picker);
        settings.minGrade = rangeBar.getLeftIndex() + 1;
        settings.maxGrade = rangeBar.getRightIndex() + 1;
        CheckBox onyomiCheckbox = rootView.findViewById(R.id.kanji_module_onyomi_checkbox);
        CheckBox kunyomiCheckbox = rootView.findViewById(R.id.kanji_module_kunyomi_checkbox);
        CheckBox englishMeaningsCheckbox = rootView.findViewById(R.id.kanji_module_english_meanings_checkbox);
        EnumSet<KanjiModuleTaskType> enumSet = EnumSet.noneOf(KanjiModuleTaskType.class);
        if (onyomiCheckbox.isChecked()) enumSet.add(KanjiModuleTaskType.ONYOMI_READINGS);
        if (kunyomiCheckbox.isChecked()) enumSet.add(KanjiModuleTaskType.KUNYOMI_READINGS);
        if (englishMeaningsCheckbox.isChecked() || enumSet.isEmpty())
            enumSet.add(KanjiModuleTaskType.ENGLISH_MEANINGS);
        settings.taskTypes = enumSet;
        return settings;
    }

    @Override
    public void setUiBySettings(ModuleSettings moduleSettings) {
        RangeBar rangeBar = rootView.findViewById(R.id.kanji_grade_range_picker);
        KanjiModuleSettings settings = (KanjiModuleSettings) moduleSettings;
        rangeBar.setRangePinsByIndices(settings.minGrade - 1, settings.maxGrade - 1);
        CheckBox onyomiCheckbox = rootView.findViewById(R.id.kanji_module_onyomi_checkbox);
        CheckBox kunyomiCheckbox = rootView.findViewById(R.id.kanji_module_kunyomi_checkbox);
        CheckBox englishMeaningsCheckbox = rootView.findViewById(R.id.kanji_module_english_meanings_checkbox);
        onyomiCheckbox.setChecked(settings.taskTypes.contains(KanjiModuleTaskType.ONYOMI_READINGS));
        kunyomiCheckbox.setChecked(settings.taskTypes.contains(KanjiModuleTaskType.KUNYOMI_READINGS));
        englishMeaningsCheckbox.setChecked(settings.taskTypes.contains(KanjiModuleTaskType.ENGLISH_MEANINGS));
    }
}
