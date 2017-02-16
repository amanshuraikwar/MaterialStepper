package com.sonu.libraries.materialstepper;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by sonu on 11/2/17.
 */

public class StepsAdapter extends FragmentPagerAdapter {

    private ArrayList<StepFragment> fragmentList;

    public StepsAdapter(FragmentManager fragmentManager, ArrayList<StepFragment> fragmentList) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public StepFragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public String getPageTitle(int position) {
        return fragmentList.get(position).getStepTitle();
    }

    public void addPage(StepFragment fragment) {
        fragment.setStepIndex(getCount());
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }
}
