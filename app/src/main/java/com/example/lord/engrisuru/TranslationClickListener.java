package com.example.lord.engrisuru;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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

    private boolean blocked;

    TranslationClickListener(TranslationTask tt_arg, View[] translationsLayouts_arg, Runnable afterClick_arg)
    {
        this.tt = tt_arg;
        this.translationsLayouts =  translationsLayouts_arg;
        this.afterClick = afterClick_arg;
    }

    void refreshButtons(Context context)
    {
//        Log.e("1",Integer.toString(translationsLayouts.length) );
        for (View v: translationsLayouts) {
//            if  (defaultDrawable == null)
            defaultDrawable = ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.translation);;
            v.setBackground(defaultDrawable);
//            Log.e("wah", v.getBackground().toString()) ;
            v.setClickable(true);
            v.setEnabled(true);
            v.invalidate();
        }
    }

    @Override
    public void onClick(View view) {

        if (blocked)
                return;
        blocked = true;
//        if  (correctDrawable == null) {
//            correctDrawable = ContextCompat.getDrawable(view.getContext(), R.drawable.translation_correct);
//            incorrectDrawable = ContextCompat.getDrawable(view.getContext(), R.drawable.translation_incorrect);
//        }
//        Log.i("CLICKED", "CLICKEd");
        for (View v: translationsLayouts) {
            correctDrawable = ContextCompat.getDrawable(view.getContext().getApplicationContext(), R.drawable.translation_correct);
            incorrectDrawable = ContextCompat.getDrawable(view.getContext().getApplicationContext(), R.drawable.translation_incorrect);

            v.setClickable(false);
            v.setEnabled(false);
            TextView tv = v.findViewById(R.id.tvText);
            if (tv.getText() == tt.correctTranslation) v.setBackground(correctDrawable);
            else if (v == view) v.setBackground(incorrectDrawable);
        }
        executor.schedule(() -> {
                blocked = false;
                afterClick.run();
        }, 1500, TimeUnit.MILLISECONDS);
    }
}
