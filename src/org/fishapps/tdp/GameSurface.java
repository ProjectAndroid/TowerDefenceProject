package org.fishapps.tdp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.asd.R;
import org.fishapps.assetManager.AssetManager;
import org.fishapps.map.Map;

import java.util.Vector;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
	
	// ZMIENNE
	public GameThread gameThread;
	private Viewport viewport;
	private AssetManager assetManager;
	private Map firstMap;
	
	// TEST CIENI
	public Light light;
	public Sprite shadowTest, shadowTest1;
    public Player player;
    public Enemy enemy;
    public Vector<Enemy> enemies;
    public MovingPath mp;
	Shadow shadow, shadow1;
    private int touchCounter = 0;
	
	// TOUCH EVENT - PREVIOUS
	float px = 0, py = 0;	// previous touch
	float ax, ay;	// actual touch
    int touchedTileX = 0, touchedTileY = 0;
	
	public GameSurface(Context context) {
		super(context);

		viewport = new Viewport( 0, 0, this);
		assetManager = new AssetManager(this);


		
		gameThread = new GameThread(this);
		gameThread.setRunning(true);
		gameThread.start();
		
		light = new Light(viewport);
		light.x = 200;
		light.y = 50;
		light.radius = 300;
		
		firstMap = new Map( viewport, assetManager, BitmapFactory.decodeResource(getResources(), R.drawable.test_map ), this);
		firstMap.setPlantsMap(BitmapFactory.decodeResource(getResources(), R.drawable.plants_map));
        firstMap.setCollisionMap(BitmapFactory.decodeResource(getResources(), R.drawable.collision));
        firstMap.setEpathMap(BitmapFactory.decodeResource(getResources(), R.drawable.epath_map));
		
		
		//firstMap.setShadowMap(BitmapFactory.decodeResource(getResources(), R.drawable.shadow_map ));
		// inicjalizacja animacji

		// TODO Auto-generated constructor stub
        MovingPath.init(firstMap.collisionList, firstMap.epathList);
		player = new Player(0, 0, getResources(), viewport);
        enemies = new Vector<Enemy>();
        for(int i = 0; i < 1; i++)
            enemies.add(new Enemy(2435, 1218,player, getResources(), viewport, 30));


	}
	
	// METODY 
	
	// funkcja updatujaca wsxzystko co zostanie wyswietlone
	public void update() {
        if(!viewport.isAlive())
            viewport.start();

        if(!player.isAlive())
            player.start();

        for(Enemy enemy:enemies)
            if(!enemy.isAlive()){
                enemy.start();
                Log.d("tdp", "enemy start") ;
            }
    }

	// rysowanie wszystkiego na ekranie
    @Override
	public void draw( Canvas c ) {
		c.drawColor(Color.BLACK);
		firstMap.draw(c);
        player.draw(c);
        light.draw(c);
        for(Enemy enemy:enemies)
            enemy.draw(c);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	@Override 
	public boolean onTouchEvent( MotionEvent event ) {
		if( event.getAction() == MotionEvent.ACTION_DOWN ) {
            ax = event.getX();
            ay = event.getY();
            if(player.checkActive()){
                if(player.getBounds().contains((int)event.getX(), (int)event.getY()))
                    player.setActive(false);
                else{
                    player.setMove(ax, ay);
                }
            }else if(player.getBounds().contains((int)event.getX(), (int)event.getY()))
                    player.setActive(true);
		}
		if( event.getAction() == MotionEvent.ACTION_MOVE ) {
            Log.d("tdp", "move");
			px = ax;
			py = ay;
			ax = event.getX();
			ay = event.getY();
            if(viewport.x + px-ax > 0 && viewport.y + py-ay > 0 && viewport.x + viewport.vWidth + px-ax < Map.getWidth() && viewport.y + viewport.vHeight + py-ay < Map.getHeight()){
                viewport.moveViewport(px-ax, py-ay);
            }else{
                if(viewport.x + px-ax > 0 && viewport.x + viewport.vWidth + px-ax < Map.getWidth())
                    viewport.moveViewport(px-ax, 0);
                if(viewport.y + py-ay > 0 && viewport.y + viewport.vHeight + py-ay < Map.getHeight())
                    viewport.moveViewport(0, py-ay);
            }
		}
        light.x = (int) event.getX() + viewport.x;
        light.y = (int) event.getY() + viewport.y;

		return true;
	}
	
}
