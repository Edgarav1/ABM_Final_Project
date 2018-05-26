package aBMFinal;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

/**
 * University class generates all universities in the model which accept students based on minimum wealth and talent requirements.
 * @author Hackett, Avalos, Morales
 */
public class University {

/**
 * Minimum talent required to attend a university. Indicates academic level or rigor.
 */
	final double minTalent;

/**
 * Minimum wealth required to attend a university. Indicates tuition costs.
 */
	final double minWealth;
	
/**
 * Gives the probability of making friends. A prob. that is constant for all schools implies that more friends will be made at bigger schools.
 */
	static final double probFriend = 0.5;
	
/**
 * The mean of talent that a university gives its students.
 */
	final double meanTalentDist;
	
/**
 * Variance in the talent that a university gives its students.
 */
	final double varianceTalentDist;
	
/**
 * List of individuals that have attended that school.
 */
	ArrayList<Individual> alumni;
	
	public Grid grid;
	

/**
 * Constructor for the class University.
 * @param minTalent minimum talent required to attend
 * @param minWealth minimum wealth required to attend
 * @param meanTalentDist average talent given to students who attend 
 * @param varianceTalentDist variance in the talent given to students who attend
 */
	public University(Grid<Object> grid, double minTalent, double minWealth, double meanTalentDist, double varianceTalentDist) {
		this.minTalent = minTalent;
		this.minWealth = minWealth;
		this.meanTalentDist = meanTalentDist;
		this.varianceTalentDist = varianceTalentDist;
		this.grid = grid;
		this.alumni = new ArrayList<Individual>();
		
	}
	
	
/**
 * The individual makes friends at university, which serve as valuable connections based on their parent's current employer
 * in the job search. Based on the probability of making friends a number of individuals are
 * added to the social network as friends.
 * Note: all friendships are reciprocal.
 * @return void
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=90)
	public void makeFriends() {
		for(int i=0; i < this.alumni.size()-1;i++) {
			this.alumni.get(i).socialNetwork.add(this.alumni.get(i));
			
			for(int j=i+1; j < this.alumni.size();j++) {
				if(RandomHelper.nextDouble() <= University.probFriend) {
					this.alumni.get(j).socialNetwork.add(this.alumni.get(i));
					this.alumni.get(i).socialNetwork.add(this.alumni.get(j));
				}
			}
		}
	}
	
/**
 * The university graduates its students and clears the alumni list to make space
 * for the next generation.
 * @return void	
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=5)
	public void graduation() {
		this.alumni.clear();
	}
/**
 * This method calculates the number of students enrolled in the university
 * @return int
 */
	public int numberAlumni() {
		return this.alumni.size(); 
	}
	
	

} // End class
