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
@author Hackett, Avalos, Morales */
public class Firms {
	
	// Instance variables
	/** Gives the minimum value a worker must have in order to work at a given firm */
	double minVal;
	/** Salary offered by the firm; salary is unique to the firm */
	double salary;
	/** Lists former employees of the firm in order to form a basis for relevant net-works
	in hiring */
	ArrayList<Individual> pastWorkers;
	/** Connects universities to firms through static relationships. */
	ArrayList<University> contacts;
	
	public Grid grid;
	
	/** Constructor of the class Firms.
	@param salary Salary offered by the firm */
	public Firms (Grid<Object> grid, double salary) {
		this.grid = grid;
		this.salary = salary;
	}
	
	// Methods
	/** Returns the number of employees that a prospective employee "knows" at the firm 
	@return float */
	public float numberAcquaintance(Individual i) {
		int counter=0;
		for(Individual j: i.socialNetwork) {
			if(j.parentEmployer==this) {
				counter++;
			}
		} // End for
		
		return counter;
	}
	/** Assigns a numeric value to a prospective employee based on expected talent and connections. 
	This number is used to rank and hire employees. 
	@return double */
	public double valIndividual(Individual i) {
		if(i.parentEmployer == null) {
			return i.talent - RandomHelper.nextDouble()*i.almaMater.varianceTalentDist;
		}
		else {
			return  i.talent - RandomHelper.nextDouble()*(1 - numberAcquaintance(i)/pastWorkers.size() )*i.almaMater.varianceTalentDist;
		}
	} // End valIndividual

	
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
	
	
	

	/** Hires workers. This method searches over workers and chooses the employees based on their value from valIndividual. 
	The available prospects with the highest values are chosen, then taken out from consideration
	for the rest of the firms hiring process. 
	@return void */
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
			pastWorkers.add(entry.getKey());
			z++;
			if(z==50) {
				break;
			}
			
			for(HashMap.Entry<Individual, Double> entry2: sortedWorkers.entrySet()) {
				System.out.println(entry2.getKey() + "/" + entry2.getValue());
			}
		}
		
	}
	
	@ScheduledMethod(start=1, interval=1, shuffle=false, priority=20)
	public void printHiredWorkers() {
		for (Individual i: pastWorkers) {
			System.out.printf("%s", valIndividual(i));
		}
	}
	
	
	
} // End class
