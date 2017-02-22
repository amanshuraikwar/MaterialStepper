package com.sonu.libraries.materialstepper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

//    MaterialStepper materialStepper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        materialStepper = (MaterialStepper) findViewById(R.id.materialStepper);
//        materialStepper.setFragmentManager(getSupportFragmentManager());
//        materialStepper.addStep(new SampleFragment());
//        materialStepper.addStep(new SampleFragment());
//        materialStepper.addStep(new SampleFragment());
//
//        materialStepper.setOnLastStepNextListener(new OnLastStepNextListener() {
//            @Override
//            public void onLastStepNext() {
//                finish();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
//        if( materialStepper.onBackPressed() == 0) {
//            super.onBackPressed();
//        }
    }
}
