package aBMFinal;

import java.util.ArrayList;

import repast.simphony.space.grid.Grid;
/** University class generates all universities in the model which accept students based on minimum wealth and talent requirements */
public class University {

	/** Talento minimo que se necesita para acceder a la universidad */
	double minTalent;
	/** Minimum talent required to attend a university. Indicated academic level or rigor. */
	double minWealth;
	/** Gives the probablity of making friends. A prob. that is constant for all schools implies that more friends will be made at bigger schools. */
	static double probFriend = 0.5;
	/** The mean of talent that a university gives its students.*/
	double meanTalentDist;
	/** Variance in the talent that a university gives its students */
	double varianceTalentDist;
	/** Alumni; individuals that have attended that school. */
	ArrayList<Individual> alumni;
	
	public Grid grid;
	
	// Constructor
	/** Constructor for the class University
	@param minTalent minimum talent required to attend
	@param minWealth minimum wealth required to attend
	@param meanTalentDist average talent given to students who attend 
	@param varianceTalentDist variance in the talent given to students who attend */
	public University(Grid<Object> grid, double minTalent, double minWealth, double meanTalentDist, double varianceTalentDist) {
		// TODO Auto-generated constructor stub
		this.minTalent = minTalent;
		this.minWealth = minWealth;
		this.meanTalentDist = meanTalentDist;
		this.varianceTalentDist = varianceTalentDist;
		this.grid = grid;
		this.alumni = new ArrayList<Individual>();
		
	}
	

} // End class
