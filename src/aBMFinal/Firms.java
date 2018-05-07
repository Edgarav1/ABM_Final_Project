package aBMFinal;

import java.util.ArrayList;
import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;

public class Firms {
	
	// Instance variables
	double minVal;
	double salary;
	
	ArrayList<Individual> pastWorkers;
	ArrayList<University> contacts;
	
	public Grid grid;
	
	// Contructor
	public Firms (Grid<Object> grid, double salary) {
		this.grid = grid;
		this.salary = salary;
	}
	
	// Methods
	
	public float numberAcquaintance(Individual i) {
		int counter=0;
		for(Individual j: i.socialNetwork) {
			if(j.parentEmployer==this) {
				counter++;
			}
		} // End for
		
		return counter;
	}
	
	public double valIndividual(Individual i) {
		if(i.parentEmployer == null) {
			return i.talent - RandomHelper.nextDouble()*i.almaMater.varianceTalentDist;
		}
		else {
			return  i.talent - RandomHelper.nextDouble()*(1 - numberAcquaintance(i)/pastWorkers.size() )*i.almaMater.varianceTalentDist;
		}
	} // End valIndividual
	
	@ScheduledMethod(start=1, interval=1, shuffle=false, priority=80)
	public void hireWorkers() {
		ArrayList<Individual> remainingProspects= new ArrayList<Individual>(); 
		for(Object i: grid.getObjects()) {
			if(i.getClass() == Individual.class) {
				
			}
		}
	}
}
