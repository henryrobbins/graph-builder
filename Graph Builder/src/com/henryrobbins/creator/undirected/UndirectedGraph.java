package com.henryrobbins.creator.undirected;

import java.awt.*;
import java.util.*;
import com.henryrobbins.creator.*;

public class UndirectedGraph extends Graph {
	
	// Setup Statistics
	public UndirectedStatistics stats;
	
	public UndirectedGraph(UndirectedStatistics stats) {
		super();
		window.setTitle("Undirected Graph");
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
	
}