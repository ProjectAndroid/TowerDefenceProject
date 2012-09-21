package org.fishapps.tdp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import org.fishapps.map.Map;

public class Light {
	
	public int x, y;
	public int intensity;
	public int radius;
	private Map map;
    private Viewport v;
	
	Bitmap b;
	
	public Light(Viewport v) {
        this.v = v;
    }

	public void draw( Canvas c )  {
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setAlpha(20);
		c.drawCircle(x - v.getX(), y-v.getY(), radius, p);
	}
}
