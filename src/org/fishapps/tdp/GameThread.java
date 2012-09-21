package org.fishapps.tdp;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private String TAG = "GameThread";
	// desired fps
	private final static int 	MAX_FPS = 50;
	// maximum number of frames to be skipped
	private final static int	MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int	FRAME_PERIOD = 1000 / MAX_FPS;	
	// status
	private boolean running;
	// surface holder
	private SurfaceHolder surfaceHolder;
	// game surface
	private GameSurface gameSurface;
	// used canvas 
	public Canvas canvas;
	
	public int canvas_width= 0, canvas_height= 0;
	
	/*
	 * CONSTRUCTOR
	 */
	public GameThread(GameSurface surface) {
		surfaceHolder = surface.getHolder();
		gameSurface = surface;
	}
	
	// METHODS
	
	/*
	 * Set runnning method
	 */
	public void setRunning(boolean status) {
		running = status;
	}
	/*
	 * Start main thread
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		Log.d(TAG, "Starting game loop");

		long beginTime;		// the time when the cycle begun
		long timeDiff;		// the time it took for the cycle to execute
		int sleepTime;		// ms to sleep (<0 if we're behind)
		int framesSkipped;	// number of frames being skipped 

		sleepTime = 0;

		while (running) {
			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = surfaceHolder.lockCanvas();
				if( canvas != null ) {
					canvas_width = canvas.getWidth();
					canvas_height = canvas.getHeight();
				}
				synchronized (surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;	// resetting the frames skipped
					// update game state


					if( canvas != null ) {
						gameSurface.draw(canvas);
					}

					// calculate how long did the cycle take
					timeDiff = System.currentTimeMillis() - beginTime;
					// calculate sleep time
					sleepTime = (int)(FRAME_PERIOD - timeDiff);

					if (sleepTime > 0) {
						// if sleepTime > 0 we're OK
						try {
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {}
					}

					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						// we need to catch up
						// update without rendering
						gameSurface.update();
						
						// add frame period to check if in next frame
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}

}
