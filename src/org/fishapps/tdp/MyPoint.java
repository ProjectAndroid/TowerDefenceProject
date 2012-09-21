package org.fishapps.tdp;

import android.graphics.Point;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksander
 * Date: 16.09.12
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class MyPoint extends Point{

    public boolean wasVisited = false, isBlock = false, isEnemyPath = false;
    public int x, y;
    int index;
    double ag, g, h;
    MyPoint parent = null;

    public MyPoint(){
        super();
        this.g = 1;
    }

    public MyPoint(int x, int y, int index, int g){
        this.x = x;
        this.y = y;
        this.index = index;
        this.g = g;
    }

    public void setVisited(){
        wasVisited = true;
    }

    public void eraseVisited(){
        wasVisited = false;
    }

    public  boolean checkVisited(){
        return this.wasVisited;
    }

    public void setG(double g){
        this.g = g;
    }

    public void setAG(double g){
        this.ag = g;
    }

    public void setH(double h){
        this.h = h;
    }

    public double getG(){
        return this.g;
    }

    public double getAG(){
        return this.ag;
    }

    public double getH(){
        return this.h;
    }

    public double getF(){
        return this.g + this.h;
    }

    public void setParent(MyPoint parent){
        this.parent = parent;
    }

    public MyPoint getParent(){
        return this.parent;
    }

    public void setBlock(boolean b){
        this.isBlock = b;
    }
}

