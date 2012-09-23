package org.fishapps.tdp;

import android.content.res.Resources;
import android.graphics.*;
import android.util.Log;
import org.asd.R;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksander
 * Date: 17.09.12
 * Time: 00:14
 * To change this template use File | Settings | File Templates.
 */

public class Player extends Thread{
    private int x, y, absx, absy, destx, desty, appos;
    private long  speed, speedTicker;
    private AnimatedSprite aSprite;
    private Viewport v;
    private MovingPath mp;
    private Vector<MyPoint> ap;
    private Rect pr;
    private boolean isActive = true, isMoving = false;

    public Player(int x, int y, Resources r, Viewport v){
        this.x = x;
        this.y = y;
        this.aSprite = new AnimatedSprite(
                BitmapFactory.decodeResource(r, R.drawable.walk_elaine),
                10,
                50,
                30,
                47,
                50,
                5
        );
        this.v = v;
        this.mp = new MovingPath(v, this);
        this.appos = -1;
        this.pr = new Rect(this.x - v.getX() , this.y - v.getY(), this.x - v.getX() + aSprite.getSpriteWidth(), this.y - v.getY()+aSprite.getSpriteHeight());
        this.speed = 10;
    }

    @Override
    public void run(){
        while(isAlive())
            update();
    }

    public void update(){
        absPosition();

        if(isMoving){
            Log.d("tdp", "update player");
            move();
            isMoving = false;
        }
        if(System.currentTimeMillis() > speedTicker + speed){
            speedTicker = System.currentTimeMillis();
            if(appos>-1){
                Log.d("tdp", "move player");
                this.x = (int) ap.get(appos).x - aSprite.getSpriteWidth()/2;
                this.y = (int) ap.get(appos).y - aSprite.getSpriteHeight();
                appos--;
            }

            aSprite.update(System.currentTimeMillis());
            aSprite.setX(this.absx - aSprite.getSpriteWidth() / 2);
            aSprite.setY(this.absy - aSprite.getSpriteHeight());
            pr.set(aSprite.getX(), aSprite.getY(), aSprite.getX() + aSprite.getSpriteWidth(), aSprite.getY()+aSprite.getSpriteHeight());
        }

    }
    
    public void draw(Canvas c){
        Rect vr = new Rect(0,0,v.vWidth, v.vHeight);
        Paint p = new Paint();
        if(vr.contains(pr.left, pr.top) || vr.contains(pr.left, pr.bottom) || vr.contains(pr.right, pr.top) || vr.contains(pr.right, pr.bottom))
            aSprite.draw(c);
        if(this.isActive){
            p.setColor(Color.argb(100, 0,0, 255));
            c.drawRect(pr.left+13, pr.top-20, pr.right-13, pr.top-5, p);
        }else{
            p.setColor(Color.argb(100, 255,0, 0));
            c.drawRect(pr.left+13, pr.top-20, pr.right-13, pr.top-5, p);
        }

        p.setColor(Color.rgb(0,255,255));
        c.drawCircle(getX(), getY(), 8, p);
        p.setColor(Color.rgb(0, 0, 255));
        c.drawCircle(destx, desty, 8, p);
    }

    private void absPosition(){
        this.absx = this.x - v.getX() + aSprite.getSpriteWidth()/2;
        this.absy = this.y - v.getY() + aSprite.getSpriteHeight();
    }

    public int getX(){
        return this.x + aSprite.getSpriteWidth()/2;
    }

    public int getY(){
        return this.y + aSprite.getSpriteHeight();
    }

    public void move(){
        Log.d("tdp", "player move");
        ap = mp.getPath();
        if(ap != null)
            appos = ap.size()-1;
    }

    public void setMove(float movx, float movy){
        if(!this.isMoving){
            absPosition();
            this.destx = (int) movx + v.getX();
            this.desty = (int) movy + v.getY();
            mp.UpdatePosition(this.absx, this.absy);
            mp.UpdateDestPosition(this.destx, this.desty);
            this.isMoving = true;
        }
    }

    public Rect getBounds(){
        return pr;
    }

    public boolean checkActive(){
        return this.isActive;
    }

    public void setActive(boolean active){
        this.isActive = active;
    }

}
