package aBMFinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;

/** The class Firms is used for all firms in the model. 
 * 	@author Hackett, Avalos, Morales 
 */
public class Firms {
	
	// Instance variables
	
/** 
 * Gives the minimum value a worker must have in order to work at a given firm.
 */
	double minVal;
	
/**
 * Parameter that measures the importance of the network to firm evaluation.
 */
	static double networkImportance;
	
/**
 * List of salaries paid by the firms to its workers.
 */
	ArrayList<Double> allSalaries = new ArrayList<Double>();
	
/**
 * markUp is what the firm offers as a mark up on the talent that a candidate is believed to have. 
 */
	double markUp;
	
/**
 * Lists former employees of the firm in order to form a basis for relevant net-works
 * in hiring.
 */
	ArrayList<Individual> pastWorkers;
	
/**
 * Lists current employees of the firm in order to form a basis for relevant net-works
 * in hiring.
 */	
	ArrayList<Individual> currentWorkers;
	
/**
 * Connects universities to firms through static relationships.
 */
	ArrayList<University> contacts;
	
	public Grid grid;
	
/**
 * allFirms groups all the firms into an ArrayList
 */
	public ArrayList<Firms> allFirms = new ArrayList<Firms>();
	
	
/** Constructor of the class Firms.
 * @param grid
 *	@param markUp mark up on the believed talent that the firm offers to its employees
 */
	public Firms (Grid<Object> grid, double markUp) {
		this.grid = grid;
		this.markUp = markUp;
		this.pastWorkers = new ArrayList<Individual>();
		this.contacts = new ArrayList<University>();
		this.currentWorkers = new ArrayList<Individual>();
		allFirms.add(this);
	}
	
	
	
	
	// Methods
	
/**
 * Returns the number of employees that a prospective employee "knows" at the firm. 
 * @return float
 */
	public float numberAcquaintance(Individual i) {
		int counter=0;
		for(Individual j: i.socialNetwork) {
			if(j.parentEmployer==this) {
				counter++;
			}
		} // End for
		
		return counter;
	}
	
	
/**
 * Assigns a numeric value to a prospective employee based on expected talent and connections.
 * This number is used to rank and hire employees.
 * Note: valuation is done in reverse numbers (most valuable worker has the smallest value) so to make easier the sorting of workers.
 * @return double
 */
	public double valIndividual(Individual i) {
		if(i.parentEmployer == null) {
			return -(i.talent - networkImportance*Math.abs(RandomHelper.nextDouble()*i.almaMater.varianceTalentDist));
		}
		else {
			return  -(i.talent - networkImportance*Math.abs(RandomHelper.nextDouble()*(1 - numberAcquaintance(i)/pastWorkers.size() )*i.almaMater.varianceTalentDist));
		}
	} // End valIndividual


/**
 * This method is used to sort, in descending order, a HashMap according to its (numeric) values.
 * @return LinkedHashMap
 */
	public LinkedHashMap<Individual, Double> sortHashMapByValues(
	        HashMap<Individual, Double> passedMap) {
	    List<Individual> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Double> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues);
	    // Collections.sort(mapKeys);

	    LinkedHashMap<Individual, Double> sortedMap =
	        new LinkedHashMap<>();

	    Iterator<Double> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Double val = valueIt.next();
	        Iterator<Individual> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            Individual key = keyIt.next();
	            Double comp1 = passedMap.get(key);
	            Double comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	} // End sortHashMap
	
	
	

/**
 * This method searches over workers and chooses the employees based on their value from valIndividual.
 * The available prospects with the highest values are chosen, then taken out from consideration
 * for the rest of the firms hiring process.
 */
	@ScheduledMethod(start=1, interval=1, shuffle=false, priority=80)
	public void hireWorkers() {
		//ArrayList<Individual> remainingProspects= new ArrayList<Individual>();
		HashMap<Individual, Double> sortedWorkers = new HashMap<Individual, Double>();
		for(Object i: grid.getObjects()) {
			if(i.getClass() == Individual.class) {
				if(((Individual) i).currentEmployer == null) {
					sortedWorkers.put((Individual) i, valIndividual((Individual) i));
				}
			}
		} // End for
		
		sortedWorkers = sortHashMapByValues(sortedWorkers);
		int z=0;
		for(HashMap.Entry<Individual, Double> entry: sortedWorkers.entrySet()) {
			this.currentWorkers.add(entry.getKey());
			entry.getKey().currentEmployer = this;
			z++;
			if(z==50) {
				break;
			}
			
		}
	} // End method
	
	
/**
 * This method adds the mark up times 1000 onto each worker's believed talent to its wealth.
 * Note: since valIndividual was made negative to sort workers, we take minus this values.
 * Note: We multiply the mark up times 1000 to make it relevant in terms of average wealth.
 */
	@ScheduledMethod(start=1, interval=1, shuffle=false, priority=75)
	public void paySalary() {
		for(Individual i: this.currentWorkers) {
			i.wealth = i.wealth - 1000*valIndividual(i)*markUp;
			i.salary  = -1000*valIndividual(i)*markUp;
			allSalaries.add(i.salary);
		}
		
	}

/**
 * This method computes the average salaries paid by a firm to its workers
 * @return double
 */
	public double averageSalariesFirm() {
		double averageSalaries = 0;
		for(int i=0; i<allSalaries.size(); i++) {
			averageSalaries += allSalaries.get(i);
		}
		averageSalaries = averageSalaries / allSalaries.size();
		return averageSalaries;
	}

	
/**
 * This method first clears the list of past workers of the firm (they retire),
 * then current workers are moved onto past workers and we clear current workers
 * to make space for the next generation of employees.
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=1)
	public void fireAll() {
		this.pastWorkers.clear();
		
		for(int i=0; i<this.currentWorkers.size();i++) {
			this.pastWorkers.add(this.currentWorkers.get(i));
		}
		
		this.currentWorkers.clear();
	}
	
	
} // End class
