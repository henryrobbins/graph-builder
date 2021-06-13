package com.henryrobbins.grid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.henryrobbins.Edge;
import com.henryrobbins.Node;
import com.henryrobbins.Stat;
import com.henryrobbins.Graph;

/** Maintains a graph builder window for building a grid graph */
public class GridGraph extends Graph{

	/** nearest node on grid */
	public Node nearNode;
	
	/** statistics for this grid graph */
	public GridStatistics stats;
			
	/** Construct a grid graph builder window */
	public GridGraph(GridStatistics stats) {
		super();
		window.setTitle("Grid Graph");
		this.stats = stats;
	}
	
	/** Additional implementation for drawing the window */
	@Override
	protected void paintComponent(Graphics g) {
			
		super.paintComponent(g);
			
		if (nPressed) {
			g.setColor(Color.GRAY);
			if (nodes.size()>0) {
				nearNode = findNearNode(moveNode);
				if (nearNode.x > mx && nearNode.y > my-25) {
					g.fillOval(nearNode.x-10, nearNode.y-60, 20, 20);
				} else if (nearNode.x < mx && nearNode.y > my-25) {
					g.fillOval(nearNode.x+40, nearNode.y-10, 20, 20);
				} else if (nearNode.x > mx && nearNode.y < my-25) {
					g.fillOval(nearNode.x-60, nearNode.y-10, 20, 20);
				} else {
					g.fillOval(nearNode.x-10, nearNode.y+40, 20, 20);
				}
			} else {
				g.fillOval(mx-10, my-35, 20, 20);
			}
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
		}	
			
		// Draws all nodes on frame
		for (Node node: nodes) {
			node.drawNode(g);
		}	
		
		// Draws all edges on frame
		for (Edge edge: edges) {
			edge.drawEdge(g);
		}	
			
		// UpdatesStats if needed
		if (updateStats) {
			stats.updateHashMap(compileHashMap());	
			updateStats = false;
		}
			
		// Draws statistics on screen
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
		
	/** return a hashmap representing the current graph */
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
	
	/** find closest node on grid */
	public Node findNearNode(Node exclude) {
			
		double min = nodes.get(0).getDistance(mx, my-25);
		Node closest = nodes.get(0);		
			
		for (Node node: nodes) {
			if (node != exclude) {
				if (node.getDistance(mx, my-25) < min) {
					min = node.getDistance(mx, my-25);
					closest = node;
				}
			}
		}
			
		return closest;	
	}
	
	/** runs when mouse is pressed. Action depends on key being pressed. */
	@Override
	public void mousePressed(MouseEvent e) {
				
		mx = e.getX();
		my = e.getY();
			
		if (validMousePosition(mx,my)) {
			if (nPressed) {
				if (nodes.size()>0) {
					if (nearNode.x > mx && nearNode.y > my-25) {
						nodes.add(new Node(nearNode.x-10, nearNode.y-60));
					} else if (nearNode.x < mx && nearNode.y > my-35) {
						nodes.add(new Node(nearNode.x+40, nearNode.y-10));
					} else if (nearNode.x > mx && nearNode.y < my-35) {
						nodes.add(new Node(nearNode.x-60, nearNode.y-10));
					} else {
						nodes.add(new Node(nearNode.x-10, nearNode.y+40));
					}
				} else {
					nodes.add(new Node(e.getX()-10, e.getY()-35));
				}
				updateStats = true;
			} else if (ePressed) {
				finalNode = findClosestNode();
				edges.add(new Edge(initialNode, finalNode));
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
		
	/** moves node with cursor while mouse is being dragged in valid location */
	@Override
	public void mouseDragged(MouseEvent e) {
			
		mx = e.getX();
		my = e.getY();
		
		if (validMousePosition(mx,my)) {
			if (!nPressed && !ePressed && !dPressed) {
				nearNode = findNearNode(moveNode);
				if (nearNode.x > mx && nearNode.y > my-25) {
					moveNode.setLocation(nearNode.x, nearNode.y-50);
				} else if (nearNode.x < mx && nearNode.y > my-25) {
					moveNode.setLocation(nearNode.x+50, nearNode.y);
				} else if (nearNode.x > mx && nearNode.y < my-25) {
					moveNode.setLocation(nearNode.x-50, nearNode.y);
				} else {
					moveNode.setLocation(nearNode.x, nearNode.y+50);
				}
			}
				
			repaint();
		}
	}	
}