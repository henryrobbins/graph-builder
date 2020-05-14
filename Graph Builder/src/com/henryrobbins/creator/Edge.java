package com.henryrobbins.creator;

import java.awt.Graphics;

public class Edge {

	public Node n1;
	public Node n2;
	
	public Edge(Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
	}
	
	public void drawEdge(Graphics g) {
		g.drawLine(n1.x, n1.y, n2.x, n2.y);	
	}
	
	// Gets the distance between the node and an x,y point
	public double getDistance (double mx, double my) {
		return Math.sqrt(Math.pow((mx-((n1.x+n2.x)/2.0)), 2) + Math.pow((my-((n1.y+n2.y)/2.0)), 2));
	}
}
