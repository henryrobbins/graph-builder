package com.henryrobbins.creator.structural;

import java.awt.Graphics;

import com.henryrobbins.creator.Edge;
import com.henryrobbins.creator.Node;

public class StructuralEdge extends Edge{
	
	public boolean isPositive;

	public StructuralEdge(Node n1, Node n2, boolean isPositive) {
		super(n1, n2);
		this.isPositive = isPositive;
	}
	
	public void drawEdge(Graphics g) {
		g.drawLine(n1.x, n1.y, n2.x, n2.y);
		
		if (isPositive) {
			g.drawLine(((n1.x+n2.x)/2)+5, ((n1.y+n2.y)/2)-15,((n1.x+n2.x)/2)-5, ((n1.y+n2.y)/2)-15);
			g.drawLine(((n1.x+n2.x)/2), ((n1.y+n2.y)/2)-10,((n1.x+n2.x)/2), ((n1.y+n2.y)/2)-20);
		} else {
			g.drawLine(((n1.x+n2.x)/2)+5, ((n1.y+n2.y)/2)-15,((n1.x+n2.x)/2)-5, ((n1.y+n2.y)/2)-15);
		}
		
	}

}
