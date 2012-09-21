package org.fishapps.tdp;

import org.fishapps.assetManager.AssetManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {
	
	// ZMIENNE 
	private int sIndex;			// numer obrazka w tablicy obrazkow
	public int x, y;			// pozycja obrazka na mapie
	
	private int shadowBitmapIndex;
	public Shadow sShadow;
	
	private AssetManager assets;
	
	public Sprite( AssetManager assets, int sIndex ) {
		this.assets = assets;
		this.sIndex = sIndex;		
	}
	public Sprite( AssetManager assets, int sIndex, int x, int y ) {
		
		this.assets = assets;
		this.sIndex = sIndex;		
		this.x = x;
		this.y = y;
	}
	
	public void draw( Canvas canvas ) {
		sShadow.x = x;
		sShadow.y = y;
		if( sShadow != null )
			sShadow.draw(canvas);
	}
	public void setPosition( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	public int getIndex() {
		return sIndex;
	}
	public Bitmap getBitmap() {
		return assets.get(sIndex);
	}
	public int getBitmapWidth() {
		return assets.get(sIndex).getWidth();
	}
	public int getBitmapHeight() {
		return assets.get(sIndex).getHeight();
	}
	public void setShadow(Light srcLight, int shadowIndex, Viewport v) {
		Sprite shadowSprite = new Sprite( assets, shadowIndex, x, y );
		sShadow = new Shadow( shadowSprite , srcLight, v );
	}
}
