package org.fishapps.tdp;

import android.graphics.*;
import android.graphics.drawable.shapes.Shape;

import java.util.Vector;

public class Shadow {
	
	Sprite srcSprite;		// obiekt, ktorego cien bedzie rzucany
	Light  srcLight;		// zrodlo swiatla
	Bitmap dstBitmap;		// koncowy wynik cienia 	
	
	float mDistance;		// odleglosc od zrodla swiatla
	float alpha;			// przezroczystosc cienia - zalezna od odleglosci od zrodla swiatla
	float tan;				// kat pod jakim swiatlo pada na obiekt
	public int x, y;		// pozycja cienia
	Shape shape;			// ksztalt cienia - do przeksztalcen,
							// w ksztalt bedzie rysowana tekstura obiektu zrodlowego 
	
	
	Vector<Point> vertices; // wierzcholki bitmapy
	Vector<Point> 			// wierzcholki docelowe
		dstVertices; 
	Paint paint;			// kolor rysowania cienia i jego przezroczystosc
	Path path;				// ksztalt cienia
	
	Viewport v;
	
	// Konstruktor cienia
	public Shadow( Sprite srcSprite, Light srcLight, Viewport v ) {
		this.srcLight 	= srcLight;
		this.srcSprite 	= srcSprite;
		this.v = v;
		vertices = new Vector<Point>();
		dstVertices = new Vector<Point>();
		x = srcSprite.x;
		y = srcSprite.y;
		initVertices();
	}
	
	// funkcja wczytujaca pozycje wierzcholkow
	private void initVertices() {
		Rect view = v.getBounds();
		Bitmap sBitmap = srcSprite.getBitmap();
		for( int i= 0; i< srcSprite.getBitmapWidth(); i++ ) {
			for( int j= 0; j< srcSprite.getBitmapHeight(); j++ ) {
				int current = sBitmap.getPixel(i, j);
				// jezeli jest czarny piksel na obrazku to wierzcholek
				if( current == Color.BLACK ) {
					vertices.add(new Point(i+x, j+y));
				}
			}
		}
	}
	
	// funkcja aktualizujaca pozycje obrot cienia 
	public void update() {
		createShadow();
	}
	private void updateVertices() {
		Rect view = v.getBounds();
		for( int i = 0; i< vertices.size(); i++ ) {
			vertices.get(i).x-=view.left;
			vertices.get(i).y-=view.top;
		}
	}
	// funkcja pomocnicza obliczajaca pozycje wierzcholkow docelowych 
	private void computeShadow() {
		Rect view = v.getBounds();
		dstVertices.removeAllElements();
		for( int i= 0; i< vertices.size(); i++ ) {
		
				// wyznaczenie wspolczynnika przyrostu y/x
				// wyznaczenie wspolczynnikow prostej
				int tx = vertices.get(i).x;
				int ty = vertices.get(i).y;
				int lx = srcLight.x;
				int ly = srcLight.y;
				int r = srcLight.radius;
	
				double rx = Math.sqrt((tx-lx)*(tx-lx)+(ty-ly)*(ty-ly));
		        double cirx = (float) ((tx-lx)*srcLight.radius/rx)+lx;
		        double ciry = (float) ((ty-ly)*srcLight.radius/rx)+ly;
				Point dstPoint = new Point( (int) cirx, (int)ciry);
				dstVertices.add(dstPoint);
				
			}
		
		if( dstVertices.size() > 2 ) {
			createShadowPath();
		}
	}
	
	// funkcja obliczajaca odleglosc miedzy 2 punktami 
	private double distance( Point p1, Point p2 ) {
		return Math.sqrt( Math.pow( p2.x - p1.x, 2) + Math.pow( p2.y - p1.y , 2) );
	}
	// funkcja znajdujaca 2 skrajne wierzcholki cienia
	private int [] getVerticesIndex() {
		double tmp = 0;
		int index1 = 0, index2 = 0;
		for( int i = 0; i< dstVertices.size(); i++ ) {
			for( int j = 0; j< dstVertices.size(); j++ ) {
				// odleglosc miedzy wierzcholkami cienia
				double distance = distance( dstVertices.get(i), dstVertices.get(j));
				
				// odleglosc miedzy 
				if( distance > tmp ) {
					tmp = distance;
					index1 = i;
					index2 = j;
				}
			}
		}
		return new int[] { index1, index2 };
	}
	
	// funkcja tworzaca gradient i sciezke rysowania 
	private void createShadowPath() {
		path = new Path();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		int [] index = getVerticesIndex();
		Rect view = v.getBounds();
		path.moveTo(dstVertices.get(index[0]).x-view.left, dstVertices.get(index[0]).y-view.top );
		path.lineTo(dstVertices.get(index[1]).x-view.left, dstVertices.get(index[1]).y-view.top );
		path.lineTo(vertices.get(index[1]).x-view.left, vertices.get(index[1]).y-view.top );
		path.lineTo(vertices.get(index[0]).x-view.left, vertices.get(index[0]).y-view.top );
		path.moveTo(dstVertices.get(index[0]).x-view.left, dstVertices.get(index[0]).y-view.top );
		
		paint.setColor(Color.BLACK);
		paint.setAlpha(30);
		paint.setShader( new LinearGradient( vertices.get(index[0]).x-view.left, vertices.get(index[0]).y-view.top ,dstVertices.get(index[0]).x-view.left, dstVertices.get(index[0]).y-view.top ,0xff000000, Color.TRANSPARENT, Shader.TileMode.CLAMP));
	}
	
	// funkcja tworzaca cien
	private void createShadow() {
		computeShadow();
	}
	public void draw( Canvas canvas ) {
		//updateVertices();
		Rect view = v.getBounds();
		if( distance( vertices.get(0), new Point( srcLight.x, srcLight.y )) <= srcLight.radius ) {
			computeShadow();
			canvas.drawPath(path, paint);
		}
		canvas.drawText("POZYCJA SWIATLA " + srcLight.x + ", " + srcLight.y, srcLight.x - v.getX() , srcLight.y - v.getY(), new Paint(Paint.LINEAR_TEXT_FLAG));
		//+  canvas.drawCircle(srcLight.x, srcLight.y, srcLight.radius, new Paint(Paint.ANTI_ALIAS_FLAG ));

	}
	
}
