package com.example.lord.engrisuru;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lord on 02/03/18.
 */

public class TranslationItemClickListener implements AdapterView.OnItemClickListener
{
    TranslationTask tt = null;
    private  View[] translationsLayouts;
    private Runnable afterClick;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private Drawable correctDrawable;
    private Drawable incorrectDrawable;
    private Drawable defaultDrawable;

    private boolean blocked = false;

    TranslationItemClickListener(View[] translationsLayouts_arg, Runnable afterClick_arg)
    {
        this.translationsLayouts =  translationsLayouts_arg;
        this.afterClick = afterClick_arg;
    }

    void refreshButtons(Context context)
    {
        for (View v: translationsLayouts) {
            defaultDrawable = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.translation);
            v.setBackground(defaultDrawable);
        }
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {
        if (blocked)
                return;
        translationsLayouts = new View[parent.getCount()];
        for (int i = 0; i < translationsLayouts.length; i++) {
            if (parent.getChildAt(i) == null) return;
            translationsLayouts[i] = parent.getChildAt(i);
        }

        blocked = true;
        correctDrawable = ContextCompat.getDrawable(view.getContext().getApplicationContext(), R.drawable.translation_correct);
        incorrectDrawable = ContextCompat.getDrawable(view.getContext().getApplicationContext(), R.drawable.translation_incorrect); // No more than one of each of these is used, so no cycle is needed
        boolean overallCorrect = false;
        for (View v: translationsLayouts) {
            v.setClickable(false);
            v.setEnabled(false);
            TextView tv = v.findViewById(R.id.tvText);
            String userAnswer = (String)tv.getText();
            boolean correct = tt.isAnswerCorrect(userAnswer.replace("\u00AD","")); // Is the text in v the correct translation?
            if (v == view) {
                tt.answer = userAnswer;
                overallCorrect = TranslationModule.selectedModule.modifyDataByAnswer(tt);
            }
            if (correct) v.setBackground(correctDrawable);
            else if (v == view) v.setBackground(incorrectDrawable);
        }
        executor.schedule(() -> {
                blocked = false;
                ((Activity)parent.getContext()).runOnUiThread(() -> refreshButtons(parent.getContext()));
                afterClick.run();
        }, (overallCorrect? 800 : 1700) / (MainActivity.LEARNING_MODE? 1 : 10), TimeUnit.MILLISECONDS); // Decreases delay if LEARNING_MODE is disabled
    }
}

//    private void hideKeyboard()
//    {
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//
//    }
