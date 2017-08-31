package com.pseudozero.thyme.thyme.utils;

import android.app.Activity;
import android.view.ViewGroup;
import com.emredavarci.noty.Noty;

/**
 * Created by syd119 on 8/31/17.
 */

public class NotyAlert {

    public static void showWarning(Activity activity, ViewGroup viewGroup, String warning) {
        Noty.init(activity, warning, viewGroup,
                Noty.WarningStyle.SIMPLE)
                .setWarningBoxBgColor("#0098F1")
                .setWarningTappedColor("#01B9F1")
                .setWarningBoxPosition(Noty.WarningPos.BOTTOM)
                .setAnimation(Noty.RevealAnim.FADE_IN, Noty.DismissAnim.BACK_TO_BOTTOM, 300, 300)
                .show();
    }
}
