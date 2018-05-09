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
