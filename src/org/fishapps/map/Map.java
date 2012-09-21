package org.fishapps.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import org.fishapps.assetManager.AssetManager;
import org.fishapps.tdp.GameSurface;
import org.fishapps.tdp.Light;
import org.fishapps.tdp.Sprite;
import org.fishapps.tdp.Viewport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class Map {
	
	private static final int tileWidth = 64, tileHeight = 64;
	
	// ZMIENNE
	Bitmap terrainMap;						// bitmapy uzywane do stworzenia mapy - bitmapa terenu
	Bitmap plantsMap;						// bitmapa roslinnosci
	Bitmap animationMap;					// bitmapa animacji
	Bitmap collisionMap;					// bitmapa kolizji
	Bitmap destructionMap;					// bitmapa zniszczalnego terenu
	Bitmap shadowMap;
    Bitmap epathMap;
	Bitmap lightMap;
    private static int width = 0;
    private static int height = 0;
	
	private Viewport viewport;				// aktualny widok, ktore miejsce mapy powinno zostac wyswietlone, a ktore nie
	
	public Vector<Sprite> spriteList,
						  plantsList,
						  collisionList,
                          epathList;// lista wszystkich uzytych spriteow - terenu
	
	private Vector<Light> lightList;
	
	private AssetManager assetManager;		// zawiera hash mape wczytanych spritow
	private GameSurface surface;
	
	// KONSTRUKTOR
	public Map( Viewport viewport, AssetManager assetManager, Bitmap terrainMap, GameSurface gameSurface ) {
		this.viewport = viewport;
		this.assetManager = assetManager;
		spriteList = new Vector<Sprite>();
		plantsList = new Vector<Sprite>();
        collisionList = new Vector<Sprite>();
        epathList = new Vector<Sprite>();
		this.terrainMap = terrainMap;
		this.surface = gameSurface;
		init();
	}
	
	// METODY
	public void setPlantsMap( Bitmap bitmap ) {
		this.plantsMap = bitmap;
		init();
	}
    public void setCollisionMap( Bitmap bitmap ) {
        this.collisionMap = bitmap;
        init();
    }
	public void setShadowMap( Bitmap bitmap ) {
		this.shadowMap = bitmap;
		init();
	}
    public void setEpathMap( Bitmap bitmap ) {
        this.epathMap = bitmap;
        init();
    }
	
	
	private void init() {
		try {
			loadMapLayer(terrainMap, surface.getResources().openRawResource(org.asd.R.raw.test_map), spriteList);
			if( plantsMap != null )
				loadMapLayer(plantsMap, surface.getResources().openRawResource(org.asd.R.raw.plants_map), plantsList);
            if( collisionMap != null)
                loadMapLayer(collisionMap, surface.getResources().openRawResource(org.asd.R.raw.collision_map), collisionList);
            if( epathMap != null)
                loadMapLayer(epathMap, surface.getResources().openRawResource(org.asd.R.raw.epath_map), epathList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// funkcja wczytujaca warstwe mapy i zapisujaca sprite'y do podanego kontenera
	public void loadMapLayer( Bitmap srcBitmap, InputStream mapLegendName, Vector<Sprite> dstLayer ) throws IOException {
		Vector<MapLegend> mLegend = new Vector<MapLegend>();
		// wczytaj legende mapy do kontenera 
		loadMapLegend( mapLegendName, mLegend );
		for( int x = 0; x< srcBitmap.getWidth(); x++ ) {
			for( int y= 0; y< srcBitmap.getHeight(); y++ ) {
				// pobierz aktualnie sprawdzany kolor 
				int cColor = srcBitmap.getPixel(x, y);
				// sprawdz kolor w legendzie mapy
				for( int i= 0; i< mLegend.size(); i++ ) {
					Log.d("res", mLegend.get(i).resource + ", " +mLegend.get(i).color);
					// jak znaleziono to stworz nowy Sprite o tej pozycji
					// i dodaj go do warstwy 
					if( cColor == mLegend.get(i).color ) {
						Sprite tmp = new Sprite(assetManager, mLegend.get(i).resource);
						tmp.setPosition(x*tileWidth, y*tileHeight);
						if( mLegend.get(i).shadowRes != 0 ) {
							Log.d("ASDSAD", "JEST CIEN");
							tmp.setShadow( surface.light, mLegend.get(i).shadowRes, viewport );
						}
						dstLayer.add(tmp);
					}
				}
			}
		}
        this.width = srcBitmap.getWidth()*tileWidth;
        this.height = srcBitmap.getHeight()*tileHeight;
	}

    public static int getWidth(){
        return width;
    }

    public static int getHeight(){
        return height;
    }

	// funkcja wczytujaca legende mapy
	// plik powinien byc zbudowany w taki sposob
	// jedna linia pliku zawiera:
	// R G B RESOURCE_ID
	private void loadMapLegend( InputStream mapLegend, Vector<MapLegend> mapLegendDST ) throws IOException {
		InputStreamReader inputReader = new InputStreamReader(mapLegend);
		BufferedReader reader = new BufferedReader(inputReader);
		String line = reader.readLine();
		while (( line = reader.readLine()) != null) {
			String [] dst = line.split(" ");
			// stworz obiekt map legend
			if( dst.length != 0  ) {
				MapLegend mLegend = null;
				if( dst.length == 4 )
					mLegend = new MapLegend( Integer.parseInt(dst[0]), Integer.parseInt(dst[1]), Integer.parseInt(dst[2]), Integer.decode(dst[3]).intValue());
				if( dst.length == 5 )
					mLegend = new MapLegend( Integer.parseInt(dst[0]), Integer.parseInt(dst[1]), Integer.parseInt(dst[2]), Integer.decode(dst[3]).intValue(), Integer.decode(dst[4]).intValue());
				mapLegendDST.add(mLegend);
			}
		}
	}
		/* KLASA POMOCNICZA W BUDOWANIU MAPY */
		class MapLegend {
			public int color;
			public int resource;
			public int shadowRes;
			public MapLegend( int r, int g, int b, int resource ) {
				this.resource = resource;
				color = Color.rgb(r, g, b);
			}
			public MapLegend( int r, int g, int b, int resource, int shadowRes ) {
				this.resource = resource;
				color = Color.rgb(r, g, b);
				this.shadowRes = shadowRes;
			}
		}
	
	public void draw(Vector<Sprite> srcList, Canvas canvas ) {
		Rect actualView = viewport.getBounds();
		for( int i = 0 ; i< srcList.size(); i++ ) {
			Sprite actualSprite = srcList.get(i);
			if( actualSprite.x >= actualView.left - tileWidth && actualSprite.x <= actualView.right ) {
				if( actualSprite.y >= actualView.top - tileHeight && actualSprite.y <= actualView.bottom ) {
					if( actualSprite.sShadow != null ) {
						Log.d("SSADASDASDS", "RYSUJ CIEN"+actualSprite.sShadow.x + ", " + actualSprite.sShadow.y);
						actualSprite.sShadow.draw(canvas);
					}
					canvas.drawBitmap( assetManager.get(srcList.get(i).getIndex()), 
										srcList.get(i).x - actualView.left, 
										srcList.get(i).y - actualView.top, 
										null );
				}
			}
		}
	}
	
	public void draw( Canvas canvas ) {
        Vector<Sprite> all = new Vector<Sprite>();
        all.addAll(spriteList);
        all.addAll(collisionList);
        all.addAll(plantsList);
        all.addAll(epathList);
		draw(all, canvas);
	}
}
