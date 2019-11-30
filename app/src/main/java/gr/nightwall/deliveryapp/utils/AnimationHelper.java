package gr.nightwall.deliveryapp.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class AnimationHelper {

    //region SCREEN TOP BAR

    public static void changeScreenTitle(TextView tvTitle, String newTitle){
        if (newTitle == null){
            tvTitle.setVisibility(View.GONE);
            return;
        }

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(newTitle);
    }

    //endregion

    //region SCREENS



    //endregion

    //region DIALOGS

    public static void openDialog(View dialog, View shadowBack, YoYo.AnimatorCallback onStart){
        YoYo.with(Techniques.FadeInUp)
                .duration(150)
                .onStart(onStart)
                .playOn(dialog);

        YoYo.with(Techniques.FadeIn)
                .duration(200)
                .interpolate(new AccelerateInterpolator())
                .playOn(shadowBack);
    }

    public static void closeDialog(View dialog, View shadowBack, YoYo.AnimatorCallback onEnd){
        YoYo.with(Techniques.FadeOutDown)
                .duration(100)
                .onEnd(onEnd)
                .playOn(dialog);

        YoYo.with(Techniques.FadeOut)
                .duration(100)
                .interpolate(new DecelerateInterpolator())
                .playOn(shadowBack);
    }

    //endregion

}
