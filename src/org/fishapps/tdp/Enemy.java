package org.fishapps.tdp;

import android.content.res.Resources;
import android.graphics.*;
import android.util.Log;
import org.asd.R;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksander
 * Date: 19.09.12
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class Enemy {

        private int x, y, absx, absy, destx, desty, appos, apposFinal, happos;
        private AnimatedSprite aSprite;
        private Viewport v;
        private MovingPath mp;
        private Vector<MyPoint> ap, hap;
        private boolean isMoving = false, playerDetected = false, chasingPlayer = false, isGettingBack = false;
        private Rect er;
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
            this.isMoving = false;
            this.chasingPlayer = false;
            this.playerDetected = false;
            this.isGettingBack = false;
            this.er = new Rect(this.x - v.getX() , this.y - v.getY(), this.x - v.getX() + aSprite.getSpriteWidth(), this.y - v.getY()+aSprite.getSpriteHeight());
            this.speed = speed;
            this.speedTicker = 0;
            this.player = p;
        }

        public void update(){

            Log.d("tdp", "enemy updated");
            if(checkToPlayerDist() < 120)
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
                            move(hap.get(happos).x - aSprite.getSpriteWidth()/2, hap.get(happos).y - aSprite.getSpriteHeight() );
                            isGettingBack = true;
                        }else{
                            if(appos>-1){
                                this.x = (int) ap.get(appos).x - aSprite.getSpriteWidth()/2;
                                this.y = (int) ap.get(appos).y - aSprite.getSpriteHeight();
                                appos--;
                            }else{
                                ap = hap;
                                appos = happos;
                                this.chasingPlayer = false;
                                this.isGettingBack = false;
                            }
                        }
                    }
                }else{
                    if(!chasingPlayer){
                        hap = ap;
                        happos = appos;
                        this.chasingPlayer = true;
                    }else{
                        int playerx = (player.getBounds().left + player.getBounds().right)/2;
                        int playery = player.getBounds().bottom;
                        move(playerx, playery);
                        this.x = (int) ap.get(appos).x - aSprite.getSpriteWidth()/2;
                        this.y = (int) ap.get(appos).y - aSprite.getSpriteHeight();
                    }
                }
                absPosition();
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
                c.drawCircle(this.absx, this.absy, 120, p);
            }

            if(vr.contains(er.left, er.top) || vr.contains(er.left, er.bottom) || vr.contains(er.right, er.top) || vr.contains(er.right, er.bottom))
                aSprite.draw(c);

            c.drawText(playerDetected + " " + chasingPlayer + " " + isGettingBack, er.centerX(), er.centerY(), p);
        }

        public void absPosition(){
            this.absx = this.x - v.getX() + aSprite.getSpriteWidth()/2;
            this.absy = this.y - v.getY() + aSprite.getSpriteHeight();
        }

        public void move(int destx, int desty){
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
