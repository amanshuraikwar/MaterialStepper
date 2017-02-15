# MaterialStepper
MaterialStepper is a custom Android View Library inspired by:
* https://material.io/guidelines/components/steppers.html
* https://github.com/fcannizzaro/material-stepper
* https://github.com/stepstone-tech/android-material-stepper

## ScreenShots
<img src="https://github.com/amanshuraikwar/MaterialStepper/blob/master/screenshots/screenshot1.png" height="500">
<img src="https://github.com/amanshuraikwar/MaterialStepper/blob/master/screenshots/screenshot2.png" height="500">
<img src="https://github.com/amanshuraikwar/MaterialStepper/blob/master/screenshots/screenshot3.png" height="500">
## How to use?
Clone/Download and include in your Android Project and use it or do whatever the hell you want to with it.

### Steps
* Add the view to your layout xml file
```xml
<libraries.sonu.com.materialstepper.MaterialStepper
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/materialStepper"/>
```
* Create fragment classes and extend from StepFragment class and Override some methods
```java
import libraries.sonu.com.materialstepper.StepFragment;

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
```
* Initialise MaterialStepper object in your Acivity/ Fragment
* Add FragmentManager to the MaterialStepper object
* Add steps to the MaterialStepper
* Don't forget to call MaterialStepper's onBackPressed() in onBackPressed() of your parent Activity
```java
import libraries.sonu.com.materialstepper.MaterialStepper;

public class MainActivity extends AppCompatActivity {

    MaterialStepper materialStepper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialStepper = (MaterialStepper) findViewById(R.id.materialStepper);
        materialStepper.setFragmentManager(getSupportFragmentManager());
        materialStepper.addStep(new SampleFragment());
        materialStepper.addStep(new SampleFragment());
        materialStepper.addStep(new SampleFragment());
    }

    @Override
    public void onBackPressed() {
        materialStepper.onBackPressed();
    }
}
```
* You are ready to go. Happy coding...

### Detailed documentation, Gradle Dependency, Maven Dependency, More Customization and Functionality, Coming soon...
