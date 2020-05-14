package com.henryrobbins.creator;

public class Stat {

	public String name = "";
	public String statistic = "";
	public int intStat = 0;
	public double doubleStat = 0;
	public boolean booleanStat = true;
	
	public Stat(String name) {
		this.name = name;
	}

	public void setStat(int statistic) {
		this.intStat = statistic;
		this.statistic = String.valueOf(intStat);
	}
	
	public void setStat(double statistic) {
		this.doubleStat = statistic;
		this.statistic = String.valueOf(doubleStat);
	}
	
	public void setStat(boolean statistic) {
		this.booleanStat = statistic;
		this.statistic = String.valueOf(booleanStat);
	}
	
}
