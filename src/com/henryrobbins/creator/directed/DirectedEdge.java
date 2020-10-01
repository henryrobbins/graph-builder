package com.henryrobbins.creator.directed;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import com.henryrobbins.creator.*;

/** Maintains information related to the directed edge of a graph including visual representation */
public class DirectedEdge extends Edge{

	/** Construct a directed edge */
	public DirectedEdge(Node n1, Node n2) {
		super(n1, n2);
	}

	/** Draw this directed edge on the Graphics */
	@Override
	public void drawEdge(Graphics g) {
		if (n1.x == n2.x && n1.y == n2.y) {
			return;
		}
		
		g.drawLine(n1.x, n1.y, n2.x, n2.y);
			
		int[] x = new int[3];
		int[] y = new int[3];

		double m = 0;	
		double rAngle = 0;
		double height = 0;
		double base = 0;
			
		if (n2.x-n1.x == 0) {	
			height = 10+(2.5*Math.sqrt(3));
			base = 0;	
		} else {	
			m = (double)(n2.y-n1.y) / (double)(n2.x-n1.x);	
			rAngle = Math.atan(Math.abs(m));
			height = (10+2.5*Math.sqrt(3))*Math.sin(rAngle);
			base = Math.sqrt(Math.pow((10+2.5*Math.sqrt(3)),2)-Math.pow(height, 2));
		}

		int tx = 0;
		int ty = 0;
				
		if (n2.y-n1.y < 0) {
			if (n2.x-n1.x < 0) {
				tx = (int) (n2.x+base);
				ty = (int) (n2.y+height);
			} else {
				tx = (int) (n2.x-base);
				ty = (int) (n2.y+height);
			}
		} else {
			if (n2.x-n1.x < 0) {
				tx = (int) (n2.x+base);
				ty = (int) (n2.y-height);
			} else {
				tx = (int) (n2.x-base);
				ty = (int) (n2.y-height);
			}
		}
				
		x[0] = tx;
		y[0] = (int) (ty - (2.5*Math.sqrt(3)));
		x[1] = tx - 5;
		y[1] = (int) (ty + (2.5*Math.sqrt(3)));
		x[2] = tx + 5;
		y[2] = (int) (ty + (2.5*Math.sqrt(3)));
				
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform old = g2d.getTransform();
		
		if (n2.x-n1.x != 0) {	
			if (n2.y-n1.y < 0) {
				if (n2.x-n1.x < 0) {
					g2d.rotate(rAngle-(Math.PI/2),tx,ty);
				} else {
					g2d.rotate((Math.PI/2)-rAngle,tx,ty);
				}
			} else {
				if (n2.x-n1.x < 0) {
					g2d.rotate((((Math.PI/2)-rAngle)+Math.PI),tx,ty);
				} else {
					g2d.rotate(rAngle+(Math.PI/2),tx,ty);
				}
			}
		} else {
			if (n2.y > n1.y) {
				g2d.rotate(Math.PI,tx,ty);
			}
		}
		
		g.fillPolygon(x,y,3); 
		g2d.setTransform(old);
	}
}