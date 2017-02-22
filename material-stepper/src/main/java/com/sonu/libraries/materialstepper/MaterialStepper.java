package com.sonu.libraries.materialstepper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sonu on 7/2/17.
 */

public class MaterialStepper
        extends RelativeLayout
        implements DataTransferCallbacks, ViewPagerCallbacks, StatusBarCallbacks{

    private static final String TAG = MaterialStepper.class.getSimpleName();

    private MaterialStepperViewPager stepsViewpager;
    private CardView navigationBar, statusBar;
    private LinearLayout navigationBarViewHolder, statusBarViewHolder;
    private HorizontalScrollView statusBarScrollView;
    private Button rightButton, leftButton, skipButton;
    private OnLastStepNextListener onLastStepNextListener;

    private int defaultTabCircleColor = Color.GRAY;
    private int defaultTabTextColor = Color.GRAY;
    private int currentTabCircleColor = Color.BLACK;
    private int currentTabTextColor = Color.BLACK;
    private int completedTabCircleColor = Color.parseColor("#8bc34a");
    private int completedTabTextColor = Color.parseColor("#8bc34a");
    private int skippedTabCircleColor = Color.parseColor("#ff9800");
    private int skippedTabTextColor = Color.parseColor("#ff9800");

    private int completedTabDrawable = R.drawable.ic_done_white_24dp;
    private int skippedTabDrawable = R.drawable.ic_skip_next_white_24dp;

    private Context mContext;
    private NavigationCallbacks navigationCallbacks;
    private StepFragment currentFragment;
    private SparseArrayCompat<Bundle> dataArray;
    private StepsAdapter stepsAdapter;
    private int currentPosition;

    private FragmentManager fragmentManager;

    public MaterialStepper(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MaterialStepper(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MaterialStepper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public OnLastStepNextListener getOnLastStepNextListener() {
        return onLastStepNextListener;
    }

    public void setOnLastStepNextListener(OnLastStepNextListener onLastStepNextListener) {
        this.onLastStepNextListener = onLastStepNextListener;
    }

    public int getDefaultTabCircleColor() {
        return defaultTabCircleColor;
    }

    public void setDefaultTabCircleColor(int defaultTabCircleColor) {
        this.defaultTabCircleColor = defaultTabCircleColor;
    }

    public int getDefaultTabTextColor() {
        return defaultTabTextColor;
    }

    public void setDefaultTabTextColor(int defaultTabTextColor) {
        this.defaultTabTextColor = defaultTabTextColor;
    }

    public int getCurrentTabCircleColor() {
        return currentTabCircleColor;
    }

    public void setCurrentTabCircleColor(int currentTabCircleColor) {
        this.currentTabCircleColor = currentTabCircleColor;
    }

    public int getCurrentTabTextColor() {
        return currentTabTextColor;
    }

    public void setCurrentTabTextColor(int currentTabTextColor) {
        this.currentTabTextColor = currentTabTextColor;
    }

    public int getCompletedTabCircleColor() {
        return completedTabCircleColor;
    }

    public void setCompletedTabCircleColor(int completeTabCircleColor) {
        this.completedTabCircleColor = completeTabCircleColor;
    }

    public int getCompletedTabTextColor() {
        return completedTabTextColor;
    }

    public void setCompletedTabTextColor(int completeTabTextColor) {
        this.completedTabTextColor = completeTabTextColor;
    }

    public int getSkippedTabCircleColor() {
        return skippedTabCircleColor;
    }

    public void setSkippedTabCircleColor(int skippedTabCircleColor) {
        this.skippedTabCircleColor = skippedTabCircleColor;
    }

    public int getSkippedTabTextColor() {
        return skippedTabTextColor;
    }

    public void setSkippedTabTextColor(int skippedTabTextColor) {
        this.skippedTabTextColor = skippedTabTextColor;
    }

    private void init() {
        dataArray = new SparseArrayCompat<>();
        onLastStepNextListener = new OnLastStepNextListener() {
            @Override
            public void onLastStepNext() {
                Toast.makeText(mContext, "Last Step Next Clicked",Toast.LENGTH_SHORT).show();
            }
        };
        View v = inflate(getContext(), R.layout.material_stepper, this);
        stepsViewpager = (MaterialStepperViewPager) v.findViewById(R.id.stepsViewpager);
        navigationBar = (CardView) v.findViewById(R.id.navigationBar);
        statusBar = (CardView) v.findViewById(R.id.statusBar);
        navigationBarViewHolder = (LinearLayout) v.findViewById(R.id.navigationBarViewHolder);
        statusBarViewHolder = (LinearLayout) v.findViewById(R.id.statusBarViewHolder);
        statusBarScrollView = (HorizontalScrollView) v.findViewById(R.id.statusBarScrollView);
        statusBarScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

//        statusBarScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                View view = statusBarViewHolder.getChildAt(currentFragment.getStepIndex());
//                int[] location = new int[2];
//                view.getLocationOnScreen(location);
//                int x = location[0];
//                int y = location[1];
//                float density  = getResources().getDisplayMetrics().density;
//                if(!(x/density >= 12)) {
//                    scrollStatusBar(currentPosition);
//                }
//            }
//        });

        rightButton = (Button) v.findViewById(R.id.rightButton);
        leftButton = (Button) v.findViewById(R.id.leftButton);
        skipButton = (Button) v.findViewById(R.id.skipButton);

        currentPosition = 0;

        stepsViewpager.setPagingEnabled(false);
        stepsViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (fragmentManager == null) {
                    Log.e(TAG,"onPageSelected():Fragment manager not set");
                } else {
                    currentFragment = stepsAdapter.getItem(currentPosition);
                    navigationCallbacks = currentFragment;
                    navigationCallbacks.initButtons(leftButton, rightButton, skipButton);
                    setCurrentTab(currentFragment);
                    currentFragment.setStatus(StepFragment.INCOMPLETE);
                    scrollStatusBar(currentFragment.getStepIndex());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setDefaultTab(StepFragment stepFragment) {
        View v = statusBarViewHolder.getChildAt(stepFragment.getStepIndex());

        (v.findViewById(R.id.tab_icon_circleimageview)).setVisibility(View.GONE);

        CircularTextView icon_textview = (CircularTextView) v.findViewById(R.id.tab_icon_circulartextview);
        icon_textview.setSolidColor(defaultTabCircleColor);

        TextView title_textview = (TextView) v.findViewById(R.id.tab_title_textview);
        title_textview.setTextColor(defaultTabTextColor);

        title_textview.setTypeface(Typeface.DEFAULT);

        setStatusBarTabIconVisibility(stepFragment.getStepIndex(),View.GONE);
    }

    private void setCurrentTab(StepFragment stepFragment) {
        View v = statusBarViewHolder.getChildAt(stepFragment.getStepIndex());

        (v.findViewById(R.id.tab_icon_circleimageview)).setVisibility(View.GONE);

        CircularTextView icon_textview = (CircularTextView) v.findViewById(R.id.tab_icon_circulartextview);
        icon_textview.setSolidColor(currentTabCircleColor);

        TextView title_textview = (TextView) v.findViewById(R.id.tab_title_textview);
        title_textview.setTextColor(currentTabTextColor);

        title_textview.setTypeface(Typeface.DEFAULT_BOLD);

        setStatusBarTabIconVisibility(stepFragment.getStepIndex(),View.GONE);
    }

    private void setSkippedTab(StepFragment stepFragment) {
        View v = statusBarViewHolder.getChildAt(stepFragment.getStepIndex());

        (v.findViewById(R.id.tab_icon_circleimageview)).setVisibility(View.GONE);

        CircularTextView icon_textview = (CircularTextView) v.findViewById(R.id.tab_icon_circulartextview);
        icon_textview.setSolidColor(skippedTabCircleColor);

        TextView title_textview = (TextView) v.findViewById(R.id.tab_title_textview);
        title_textview.setTextColor(skippedTabTextColor);

        title_textview.setTypeface(Typeface.DEFAULT);

        setStatusBarTabIconVisibility(stepFragment.getStepIndex(), View.VISIBLE);
        setStatusBarTabIconDrawable(stepFragment.getStepIndex(), skippedTabDrawable);
    }

    private void setCompletedTab(StepFragment stepFragment) {
        View v = statusBarViewHolder.getChildAt(stepFragment.getStepIndex());

        (v.findViewById(R.id.tab_icon_circleimageview)).setVisibility(View.GONE);

        CircularTextView icon_textview = (CircularTextView) v.findViewById(R.id.tab_icon_circulartextview);
        icon_textview.setSolidColor(completedTabCircleColor);

        TextView title_textview = (TextView) v.findViewById(R.id.tab_title_textview);
        title_textview.setTextColor(completedTabTextColor);

        title_textview.setTypeface(Typeface.DEFAULT);

        setStatusBarTabIconVisibility(stepFragment.getStepIndex(), View.VISIBLE);
        setStatusBarTabIconDrawable(stepFragment.getStepIndex(), completedTabDrawable);
    }

    private void scrollStatusBar(int position) {
        int length = 0;
        for (int i = 0; i < position; i++) {
            length += statusBarViewHolder.getChildAt(i).getMeasuredWidth();
        }
        statusBarScrollView.smoothScrollTo(length, 0);
    }

    private void initAdapter(){
        stepsAdapter = new StepsAdapter(fragmentManager,new ArrayList<StepFragment>());
        stepsViewpager.setAdapter(stepsAdapter);
    }

    public final FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public final void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        initAdapter();
    }

    public int onBackPressed() {
        if(stepsAdapter.getItem(currentPosition).canGoBack()) {
            leftButton.performClick();
        }
        return currentPosition;
    }

    public final void addStep(StepFragment stepFragment){
        if (fragmentManager == null) {
            Log.e(TAG,"addStep():Fragment manager not set");
        } else {
            stepFragment.setParent(this);
            stepsAdapter.addPage(stepFragment);
            populateTabs();
            if (currentFragment == null) {
                currentFragment = stepFragment;
                navigationCallbacks = stepFragment;
                navigationCallbacks.initButtons(leftButton, rightButton, skipButton);
                initNavigationButtons();
                switch (currentFragment.getStatus()) {
                    case StepFragment.COMPLETED:
                        //do nothing
                        break;
                    case StepFragment.INCOMPLETE:
                    case StepFragment.SKIPPED:
                        setCurrentTab(currentFragment);
                        break;
                    default:
                        Log.wtf(TAG,"onPageSelected():invalid status");
                }
            } else {
                switch (currentFragment.getStatus()) {
                    case StepFragment.COMPLETED:
                        //do nothing
                        break;
                    case StepFragment.INCOMPLETE:
                    case StepFragment.SKIPPED:
                        setCurrentTab(currentFragment);
                        break;
                    default:
                        Log.wtf(TAG,"onPageSelected():invalid status");
                }
            }
        }
    }

    private void initNavigationButtons(){
        if(fragmentManager == null) {
            Log.e(TAG,"initNavigationButtons():Fragment manager not set");
        } else {
            rightButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( currentFragment.getStepIndex() == (stepsAdapter.getCount()-1)) {
                        navigationCallbacks.onRightCLicked();
                        onLastStepNextListener.onLastStepNext();
                    } else {
                        navigationCallbacks.onRightCLicked();
                    }
                }
            });
            leftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationCallbacks.onLeftClicked();
                }
            });
            skipButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( currentFragment.getStepIndex() == (stepsAdapter.getCount()-1)) {
                        navigationCallbacks.onSkipClicked();
                        onLastStepNextListener.onLastStepNext();
                    } else {
                        navigationCallbacks.onSkipClicked();
                    }
                }
            });
        }
    }

    private void handleStatusOnNext(StepFragment stepFragment) {
        int status = stepFragment.getStatus();
        switch (status) {
            case StepFragment.INCOMPLETE:
                setDefaultTab(stepFragment);
                break;
            case StepFragment.SKIPPED:
                setSkippedTab(stepFragment);
                break;
            case StepFragment.COMPLETED:
                setCompletedTab(stepFragment);
                break;
            default:
                Log.e(TAG, "handleStatus():invalid status");
        }
    }

    private void populateTabs() {
        statusBarViewHolder.removeAllViews();
        for (int i = 0; i <= (stepsAdapter.getCount()+1); i++) {
            View v;
            if( i >= stepsAdapter.getCount()) {
                v = getTabItem(null);
                v.setVisibility(View.INVISIBLE);
            } else if (i == (stepsAdapter.getCount() - 1)) {
                v = getTabItem(stepsAdapter.getItem(i));
                v.findViewById(R.id.dash).setVisibility(GONE);
            } else {
                v = getTabItem(stepsAdapter.getItem(i));
            }
            statusBarViewHolder.addView(v);
        }
    }

    private View getTabItem(StepFragment stepFragment) {
        View v = inflate(mContext, R.layout.material_stepper_tab, null);
        if(stepFragment == null) {
            return v;
        }
        CircularTextView icon_textview = (CircularTextView) v.findViewById(R.id.tab_icon_circulartextview);
        icon_textview.setSolidColor(defaultTabCircleColor);
        TextView title_textview = (TextView) v.findViewById(R.id.tab_title_textview);
        icon_textview.setText(String.valueOf(stepFragment.getStepIndex() + 1));
        title_textview.setText(stepFragment.getStepTitle());
        title_textview.setTextColor(defaultTabTextColor);
        return v;
    }

    @Override
    public void sendData(Bundle bundle, int stepIndex) {
        dataArray.put(stepIndex, bundle);
    }

    @Override
    public Bundle getData(int stepIndex) {
        return dataArray.get(stepIndex);
    }

    @Override
    public void swipeToPage(StepFragment currentFragment, int index) {
        handleStatusOnNext(currentFragment);
        stepsViewpager.setCurrentItem(index);
    }

    @Override
    public void setStatusBarTabIconVisibility(int position, int visibility){
        if( visibility == View.GONE || visibility == View.INVISIBLE) {
            ((CircularTextView) statusBarViewHolder
                    .getChildAt(position)
                    .findViewById(R.id.tab_icon_circulartextview))
                    .setText((position+1)+"");
        } else {
            ((CircularTextView) statusBarViewHolder
                    .getChildAt(position)
                    .findViewById(R.id.tab_icon_circulartextview))
                    .setText(" ");
        }
        statusBarViewHolder
                .getChildAt(position)
                .findViewById(R.id.tab_icon_circleimageview)
                .setVisibility(visibility);
    }

    @Override
    public void setStatusBarTabIconDrawable(int position, int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ImageView) statusBarViewHolder
                    .getChildAt(position)
                    .findViewById(R.id.tab_icon_circleimageview))
                    .setImageDrawable(getResources().getDrawable(drawable, null));
        } else {
            ((ImageView) statusBarViewHolder
                    .getChildAt(position)
                    .findViewById(R.id.tab_icon_circleimageview))
                    .setImageDrawable(getResources().getDrawable(drawable));
        }
    }
}
