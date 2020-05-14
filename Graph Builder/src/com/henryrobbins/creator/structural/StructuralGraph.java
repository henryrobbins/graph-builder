package com.henryrobbins.creator.structural;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import com.henryrobbins.creator.Edge;
import com.henryrobbins.creator.Graph;
import com.henryrobbins.creator.Node;
import com.henryrobbins.creator.Stat;

public class StructuralGraph extends Graph{
	
	private boolean rPressed;
	
	// Setup Statistics
	public StructuralStatistics stats;
		
	public StructuralGraph(StructuralStatistics stats) {
		super();
		window.setTitle("Structural Graph");
		this.stats = stats;
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (nPressed) {
			g.setColor(Color.GRAY);
			g.fillOval(mx-10, my-35, 20, 20);
			g.setColor(Color.BLACK);
		}	
		if (dPressed) {
			g.setColor(Color.RED);
			g.drawLine(mx-10, my-35, mx+10, my-15);
			g.drawLine(mx+10, my-35, mx-10, my-15);
			g.setColor(Color.BLACK);
		}
		if (ePressed) {
			g.drawLine(initialNode.x, initialNode.y, mx, my-20);
			g.drawLine(((initialNode.x+mx)/2)+5, ((initialNode.y+my-20)/2)-15,((initialNode.x+mx)/2)-5, ((initialNode.y+my-20)/2)-15);
			g.drawLine(((initialNode.x+mx)/2), ((initialNode.y+my-20)/2)-10,((initialNode.x+mx)/2), ((initialNode.y+my-20)/2)-20);
		}			
		if (rPressed) {
			g.drawLine(initialNode.x, initialNode.y, mx, my-20);
			g.drawLine(((initialNode.x+mx)/2)+5, ((initialNode.y+my-20)/2)-15,((initialNode.x+mx)/2)-5, ((initialNode.y+my-20)/2)-15);
		}	
		// Draws all nodes on frame
		for (Node node: nodes) {
			node.drawNode(g);
		}	
		// Draws all edges on frame
		for (Edge edge: edges) {
			edge.drawEdge(g);
		}	
		
		if (updateStats) {	
			stats.updateHashMap(compileHashMap());	
			updateStats = false;
		}
		
		g.setFont(new Font("Helvetica", Font.PLAIN, 15)); 
		int y = 80;
		for (Stat stat: stats.statistics) {
			g.drawString(stat.name, (int)(windowSize.width - 300)+20, y);
			g.drawString(stat.statistic, (int)(windowSize.width - 300)+200, y);
			y += 20;
		}
		
		y += 50;
		g.drawString("Connections List:", (int)(windowSize.width - 300)+20, y);
		y += 20;
		for (int id: stats.hashMap.keySet()) {
			g.drawString((id + ": " + stats.hashMap.get(id)), (int)(windowSize.width - 300)+20, y);
			y += 20;
		}
		
	}
	
	// Complies and returns a hashMap to represent drawn graph
	public LinkedHashMap<Integer,ArrayList<Integer>> compileHashMap() {
			
		LinkedHashMap<Integer,ArrayList<Integer>> hashMap = new LinkedHashMap<Integer,ArrayList<Integer>>();
			
		for (Node node: nodes) {
			node.connections.clear();
			node.setID(nodes.indexOf(node)+1);
		}
		
		for (Edge edge: edges) {
			edge.n1.addConnection(edge.n2);
			edge.n2.addConnection(edge.n1);
		}		
			
		for (Node node: nodes) {
			hashMap.put(node.id,node.connections);
		}
			
		return hashMap;		
				
	}
	
	// This code runs when mouse is dragged in window
	@Override
	public void mouseDragged(MouseEvent e) {
			
		mx = e.getX();
		my = e.getY();
			
		if (validMousePosition(mx,my)) {
			if (!nPressed && !ePressed && !rPressed && !dPressed) {
				moveNode.setLocation(mx, my-25);
			}
				
			repaint();
		}
	}
	
	// This code runs when a key "e" is pressed
	@Override
	public void keyPressed(KeyEvent e) {

		int id = e.getKeyCode();
		
		if (validMousePosition(mx,my)) {
			if (id == KeyEvent.VK_N) {
				nPressed = true;
			}	
			if (id == KeyEvent.VK_E) {
				initialNode = findClosestNode();
				ePressed = true;
			}
			if (id == KeyEvent.VK_R) {
				initialNode = findClosestNode();
				rPressed = true;
			}
			if (id == KeyEvent.VK_D) {
				dPressed = true;
			}
				
			repaint();
		}
			
	}
	 
	// This code runs when a key "e" is released
	@Override
	public void keyReleased(KeyEvent e) {
		
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_N) {
			nPressed = false;
		}
		if (id == KeyEvent.VK_E) {
			ePressed = false;
		}
		if (id == KeyEvent.VK_R) {
			rPressed = false;
		}
		if (id == KeyEvent.VK_D) {
			dPressed = false;
		}
			
		repaint();
			
	}
	
	// This code runs when the mouse is pressed
	@Override
	public void mousePressed(MouseEvent e) {
				
		mx = e.getX();
		my = e.getY();
		
		if (validMousePosition(mx,my)) {
			if (nPressed) {
				nodes.add(new Node(e.getX()-10, e.getY()-35));
				updateStats = true;
			} else if (ePressed) {
				finalNode = findClosestNode();
				edges.add(new StructuralEdge(initialNode, finalNode, true));
				updateStats = true;
			} else if (rPressed){
				finalNode = findClosestNode();
				edges.add(new StructuralEdge(initialNode, finalNode, false));
				updateStats = true;
			} else if (dPressed){
				if (nodes.size()>0 && edges.size()>0) {
					deleteNode = findClosestNode();
					deleteEdge = findClosestEdge();
					if (deleteNode.getDistance(mx, my-25)<deleteEdge.getDistance(mx, my-25)) {
						nodes.remove(deleteNode);
						Iterator<Edge> iter = edges.iterator(); 
						while(iter.hasNext()) {
							Edge edge = iter.next();
							if (edge.n1 == deleteNode || edge.n2 == deleteNode) {
								iter.remove();
								edge = null;
							}
						}
						deleteNode = null;
					} else {
						edges.remove(deleteEdge);
						deleteEdge = null;
					}
				} else if (nodes.size()>0) {
					deleteNode = findClosestNode();
					nodes.remove(deleteNode);
					Iterator<Edge> iter = edges.iterator(); 
					while(iter.hasNext()) {
						Edge edge = iter.next();
						if (edge.n1 == deleteNode || edge.n2 == deleteNode) {
							iter.remove();
							edge = null;
						}
					}
					deleteNode = null;
				} else {
					deleteEdge = findClosestEdge();
					edges.remove(deleteEdge);
					deleteEdge = null;
				}
				updateStats = true;
			} else {
				moveNode = findClosestNode();
			}
				
			repaint();
		}
	}	
}
