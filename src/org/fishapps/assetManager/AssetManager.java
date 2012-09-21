package org.fishapps.assetManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.asd.R;
import org.fishapps.tdp.GameSurface;

import java.util.HashMap;

public class AssetManager {
	
	// ZMIENNE
	private HashMap<Integer, Bitmap> assetResource;
	private GameSurface surface;
	
	// KONSTRUKTOR
	public AssetManager( GameSurface gameSurface ) {
		assetResource = new HashMap<Integer, Bitmap>();
		surface = gameSurface;
		init();
	}
	// METODY 
	public void put( int index, Bitmap resource ) {
		assetResource.put( index, resource );
	}
	public Bitmap get( int index ) {
		return assetResource.get(index);
	}
	// funkcja inicjujaca grafike i wszystko 
	private void init() {
		// grass
		this.put(R.drawable.grass, BitmapFactory.decodeResource(surface.getResources(), R.drawable.grass));
		this.put(R.drawable.shadow_test, BitmapFactory.decodeResource(surface.getResources(), R.drawable.shadow_test));
		this.put(R.drawable.tre, BitmapFactory.decodeResource(surface.getResources(), R.drawable.tre));
		this.put(R.drawable.tre_shadow, BitmapFactory.decodeResource(surface.getResources(), R.drawable.tre_shadow));
        this.put(R.drawable.rock, BitmapFactory.decodeResource(surface.getResources(), R.drawable.rock));
        this.put(R.drawable.path, BitmapFactory.decodeResource(surface.getResources(), R.drawable.path));
	}
	public Resources getResources() {
		return surface.getResources();
	}
	
}
