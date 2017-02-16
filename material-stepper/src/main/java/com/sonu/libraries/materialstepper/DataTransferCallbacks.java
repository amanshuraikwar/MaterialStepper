package com.sonu.libraries.materialstepper;

import android.os.Bundle;

/**
 * Created by sonu on 11/2/17.
 */

public interface DataTransferCallbacks {
    void sendData(Bundle bundle, int stepIndex);
    Bundle getData(int stepIndex);
}
