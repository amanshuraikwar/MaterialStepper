package com.sonu.libraries.materialstepper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sonu on 13/2/17.
 */

public class SampleFragment extends Fragment {

    public SampleFragment(){
        //do nothing
    }

//    @Override
//    public String getStepTitle() {
//        return "Step 1";
//    }
//
//    @Override
//    public boolean canGoBack() {
//        return true;
//    }
//
//    @Override
//    public boolean canSkip() {
//        return true;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sample, container, false);
        return v;
    }
}
