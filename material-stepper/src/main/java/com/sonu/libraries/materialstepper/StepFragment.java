package com.sonu.libraries.materialstepper;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

/**
 * Created by sonu on 9/2/17.
 */

public abstract class StepFragment extends Fragment implements NavigationCallbacks{
    public static final int SKIPPED = 2;
    public static final int INCOMPLETE = 0;
    public static final int COMPLETED = 1;

    private int stepIndex = -1;

    private int status = INCOMPLETE;

    private MaterialStepper parent;
    private Context mContext;
    private DataTransferCallbacks dataTransferCallbacks;
    private ViewPagerCallbacks viewPagerCallbacks;
    private StatusBarCallbacks statusBarCallbacks;

    public StepFragment(){
        //do nothing
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public final MaterialStepper getParent() {
        return parent;
    }

    public final void setParent(MaterialStepper parent) {
        this.parent = parent;
        this.dataTransferCallbacks = parent;
        this.viewPagerCallbacks = parent;
        this.statusBarCallbacks = parent;
    }

    public final Context getContext() {
        return mContext;
    }

    public boolean canGoBack() {
        return true;
    }

    public boolean canSkip() {
        return true;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public final int getStepIndex() {
        return stepIndex;
    }

    protected final void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    protected final void sendData(Bundle bundle) {
        dataTransferCallbacks.sendData(bundle, stepIndex+1);
    }

    protected final Bundle getData() {
        return dataTransferCallbacks.getData(stepIndex);
    }

    protected final void sendDataTo(Bundle bundle, int stepIndex) {
        dataTransferCallbacks.sendData(bundle, stepIndex);
    }

    protected final Bundle getDataOf(int stepIndex) {
        return dataTransferCallbacks.getData(stepIndex);
    }

    @Override
    public StepFragment onRightCLicked() {
        this.status = COMPLETED;
        viewPagerCallbacks.swipeToPage(this, this.stepIndex+1);
        return this;
    }

    @Override
    public StepFragment onLeftClicked() {
        viewPagerCallbacks.swipeToPage(this, this.stepIndex-1);
        return this;
    }

    @Override
    public StepFragment onSkipClicked(){
        this.status = SKIPPED;
        viewPagerCallbacks.swipeToPage(this, this.stepIndex+1);
        return this;
    }

    @Override
    public void onBackPressed() {
        if(canGoBack()) {
            onLeftClicked();
        }
    }

    @Override
    public void initButtons(Button leftButton, Button rightButton, Button skipButton) {
        if(canSkip()) {
            skipButton.setVisibility(View.VISIBLE);
        } else {
            skipButton.setVisibility(View.GONE);
        }

        if (stepIndex == 0 || !canGoBack()) {
            leftButton.setVisibility(View.GONE);
            rightButton.setTextColor(Color.BLACK);
            rightButton.setText("NEXT");
            rightButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_black_24dp, 0);
        } else {
            if (leftButton.getVisibility() == View.GONE) {
                leftButton.setVisibility(View.VISIBLE);
            }
            leftButton.setTextColor(Color.BLACK);
            leftButton.setText("BACK");
            leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_navigate_before_black_24dp, 0, 0, 0);
            rightButton.setTextColor(Color.BLACK);
            rightButton.setText("NEXT");
            rightButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_black_24dp, 0);
        }
    }

    public abstract String getStepTitle();
}
