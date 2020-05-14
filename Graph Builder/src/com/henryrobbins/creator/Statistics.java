package com.henryrobbins.creator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/** Maintains a list of statistics for a hashmap representation of a graph */
public abstract class Statistics {

	/** graph representation with node keys and their respective list of neighbors as values */
	public LinkedHashMap<Integer, ArrayList<Integer>> hashMap= new LinkedHashMap<Integer, ArrayList<Integer>>();

	/** list of computed stats */
	public ArrayList<Stat> statistics= new ArrayList<Stat>();
	/** stat maintaining number of nodes in graph */
	public Stat nodes= new Stat("Nodes");
	/** stat maintaining number of edges in graph */
	public Stat edges= new Stat("Edges");
	/** stat maintaining if graph is connected */
	public Stat connected= new Stat("Connected");
	/** stat maintaining smallest degree of a node in graph */
	public Stat sDegree= new Stat("Smallest Degree");
	/** stat maintaining largest degree of a node in graph */
	public Stat lDegree= new Stat("Largest Degree");
	/** stat maintaining average degree of a node in graph */
	public Stat aDegree= new Stat("Average Degree");
	/** stat maintaining smallest geodesic distance in graph */
	public Stat sGeodesic= new Stat("Smallest Geodesic");
	/** stat maintaining largest geodesic distance in graph */
	public Stat lGeodesic= new Stat("Largest Geodesic");
	/** stat maintaining average geodesic distance in graph */
	public Stat aGeodesic= new Stat("Average Geodesic");

	/** Add all maintained stats to the list of stats */
	public Statistics() {
		statistics.add(nodes);
		statistics.add(edges);
		statistics.add(connected);
		statistics.add(sDegree);
		statistics.add(lDegree);
		statistics.add(aDegree);
		statistics.add(sGeodesic);
		statistics.add(lGeodesic);
		statistics.add(aGeodesic);
	}

	/** Update the graph */
	public void updateHashMap(LinkedHashMap<Integer, ArrayList<Integer>> hM) {
		hashMap= hM;

		for (int id : hashMap.keySet()) {
			hashMap.put(id, shortenArray(id, hashMap.get(id)));
		}

		setStatistics();
	}

	/** Set all statistics for the current graph*/
	public void setStatistics() {
		setNodes();
		setEdges();
		setConnected();
		if (nodes.intStat > 0) {
			setSDegree();
			setLDegree();
			setADegree();
			setSGeodesic();
			setLGeodesic();
			setAGeodesic();
		}
	}

	/** Set number of nodes in graph */
	public void setNodes() {
		nodes.setStat(hashMap.size());
	}

	/** Set number of edges in graph */
	public void setEdges() {
		int value= 0;
		for (int id1 : hashMap.keySet()) {
			for (int id2 : hashMap.keySet()) {
				if (id2 > id1) {
					if (FindDegreeOfSeparation(id1, id2) == 1) {
						value++ ;
					}
				}
			}
		}
		edges.setStat(value);
	}

	/** Set connectivity stat */
	public void setConnected() {
		for (int id1 : hashMap.keySet()) {
			for (int id2 : hashMap.keySet()) {
				if (id2 != id1) {
					if (FindDegreeOfSeparation(id1, id2) == 0) {
						connected.setStat(false);
						return;
					}
				}
			}
		}
		connected.setStat(true);
	}
	
	/** Set smallest degree stat */
	public void setSDegree() {
		int value= hashMap.get(1).size();
		for (int id : hashMap.keySet()) {
			if (hashMap.get(id).size() <= value) {
				value= hashMap.get(id).size();
			}
		}
		sDegree.setStat(value);
	}

	/** Set largest degree stat */
	public void setLDegree() {
		int value= hashMap.get(1).size();
		for (int id : hashMap.keySet()) {
			if (hashMap.get(id).size() >= value) {
				value= hashMap.get(id).size();
			}
		}
		lDegree.setStat(value);
	}

	/** Set average degree stat */
	public void setADegree() {
		double value= 0;
		for (int id : hashMap.keySet()) {
			value+= hashMap.get(id).size();
		}
		value/= hashMap.size();
		aDegree.setStat(value);
	}

	/** Set smallest geodesic distance */
	public void setSGeodesic() {
		int value;
		if (hashMap.size() > 1) {
			value= FindDegreeOfSeparation(1, 2);
			for (int id1 : hashMap.keySet()) {
				for (int id2 : hashMap.keySet()) {
					if (id2 > id1) {
						if (FindDegreeOfSeparation(id1, id2) <= value) {
							value= FindDegreeOfSeparation(id1, id2);
						}
					}
				}
			}
		} else {
			value= 0;
		}
		sGeodesic.setStat(value);
	}

	/** Set largest geodesic distance */
	public void setLGeodesic() {
		int value;
		if (hashMap.size() > 1) {
			value= FindDegreeOfSeparation(1, 2);
			for (int id1 : hashMap.keySet()) {
				for (int id2 : hashMap.keySet()) {
					if (id2 > id1) {
						if (FindDegreeOfSeparation(id1, id2) >= value) {
							value= FindDegreeOfSeparation(id1, id2);
						}
					}
				}
			}
		} else {
			value= 0;
		}
		lGeodesic.setStat(value);
	}
	
	/** Set average geodesic distance */
	public void setAGeodesic() {
		double value;
		if (hashMap.size() > 1) {
			value= 0;
			for (int id1 : hashMap.keySet()) {
				for (int id2 : hashMap.keySet()) {
					if (id2 > id1) {
						value+= FindDegreeOfSeparation(id1, id2);
					}
				}
			}
			value/= ((hashMap.size() * (hashMap.size() - 1)) / 2);
		} else {
			value= 0;
		}
		aGeodesic.setStat(value);
	}

	/** Get degree of separation or geodesic distance between two nodes */
	public int FindDegreeOfSeparation(int member1, int member2) {
		ArrayList<Integer> Degrees= new ArrayList<Integer>();
		Degrees.add(member1);
		int degree= 0;
		boolean found= false;
		while (found == false) {
			if (degree < hashMap.size()) {
				// Generate an array of all connections in the given degree of separation
				ArrayList<Integer> degreesArray= new ArrayList<Integer>();
				for (Integer person : Degrees) {
					for (Integer connection : hashMap.get(person)) {
						degreesArray.add(connection);
					}
				}
				Degrees= degreesArray;
				ArrayList<Integer> shortArray= new ArrayList<Integer>();
				for (Integer element : Degrees) {
					if (inArray(shortArray, element) == false) {
						shortArray.add(element);
					}
				}
				Degrees= shortArray;

				// Checks array for the second member
				if (inArray(Degrees, member2) == true) {
					found= true;
				}
				degree++ ;
			} else {
				return 0;
			}
		}
		return degree;
	}

	/** returns true iff given integer is in array */
	public boolean inArray(ArrayList<Integer> Array, int Integer) {
		Collections.sort(Array);
		for (Integer element : Array) {
			if (element == Integer) { return true; }
			if (element > Integer) {
				break;
			}
		}
		return false;
	}

	/** returns array with removed duplicates */
	public ArrayList<Integer> shortenArray(int id, ArrayList<Integer> array) {
		ArrayList<Integer> newArray= new ArrayList<Integer>();
		for (int n : array) {
			if (!inArray(newArray, n) && n != id) {
				newArray.add(n);
			}
		}
		return newArray;
	}
}