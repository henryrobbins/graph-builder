package com.henryrobbins.directed;

import com.henryrobbins.Statistics;

/** Maintains the statistics on a directed graph */
public class DirectedStatistics extends Statistics {

	@Override
	public void setEdges() {
		int value = 0;
		for (int id1: hashMap.keySet()) {
			value += hashMap.get(id1).size();
		}
		edges.setStat(value);		
	}
}
