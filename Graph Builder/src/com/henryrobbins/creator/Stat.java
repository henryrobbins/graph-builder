package com.henryrobbins.creator;

/** Maintains information related to a statistic of a graph */
public class Stat {

	/** name of the statistic */
	public String name = "";
	/** string value of statistic*/
	public String statistic = "";
	/** integer value of statistic */
	public int intStat = 0;
	/** double value of statistic */
	public double doubleStat = 0;
	/** boolean value of statistic */
	public boolean booleanStat = true;
	
	/** Construct a new statistic */
	public Stat(String name) {
		this.name = name;
	}

	/** Set the integer value of statistic */
	public void setStat(int statistic) {
		this.intStat = statistic;
		this.statistic = String.valueOf(intStat);
	}
	
	/** Set the double value of statistic */
	public void setStat(double statistic) {
		this.doubleStat = statistic;
		this.statistic = String.valueOf(doubleStat);
	}
	
	/** Set the boolean value of statistic */
	public void setStat(boolean statistic) {
		this.booleanStat = statistic;
		this.statistic = String.valueOf(booleanStat);
	}
}