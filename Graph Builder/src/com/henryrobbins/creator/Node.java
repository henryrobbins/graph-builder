package com.henryrobbins.creator;

import java.awt.Graphics;
import java.util.ArrayList;

/** Maintains information related to the node of a graph including visual representation */
public class Node {

	/** id of the node */
	public int id;
	/** list of this nodes neighbors */
	public ArrayList<Integer> connections = new ArrayList<Integer>();
	/** x-coordinate location of node on screen */
	public int x;
	/** y-coordinate location of node on screen */
	public int y;
	
	/** Create a new node at the specified coordinates */
	public Node(int x, int y) {
		this.x = x+10;
		this.y = y+10;
	}
	
	/** Set the id of this node */
	public void setID(int id) {
		this.id = id;
	}
	
	/** Draw this node to the given Graphics */
	public void drawNode(Graphics g) {
		g.fillOval(x-10, y-10, 20, 20);
	}
	
	/** Set the location of this node on the window */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/** Add a new neighboring node */
	public void addConnection(Node node) {
		connections.add(node.id);
	}
	
	/** Get distance between this node and some x,y point */
	public double getDistance (double mx, double my) {
		return Math.sqrt(Math.pow((mx-x), 2) + Math.pow((my-y), 2));
	}
}