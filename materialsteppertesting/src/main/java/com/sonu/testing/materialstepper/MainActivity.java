package com.sonu.testing.materialstepper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sonu.libraries.materialstepper.MaterialStepper;

public class MainActivity extends AppCompatActivity {

    MaterialStepper materialStepper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialStepper.setFragmentManager(getSupportFragmentManager());
        materialStepper.addStep(new SampleFragment());
        materialStepper.addStep(new SampleFragment());
        materialStepper.addStep(new SampleFragment());
    }

    @Override
    public void onBackPressed() {
        if( materialStepper.onBackPressed() == 0) {
            super.onBackPressed();
        }
    }
}
