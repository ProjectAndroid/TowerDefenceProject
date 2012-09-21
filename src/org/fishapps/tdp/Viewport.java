package org.fishapps.tdp;

import android.graphics.Rect;

public class Viewport {
		
	int x, y;	 			// pozycja aktualnego widoku
	Rect currentView;		// prostokat widoku
	GameSurface surface;
	int vWidth= 0, vHeight= 0;	// wielkosc viewportu
	
	// Constructor 
	public Viewport( int x, int y, GameSurface surface ) {
		this.x = x;
		this.y = y;
		this.surface = surface;
	}
	public Rect getBounds() {
		vWidth = surface.gameThread.canvas_width;
		vHeight = surface.gameThread.canvas_height;
		return new Rect(x, y, x+vWidth, y+vHeight);
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	// przesun viewport
	public void moveViewport( float x, float y ) {
		this.x+=x;
		this.y+=y;
	}
}
