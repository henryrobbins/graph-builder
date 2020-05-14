package com.henryrobbins.creator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public abstract class Statistics {

	// HashMap used to calculate statistics
	public LinkedHashMap<Integer, ArrayList<Integer>> hashMap= new LinkedHashMap<Integer, ArrayList<Integer>>();

	// ArrayList of stats
	public ArrayList<Stat> statistics= new ArrayList<Stat>();
	public Stat nodes= new Stat("Nodes");
	public Stat edges= new Stat("Edges");
	public Stat connected= new Stat("Connected");
	public Stat sDegree= new Stat("Smallest Degree");
	public Stat lDegree= new Stat("Largest Degree");
	public Stat aDegree= new Stat("Average Degree");
	public Stat sGeodesic= new Stat("Smallest Geodesic");
	public Stat lGeodesic= new Stat("Largest Geodesic");
	public Stat aGeodesic= new Stat("Average Geodesic");

	// Add stats to ArrayList
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

	// Updates the hashMap
	public void updateHashMap(LinkedHashMap<Integer, ArrayList<Integer>> hM) {
		hashMap= hM;

		for (int id : hashMap.keySet()) {
			hashMap.put(id, shortenArray(id, hashMap.get(id)));
		}

		setStatistics();
	}

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

	public void setNodes() {

		nodes.setStat(hashMap.size());

	}

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

	public void setSDegree() {

		int value= hashMap.get(1).size();

		for (int id : hashMap.keySet()) {
			if (hashMap.get(id).size() <= value) {
				value= hashMap.get(id).size();
			}
		}

		sDegree.setStat(value);
	}

	public void setLDegree() {

		int value= hashMap.get(1).size();

		for (int id : hashMap.keySet()) {
			if (hashMap.get(id).size() >= value) {
				value= hashMap.get(id).size();
			}
		}

		lDegree.setStat(value);
	}

	public void setADegree() {

		double value= 0;

		for (int id : hashMap.keySet()) {
			value+= hashMap.get(id).size();
		}

		value/= hashMap.size();

		aDegree.setStat(value);
	}

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

	// Given two random people, finds degree of separation between them
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

	// Checks to see if an Integer is in an Array
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

	// Shortens a given array and removes id
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
