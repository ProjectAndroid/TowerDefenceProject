package org.fishapps.tdp;

import android.content.res.Resources;
import android.graphics.*;
import org.asd.R;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksander
 * Date: 19.09.12
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class Enemy extends Thread{

        private int x, y, absx, absy, destx, desty, appos, apposFinal, happos, happosFinal;
        private AnimatedSprite aSprite;
        private Viewport v;
        private MovingPath mp;
        private Vector<MyPoint> ap, hap;
        public boolean isMoving = false;
        public boolean playerDetected = false, chasingPlayer = false, isGettingBack = false;
        public Rect er;
        private long speed, speedTicker;
        private Player player;

        public Enemy(int x, int y, Player p, Resources r, Viewport v, long speed){
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
            this.happos = -1;
            this.chasingPlayer = false;
            this.isMoving = false;
            this.playerDetected = false;
            this.isGettingBack = false;
            this.er = new Rect(this.x - v.getX() , this.y - v.getY(), this.x - v.getX() + aSprite.getSpriteWidth(), this.y - v.getY()+aSprite.getSpriteHeight());
            this.speed = speed;
            this.speedTicker = 0;
            this.player = p;
        }

    @Override
    public void run(){
        while(isAlive()){
            if(!isMoving)
                move(151, 129);
            update();
        }
    }

        public void update(){

            absPosition();
            if(checkToPlayerDist() < 200)
                this.playerDetected = true;
            else
                this.playerDetected = false;

            if(System.currentTimeMillis() > speedTicker + speed){

                if(!this.playerDetected){
                    if(!this.chasingPlayer){
                        if(appos>-1){

                            this.x = (int) ap.get(appos).x - aSprite.getSpriteWidth()/2;
                            this.y = (int) ap.get(appos).y - aSprite.getSpriteHeight();
                            appos--;
                        }else if(isMoving){

                            appos = apposFinal;
                            this.x = 2435;
                            this.y = 1218;
                        }
                    }else{
                        if(!isGettingBack){
                            move(hap.get(happos).x, hap.get(happos).y);
                            isGettingBack = true;
                        }else{
                            if(appos>-1){
                                this.x = (int) ap.get(appos).x - aSprite.getSpriteWidth()/2;
                                this.y = (int) ap.get(appos).y - aSprite.getSpriteHeight();
                                appos--;
                            }else{
                                ap = hap;
                                appos = happos;
                                apposFinal = happosFinal;
                                this.chasingPlayer = false;
                                this.isGettingBack = false;
                            }
                        }
                    }
                }else{
                    if(!chasingPlayer){
                        if(!isGettingBack){
                            hap = (Vector<MyPoint>) ap.clone();
                            happos = appos;
                            happosFinal = apposFinal;
                        }
                        this.chasingPlayer = true;
                    }else if(!player.getBounds().intersect(er)){
                            move(player.getX(), player.getY());
                            if(appos > 0){
                                this.x = (int) ap.get(appos).x - aSprite.getSpriteWidth()/2;
                                this.y = (int) ap.get(appos).y - aSprite.getSpriteHeight();
                            }
                    }
                }
                aSprite.update(System.currentTimeMillis());
                aSprite.setX(this.absx - aSprite.getSpriteWidth()/2);
                aSprite.setY(this.absy - aSprite.getSpriteHeight());
                er.set(aSprite.getX(), aSprite.getY(), aSprite.getX() + aSprite.getSpriteWidth(), aSprite.getY()+aSprite.getSpriteHeight());
                speedTicker = System.currentTimeMillis();
            }

        }

        public void draw(Canvas c){
            Rect vr = new Rect(0,0,v.vWidth, v.vHeight);
            Paint p = new Paint();
            if(this.playerDetected){
                p.setColor(Color.argb(40, 255, 0, 0));
                p.setStyle(Paint.Style.FILL_AND_STROKE);
                c.drawCircle(this.absx, this.absy, 200, p);
            }

            if(happos != -1)
                c.drawCircle(hap.get(happos).x, hap.get(happos).y, 8, p);

            if(appos != -1)
                c.drawCircle(ap.get(appos).x, ap.get(appos).y, 8, new Paint());

            if(vr.contains(er.left, er.top) || vr.contains(er.left, er.bottom) || vr.contains(er.right, er.top) || vr.contains(er.right, er.bottom))
                aSprite.draw(c);


            p.setColor(Color.rgb(255,0,255));
            c.drawCircle(x, y, 8, p);
            p.setColor(Color.rgb(255, 0,0));
            c.drawCircle(destx, desty, 8, p);
        }

        public void absPosition(){
            this.absx = this.x - v.getX() + aSprite.getSpriteWidth()/2;
            this.absy = this.y - v.getY() + aSprite.getSpriteHeight();
        }

        public void move(int destx, int desty){
            absPosition();
            this.destx = destx;
            this.desty = desty;
            mp.UpdatePosition(this.absx, this.absy);
            mp.UpdateDestPosition(this.destx, this.desty);
            ap = mp.getPath();
            if(ap != null){
                apposFinal = ap.size()-1;
                appos = ap.size()-1;
                this.isMoving = true;
            }
        }

        private double checkToPlayerDist(){
            double dist = Math.sqrt((er.centerX() - player.getBounds().centerX())*(er.centerX() - player.getBounds().centerX()) + (er.centerY() - player.getBounds().centerY())*(er.centerY() - player.getBounds().centerY()));
             return dist;
        }
}
