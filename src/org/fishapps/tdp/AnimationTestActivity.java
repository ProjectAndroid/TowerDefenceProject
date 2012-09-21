package org.fishapps.tdp;

import android.app.Activity;
import android.os.Bundle;

public class AnimationTestActivity extends Activity {
	
	// ZMIENNE 
	private GameSurface gSurface;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gSurface = new GameSurface(this);
        setContentView(gSurface);
    }
}