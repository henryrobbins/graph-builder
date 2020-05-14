package com.henryrobbins.creator.directed;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JFrame;

import com.henryrobbins.creator.Edge;
import com.henryrobbins.creator.Graph;
import com.henryrobbins.creator.Node;
import com.henryrobbins.creator.Stat;

public class DirectedGraph extends Graph {
	
	// Setup Statistics
	public DirectedStatistics stats;
			
	public DirectedGraph(DirectedStatistics stats) {
		super();
		window.setTitle("Directed Graph");
		this.stats = stats;
	}
	
	// This draws on the window 
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
			drawArrow(g,mx, my-20);
			g.drawLine(initialNode.x, initialNode.y, mx, my-20);
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

	private void drawArrow(Graphics g,int mx,int my) {
		
		int[] x = new int[3];
		int[] y = new int[3];

		double m = 0;	
		double rAngle = 0;
		double height = 0;
		double base = 0;
			
		if (mx-initialNode.x == 0) {	
			height = 10+(2.5*Math.sqrt(3));
			base = 0;	
		} else {	
			m = (double)(my-initialNode.y) / (double)(mx-initialNode.x);	
			rAngle = Math.atan(Math.abs(m));
			height = (2.5*Math.sqrt(3))*Math.sin(rAngle);
			base = Math.sqrt(Math.pow((2.5*Math.sqrt(3)),2)-Math.pow(height, 2));
		}

		int tx = 0;
		int ty = 0;
				
		if (my-initialNode.y < 0) {
			if (mx-initialNode.x < 0) {
				tx = (int) (mx+base);
				ty = (int) (my+height);
			} else {
				tx = (int) (mx-base);
				ty = (int) (my+height);
			}
		} else {
			if (mx-initialNode.x < 0) {
				tx = (int) (mx+base);
				ty = (int) (my-height);
			} else {
				tx = (int) (mx-base);
				ty = (int) (my-height);
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
		
		if (mx-initialNode.x != 0) {	
			if (my-initialNode.y < 0) {
				if (mx-initialNode.x < 0) {
					g2d.rotate(rAngle-(Math.PI/2),tx,ty);
				} else {
					g2d.rotate((Math.PI/2)-rAngle,tx,ty);
				}
			} else {
				if (mx-initialNode.x < 0) {
					g2d.rotate((((Math.PI/2)-rAngle)+Math.PI),tx,ty);
				} else {
					g2d.rotate(rAngle+(Math.PI/2),tx,ty);
				}
			}
		} else {
			if (my > initialNode.y) {
				g2d.rotate(Math.PI,tx,ty);
			}
		}
		
		g.fillPolygon(x,y,3); 
		g2d.setTransform(old);
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
		}		
		
		for (Node node: nodes) {
			hashMap.put(node.id,node.connections);
		}
		
		return hashMap;		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
			
		mx = e.getX();
		my = e.getY();
		
		if (validMousePosition(mx,my)) {
			if (nPressed) {
				nodes.add(new Node((e.getX()-10), (e.getY()-35)));
				updateStats = true;
			} else if (ePressed) {
				finalNode = findClosestNode();
				edges.add(new DirectedEdge(initialNode, finalNode));
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