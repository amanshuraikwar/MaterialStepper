package libraries.sonu.com.materialstepper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sonu on 13/2/17.
 */

public class SampleFragment extends StepFragment {

    public SampleFragment(){
        //do nothing
    }

    @Override
    public String getStepTitle() {
        return "Title";
    }

    @Override
    public boolean canGoBack() {
        return true;
    }

    @Override
    public boolean canSkip() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sample, container, false);
        return v;
    }
}
