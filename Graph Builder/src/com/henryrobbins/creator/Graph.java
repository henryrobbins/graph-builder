package com.henryrobbins.creator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public abstract class Graph extends JComponent implements MouseListener, MouseMotionListener, KeyListener {
	
	// ArrayList of Nodes and Edges
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();;
	
	// Checks for keys pressed
	public boolean nPressed;	
	public boolean ePressed;
	public boolean dPressed;
	
	// Mouse position
	public int mx;
	public int my;
	
	// Various Node and Edge types
	public Node initialNode;
	public Node finalNode;
	public Node moveNode;
	public Node deleteNode;
	public Edge deleteEdge;
	
	// Updates stats when true
	public boolean updateStats = false; 
	
	// Screen Dimensions
	public Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	public Dimension windowSize;
	
	public JFrame window = new JFrame("Graph");

	// Launches Graph
	public Graph(){	
		window.add(this);
		window.pack();
		window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		windowSize = window.getBounds().getSize();
		int wdwLeft = (screenSize.width / 2 - windowSize.width / 2);
        int wdwTop = (screenSize.height / 2 - windowSize.height / 2)-50;
        window.setLocation(wdwLeft, wdwTop);
		
		window.addMouseListener(this);	
		window.addMouseMotionListener(this);
		window.addKeyListener(this);
	}
	
	// Sets dimension
	public Dimension getPreferredSize() {
		return new Dimension(550,275);
	}
	
	// Paints window
	@Override
	protected void paintComponent(Graphics g) {
		
		windowSize = window.getBounds().getSize();
		
		g.drawLine(0, 50, windowSize.width, 50);
		g.drawLine((int)(windowSize.width - 300), 0, (int)(windowSize.width - 300), windowSize.height);
		
		g.setFont(new Font("Helvetica", Font.PLAIN, 18)); 
		g.drawString("Graph", (int)(((windowSize.width - 300)/2.0)-30), 35);
		g.drawString("Statistics", (int)(windowSize.width-185), 35);
			
	}
	
	// Finds the closest node to the current mousePosition
	public Node findClosestNode() {
		
		double min = nodes.get(0).getDistance(mx, my-25);
		Node closest = nodes.get(0);		
		
		for (Node node: nodes) {
			if (node.getDistance(mx, my-25) < min) {
				min = node.getDistance(mx, my-25);
				closest = node;
			}
		}
		
		return closest;	
	}
	
	// Finds the closest edge to the current mousePosition
	public Edge findClosestEdge() {
			
		double min = edges.get(0).getDistance(mx, my-25);
		Edge closest = edges.get(0);		
			
		for (Edge edge: edges) {
			if (edge.getDistance(mx, my-25) < min) {
				min = edge.getDistance(mx, my-25);
				closest = edge;
			}
		}
			
		return closest;	
	}
	
	// Checks if mouse position is valid
	public boolean validMousePosition(int mx, int my) {
		if (mx >= 0 && mx <= (int)(windowSize.width * (3.0/4.0)) && my >= 75 && my <= windowSize.height+25) {
			return true;
		} else {
			return false;
		}
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
	
	// This code runs when mouse is dragged in window
	@Override
	public void mouseDragged(MouseEvent e) {
		
		mx = e.getX();
		my = e.getY();
		
		if (validMousePosition(mx,my)) {
			if (!nPressed && !ePressed && !dPressed) {
				moveNode.setLocation(mx, my-25);
			}
				
			repaint();
		}
	}

	// This code runs when mouse is moved in window
	@Override
	public void mouseMoved(MouseEvent e) {
		
		mx = e.getX();
		my = e.getY();
		
		repaint();
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
			if (id == KeyEvent.VK_D) {
				dPressed = false;
			}
			
			repaint();
		
	}
	
	// These methods aren't used in this program
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

		@Override
	public void keyTyped(KeyEvent e) {
			// Gets id of key typed
			int id = e.getKeyCode();
			// KeyEvent.VK_(letter or number etc...)
			// Example: KeyEvent.VK_W	
		}
}