package com.henryrobbins.creator;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.henryrobbins.creator.directed.DirectedGraph;
import com.henryrobbins.creator.directed.DirectedStatistics;
import com.henryrobbins.creator.grid.GridGraph;
import com.henryrobbins.creator.grid.GridStatistics;
import com.henryrobbins.creator.structural.StructuralGraph;
import com.henryrobbins.creator.structural.StructuralStatistics;
import com.henryrobbins.creator.undirected.UndirectedGraph;
import com.henryrobbins.creator.undirected.UndirectedStatistics;

/** Starting window of application where user selects type of graph to build */
public class StartScreen extends JFrame implements ActionListener {

	/** list of current graphs being built */
	ArrayList<Graph> graphs= new ArrayList<Graph>();
	/** list of statistics for current graphs being built*/
	ArrayList<Statistics> statistics= new ArrayList<Statistics>();

	/** button to create an undirected graph */
	JButton undirectedGraph= new JButton("New Undirected Graph");
	/** button to create a directed graph */
	JButton directedGraph= new JButton("New Directed Graph");
	/** button to create a structural graph */
	JButton structuralGraph= new JButton("New Structural Graph");
	/** button to create a grid graph */
	JButton gridGraph= new JButton("New Grid Graph");

	/** Construct a new start screen */
	public StartScreen() {
		JFrame window= new JFrame();
		window.setTitle("Graph Creator");
		window.setSize(400, 200);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLayout(new GridLayout(4, 1, 0, 0));
		Dimension screenSize= new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		Dimension windowSize= new Dimension(getPreferredSize());
		int wdwLeft= (screenSize.width / 2 - windowSize.width / 2);
		int wdwTop= (screenSize.height / 2 - windowSize.height / 2) - 50;
		window.setLocation(wdwLeft, wdwTop);

		window.add(undirectedGraph);
		undirectedGraph.addActionListener(this);

		window.add(directedGraph);
		directedGraph.addActionListener(this);

		window.add(structuralGraph);
		structuralGraph.addActionListener(this);

		window.add(gridGraph);
		gridGraph.addActionListener(this);

		window.setVisible(true);
	}

	/** Set dimensions of start screen window */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 150);
	}

	/** Create a graph builder window corresponding to the pressed button */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == undirectedGraph) {
			statistics.add(new UndirectedStatistics());
			graphs.add(new UndirectedGraph((UndirectedStatistics) statistics.get(statistics.size() - 1)));
		}
		if (e.getSource() == directedGraph) {
			statistics.add(new DirectedStatistics());
			graphs.add(new DirectedGraph((DirectedStatistics) statistics.get(statistics.size() - 1)));
		}

		if (e.getSource() == structuralGraph) {
			statistics.add(new StructuralStatistics());
			graphs.add(new StructuralGraph((StructuralStatistics) statistics.get(statistics.size() - 1)));
		}
		if (e.getSource() == gridGraph) {
			statistics.add(new GridStatistics());
			graphs.add(new GridGraph((GridStatistics) statistics.get(statistics.size() - 1)));
		}
	}
}