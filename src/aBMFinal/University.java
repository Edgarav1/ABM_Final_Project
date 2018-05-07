package aBMFinal;

import java.util.ArrayList;

import repast.simphony.space.grid.Grid;

public class University {

	/** Talento minimo que se necesita para acceder a la universidad */
	double minTalent;
	double minWealth;
	
	static double probFriend = 0.5;
	
	double meanTalentDist;
	double varianceTalentDist;
	
	ArrayList<Individual> alumni;
	
	public Grid grid;
	
	// Constructor
	public University(Grid<Object> grid, double minTalent, double minWealth, double meanTalentDist, double varianceTalentDist) {
		// TODO Auto-generated constructor stub
		this.minTalent = minTalent;
		this.minWealth = minWealth;
		this.meanTalentDist = meanTalentDist;
		this.varianceTalentDist = varianceTalentDist;
		this.grid = grid;
		
	}
	

} // End class
