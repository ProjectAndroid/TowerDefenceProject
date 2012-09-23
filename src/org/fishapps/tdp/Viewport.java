package org.fishapps.tdp;

import android.graphics.Rect;

public class Viewport extends Thread{
		
	int x, y, destx, desty;	 			// pozycja aktualnego widoku
	Rect currentView;		// prostokat widoku
	GameSurface surface;
	int vWidth= 0, vHeight= 0;	// wielkosc viewportu
    boolean isMoved = false;
	
	// Constructor 
	public Viewport( int x, int y, GameSurface surface ) {
		this.x = x;
		this.y = y;
		this.surface = surface;
        this.isMoved = false;

	}

    @Override
    public void run(){
        while(isAlive())
            update();
    }

    private void update(){
        if(this.isMoved) {
            this.x=destx;
            this.y=desty;
            this.isMoved = false;
        }
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
        this.destx = this.x;
        this.desty = this.y;
        this.destx+=x;
        this.desty+=y;
        this.isMoved = true;
	}
}
