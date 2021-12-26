# MaterialStepper (No Longer Maintained)
MaterialStepper is a custom Android View Library inspired by:
* https://material.io/guidelines/components/steppers.html
* https://github.com/fcannizzaro/material-stepper
* https://github.com/stepstone-tech/android-material-stepper

### To the source(code)
* https://github.com/amanshuraikwar/MaterialStepper/tree/master/material-stepper

### To the example app
* https://github.com/amanshuraikwar/MaterialStepper/tree/master/materialstepperexample

### How is this library different from others?
* This provides a View instead of Activity classes to extend from, making it simpler to use.
* You can customize the functionality of **Next**, **Back** and **Skip** buttons and decide manually when to go to next/ previous step.
* You can visually customize **Next**, **Back** and **Skip** buttons for each step individually. 
* You can customize the TextColor, CircleColor, IconDrawable of the **StepTab** in top bar (StatusBar) 

## ScreenShots
<img src="https://github.com/amanshuraikwar/MaterialStepper/blob/master/screenshots/screenshot1.png" height="500">
<img src="https://github.com/amanshuraikwar/MaterialStepper/blob/master/screenshots/screenshot2.png" height="500">
<img src="https://github.com/amanshuraikwar/MaterialStepper/blob/master/screenshots/screenshot3.png" height="500">

## How to use?

### gradle
Add this to your (Module:app)'s build.gradle file
```gradle
dependencies {
        compile 'com.sonu.libraries.materialstepper:material-stepper:0.0.4'
}
```

### Steps
* Add the view to your layout xml file
```xml
<com.sonu.libraries.materialstepper.MaterialStepper
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/materialStepper"/>
```
* MaterialStepper requires the Fragment instances that are added as steps should be inherited from class **StepFragment** provided by this library
* Create fragment classes and extend from StepFragment class and Override some methods
```java
import com.sonu.libraries.materialstepper.StepFragment;
/*
 * N = 1, 2, 3...
 */
public class SampleFragmentN extends StepFragment {

    public SampleFragmentN(){
        //do anything you wan't in the constructor
    }

    /*
     * return the title of the step in this method
     * this title will be shown in the Tab of your step (StepTab) in the Top Bar (StatusBar) 
     */
    @Override
    public String getStepTitle() {
        return "Title of step";
    }

    /*
     * this method decides if the user can go back from this step or not
     * this method toggles the visiblity of back button in the bottom bar (NavigationBar)
     * return true for VISIBLE and false for GONE
     */
    @Override
    public boolean canGoBack() {
        return true;
    }

    /*
     * this method decides if the user can skip this step or not
     * this method toggles the visiblity of skip button in the bottom bar (NavigationBar)
     * return true for VISIBLE and false for GONE
     */
    @Override
    public boolean canSkip() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sample, container, false);
        return v;
    }
}
```
* Initialise MaterialStepper object in your Acivity/ Fragment
* Add FragmentManager to the MaterialStepper object
* Add steps to the MaterialStepper
* Don't forget to call MaterialStepper's onBackPressed() in onBackPressed() of your parent Activity
```java
import com.sonu.libraries.materialstepper.MaterialStepper;
import com.sonu.libraries.materialstepper.OnLastStepNextListener;

public class MainActivity extends AppCompatActivity {

    MaterialStepper materialStepper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialStepper = (MaterialStepper) findViewById(R.id.materialStepper);
        
        //adding fragment manager for ViewPager Adapter
        materialStepper.setFragmentManager(getSupportFragmentManager());
        
        //adding steps
        materialStepper.addStep(new SampleFragment1());
        materialStepper.addStep(new SampleFragment2());
        materialStepper.addStep(new SampleFragment3());
        
        //adding functionality when NEXT button is clicked on last step
        materialStepper.setOnLastStepNextListener(new OnLastStepNextListener() {
            @Override
            public void onLastStepNext() {
                //some action
            }
        });
    }

    @Override
    public void onBackPressed() {
        //calling onBackPressed for handling back presses in MaterialStepper View
        //onBackPressed() returns step index of currently displayed step
        //if the return value is 0 then call super for default functionality
        if( materialStepper.onBackPressed() == 0) {
            super.onBackPressed();
        }
    }
}
```
* You are ready to go. Happy coding...

# Dive Deep into Customizations
Read further if you need to use the library to its full potential

## Customizing StepFragment

### Status of a step
* **status** is a instance variable of class **StepFragment** which is responsible for the icon displayed in the StepTab of that step in the StatusBar when the user moves to another step by pressing **skip** or **next** buttons from the step
* **status** should only be set to 3 static integers defined in class StepFragment
```java
    public static final int SKIPPED = 2;
    public static final int INCOMPLETE = 0;
    public static final int COMPLETED = 1;
```
* While overriding the onRightCLicked(), onLeftCLicked(), onSkipCLicked() remember to call super for these methods to set the status appropriately or else set the variable manually and logically
* Definition of methods onRightCLicked(), onLeftCLicked(), onSkipCLicked() in the class StepFragment are given below just to give you an idea about functionality
```java
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
```
* Always set the **status** before calling **swipeToPage()** because the StepTab is updated in that method
* Implementation of **swipeToPage()** is given below to give you an idea about functionality
```java
    @Override
    public void swipeToPage(StepFragment currentFragment, int index) {
        handleStatusOnNext(currentFragment);
        stepsViewpager.setCurrentItem(index);
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
```
### Customizing OnClickListeners of **Next**, **Back** and **Skip** buttons
* When Next button is clicked, onRightClicked() is called
* When Back button is clicked, onLeftClicked() is called
* When Skip button is clicked, onSkipClicked() is called
* Methods onRightCLicked(), onLeftCLicked(), onSkipCLicked() are implemented in StepFragment class from interface **NavigationCallbacks**
* To customize their functionality override these methods and call super when your custom task is over or set the **status** instance variable appropirately and logically
* A code sample is shown below
```java
    @Override
    public StepFragment onRightCLicked() {
        customNextClick();
        return this;
    }

    @Override
    public StepFragment onLeftClicked() {
        customBackClick();
        return this;
    }

    @Override
    public StepFragment onSkipClicked() {
        customSkipClick();
        return this;
    }
    
    private void customNextClick(){
        //do your custom stuff
        //call super
        super.onRightCLicked();
    }

    private void customBackClick(){
        //do your custom stuff
        //call super
        super.onLeftClicked();
    }

    private void customSkipClick(){
        //do your custom stuff
        //call super
        super.onSkipClicked();
    }
```
