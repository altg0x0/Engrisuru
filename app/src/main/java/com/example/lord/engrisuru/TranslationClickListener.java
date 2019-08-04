package com.example.lord.engrisuru;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lord on 02/03/18.
 */

public class TranslationClickListener implements View.OnClickListener
{
    TranslationTask tt;
    View[] translationsLayouts;
    private Runnable afterClick;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private Drawable correctDrawable;
    private Drawable incorrectDrawable;
    private Drawable defaultDrawable;

    private boolean blocked = false;

    TranslationClickListener(TranslationTask tt_arg, View[] translationsLayouts_arg, Runnable afterClick_arg)
    {
        this.tt = tt_arg;
        this.translationsLayouts =  translationsLayouts_arg;
        this.afterClick = afterClick_arg;
    }

    void refreshButtons(Context context)
    {
        for (View v: translationsLayouts) {
            defaultDrawable = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.translation);
            v.setBackground(defaultDrawable);
            v.setClickable(true);
            v.setEnabled(true);
            v.invalidate();
        }
    }

    @Override
    public void onClick(View view)
    {
        if (blocked)
                return;
        for (View v: translationsLayouts) if (v == null) return;

        blocked = true;
        correctDrawable = ContextCompat.getDrawable(view.getContext().getApplicationContext(), R.drawable.translation_correct);
        incorrectDrawable = ContextCompat.getDrawable(view.getContext().getApplicationContext(), R.drawable.translation_incorrect);
        boolean overallCorrect = false;
        for (View v: translationsLayouts) {
            v.setClickable(false);
            v.setEnabled(false);
            TextView tv = v.findViewById(R.id.tvText);
            String userAnswer = (String)tv.getText();
            boolean correct = tt.isAnswerCorrect(userAnswer); // Is the text in v the correct translation?
            if (v == view) {
                tt.answer = userAnswer;
                overallCorrect = TranslationModule.selectedModule.modifyDataByAnswer(tt);
            }
            if (correct) v.setBackground(correctDrawable);
            else if (v == view) v.setBackground(incorrectDrawable);
        }
        executor.schedule(() -> {
                blocked = false;
                afterClick.run();
        }, overallCorrect? 800 : 1700, TimeUnit.MILLISECONDS);
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
