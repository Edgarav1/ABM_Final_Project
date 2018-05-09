package aBMFinal;

import java.util.ArrayList;
import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;


// Instance Variables
public class Individual {
	static float propInheritance=0;
	static float riskAversion=0;
	
	double wealth;
	double talent;
	University almaMater;
	ArrayList<Individual> socialNetwork;
	
	Firms parentEmployer;
	Firms currentEmployer;
	
	public Grid grid;
	
	// Constructor
	public Individual(Grid<Object> grid) {
		this.wealth = RandomHelper.nextDoubleFromTo(0, 10000);	// TODO chi squared distribution
		this.talent = RandomHelper.nextDoubleFromTo(0, 100);	// TODO Pareto
		this.grid = grid;
		
	}
	
	// Methods
/*	
	public double valUniversity(University u) {
		return u.meanTalentDist - riskAversion*u.varianceTalentDist;
	}
	

	public University chooseUniversity() {
		//TODO search grid only for universities
		University choice=null;
		for(Object u: grid.getObjects()) {
			if (u.getClass() == University.class) { //TODO ask Florian D:
				if(choice == null && this.talent >= ((University) u).minTalent && this.wealth >= ((University) u).minWealth) {
					choice=(University) u;
				}
				else if(this.talent >= ((University) u).minTalent && this.wealth >= ((University) u).minWealth) {
					if(valUniversity(choice) < valUniversity((University) u)) {
						choice = (University) u;
					}
				}
			} // End check class
		} // End for
		
		choice.alumni.add(this);
		this.almaMater = choice;
		
		return choice;
	} // End method
	
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=100)
	public void gainTalent(Random random) {
		University choice = chooseUniversity();
		this.talent += random.nextGaussian()*choice.varianceTalentDist + choice.meanTalentDist;
	}
	
	
	
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=90)
	public void makeFriends() {
		for(Individual i:this.almaMater.alumni) {
			if(RandomHelper.nextDouble() <= University.probFriend) {
				this.socialNetwork.add(i);
			}
		}
	}

*/	
}
