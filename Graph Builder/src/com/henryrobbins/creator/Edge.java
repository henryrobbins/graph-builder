package com.henryrobbins.creator;

import java.awt.Graphics;

/** Maintains information related to the edge of a graph including visual representation */
public class Edge {

	/** tail node of this edge */
	public Node n1;
	/** head node of this edge */
	public Node n2;
	
	/** Construct an edge between some tail and head node */
	public Edge(Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
	}
	
	/** Draw this edge on the given Graphics */
	public void drawEdge(Graphics g) {
		g.drawLine(n1.x, n1.y, n2.x, n2.y);	
	}
	
	/** Get distance between center of this edge and an x,y point */
	public double getDistance (double mx, double my) {
		return Math.sqrt(Math.pow((mx-((n1.x+n2.x)/2.0)), 2) + Math.pow((my-((n1.y+n2.y)/2.0)), 2));
	}
}
