package org.fishapps.tdp;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.fishapps.map.Map;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksander
 * Date: 17.09.12
 * Time: 02:13
 * To change this template use File | Settings | File Templates.
 */
public class MovingPath {

    public static Vector<MyPoint> points = new Vector<MyPoint>();
    public Vector<MyPoint> actPath = new Vector<MyPoint>();
    Viewport v;
    Player player;
    Enemy enemy;
    boolean sf = false, ef =false, hasPath = false, isPlayer = false;

    private MyPoint pointSF = new MyPoint(), pointEF = new MyPoint(), pointS = new MyPoint(), pointE = new MyPoint();
    private static int width;
    private static int height;

    public MovingPath(Viewport v, Player player){
        this.v = v;
        this.player = player;
        this.isPlayer = true;
    }

    public MovingPath(Viewport v, Enemy enemy){
        this.v = v;
        this.enemy = enemy;
        this.isPlayer = false;
    }

    public static void init(Vector<Sprite> cl, Vector<Sprite>epl){
        int index = 0, cx = 0, cy = 0;
        MyPoint actPoint;
        Random r = new Random();
        double mw2 = Map.getWidth()*Map.getWidth(), mh2 = Map.getHeight()*Map.getHeight();
        for(int y = 8; y < Map.getHeight(); y+=16)
            for(int x = 8; x < Map.getWidth(); x+=16){
                points.add(new MyPoint(x, y, index, 1));
                actPoint = points.get(index);
                cx = (int) (Math.floor(x / 64)*64);
                cy = (int) (Math.floor(y / 64)*64);
                for(Sprite cs:cl)
                    if(cs.x == cx && cs.y == cy) {
                        actPoint.setBlock(true);
                        break;
                    }
                for(Sprite ep:epl)
                    if(ep.x == cx && ep.y == cy){
                        actPoint.isEnemyPath = true;
                        break;
                    }
                index++;
            }

        width = Map.getWidth()/16;
        height = Map.getHeight()/16;
    }


    //pointEF - koncowy wierzcholek//
    public int FindPath(){

        MyPoint act = null, tmp;

        PriorityQueue<MyPoint> opened = new PriorityQueue<MyPoint>(width*height, new Comparator<MyPoint>() {
            @Override
            public int compare(MyPoint myPoint, MyPoint myPoint1) {
                return (int)(myPoint.getF() - myPoint1.getF());
            }
        });
        Vector<MyPoint> closed = new Vector<MyPoint>();
        opened.add(pointSF);
        pointSF.setVisited();

        //obliczanie wartosci h(x)
        for(MyPoint point:points){
            point.h = (int) (Math.abs(point.y - pointEF.y) + Math.abs(point.x - pointEF.x));
            point.parent = null;
            point.eraseVisited();
            point.setG(1);

        }


        while(!opened.isEmpty() && act != pointEF){
            act = opened.remove();
            closed.add(act);

            if(act.index - width > 0){
                tmp = points.get(act.index - width);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
            if(act.index - width -1 > 0 && (act.index - width - 1)%width < width-1 && act.index!=0){
                tmp = points.get(act.index - width -1);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
            if(act.index - width + 1 > 0 && (act.index - width + 1)%width > 0 ){
                tmp = points.get(act.index - width + 1);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
            if(act.index + width < width*height){
                tmp = points.get(act.index + width);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();

                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
            if(act.index + width - 1 < width*height && (act.index - 1)%width < width-1 && act.index!=0) {
                tmp = points.get(act.index + width - 1);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
            if(act.index + width + 1 < width*height && (act.index + 1)%width > 0) {
                tmp = points.get(act.index + width + 1);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
            if((act.index - 1)%width < width-1 && act.index!=0){
                tmp = points.get(act.index - 1);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
            if((act.index + 1)%width  > 0){
                tmp = points.get(act.index + 1);
                if(!tmp.checkVisited() && !tmp.isBlock){
                    if(!isPlayer && tmp.isEnemyPath){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(isPlayer){
                        tmp.setParent(act);
                        tmp.setG(act.getG() + tmp.getG());
                        opened.add(tmp);
                        tmp.setVisited();
                    }else if(!isPlayer){
                        if((!enemy.playerDetected && enemy.chasingPlayer) || enemy.playerDetected){
                            tmp.setParent(act);
                            tmp.setG(act.getG() + tmp.getG());
                            opened.add(tmp);
                            tmp.setVisited();
                        }
                    }
                }
            }
        }
        act = pointEF;
        actPath.clear();
        while(act!=null){

            actPath.add(act);
            act = act.getParent();
            if(act == pointSF){
                return 1;
            }
        }
        return 0;
    }

    public void UpdatePosition(int x, int y){
        pointS.x = x;
        pointS.y = y;
        sf = true;
    }

    public void UpdateDestPosition(int x, int y){
        pointE.x = x;
        pointE.y = y;
        sf = false;
        hasPath = true;
    }


    public Vector<MyPoint> getPath(){
            for(MyPoint point:points)
            {
                if(this.pointS.x + v.getX() > point.x-8 && this.pointS.x + v.getX() < point.x+8 && this.pointS.y + v.getY() > point.y-8 && this.pointS.y + v.getY() < point.y+8) {
                    pointSF = point;
                    sf = true;
                }
                if(this.pointE.x > point.x-8 && this.pointE.x < point.x+8 && this.pointE.y > point.y-8 && this.pointE.y < point.y+8) {
                    if(!point.isBlock){
                        pointEF = point;
                        ef = true;
                    }
                }
            }


        if(sf == true && ef == true){
            if(hasPath){
                FindPath();
                hasPath = false;
                return actPath;
            }
        }
        return null;
    }



    public void drawPosibleMoves(Canvas c){
        Paint p = new Paint();
        if(!isPlayer)
            for(MyPoint point:actPath){
                    c.drawCircle(point.x-v.getX(), point.y-v.getY(), 8, new Paint());
            }
    }
}
