package com.henryrobbins.creator;

import java.awt.Graphics;
import java.util.ArrayList;

public class Node {

	public int x;
	public int y;
	public int id;
	public ArrayList<Integer> connections = new ArrayList<Integer>();
	
	public Node(int x, int y) {
		this.x = x+10;
		this.y = y+10;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void drawNode(Graphics g) {
		g.fillOval(x-10, y-10, 20, 20);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void addConnection(Node node) {
		connections.add(node.id);
	}
	
	// Gets the distance between the node and an x,y point
	public double getDistance (double mx, double my) {
		return Math.sqrt(Math.pow((mx-x), 2) + Math.pow((my-y), 2));
	}
	
}
