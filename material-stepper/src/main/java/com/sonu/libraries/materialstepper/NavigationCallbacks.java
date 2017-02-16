package com.sonu.libraries.materialstepper;

import android.widget.Button;

/**
 * Created by sonu on 9/2/17.
 */

public interface NavigationCallbacks {
    StepFragment onRightCLicked();
    StepFragment onLeftClicked();
    StepFragment onSkipClicked();
    void onBackPressed();
    void initButtons(Button leftButton, Button rightButton, Button skipButton);
}
