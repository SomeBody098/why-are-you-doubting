package com.run.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.run.game.Main;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        configuration.useWakelock = true;
        configuration.numSamples = 0;
        setColorAndDepth(configuration);
        initialize(new Main(), configuration);
    }

    private void setColorAndDepth(AndroidApplicationConfiguration configuration){
        configuration.r = 8;
        configuration.g = 8;
        configuration.b = 8;
        configuration.a = 8;
        configuration.depth = 16;
    }
}
