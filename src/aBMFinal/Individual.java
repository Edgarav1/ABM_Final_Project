package aBMFinal;

import java.util.ArrayList;
import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;


/** Class for all individuals which study, then work. 
*/
public class Individual {

	// Instance Variables
	
	/** Gives the proportion of an individuals characteristics which are passed onto the next generation; wealth, talent, network */
	static float propInheritance;
	
	/** Risk aversion in choosing universities: Describes the amount which individuals discount a university's mean talent 
	by its variance */
	static double riskAversion=0;
	
	/** Gives the wealth of the individual; at the start of his/her life wealth is an inherited proportion of their parent's wealth,
	which then is modified by the salary obtained after college. */
	double wealth;
	
	/** Talent is an exogenous value inheritied partially from parents which is then modified by attending university. */
	double talent;
	
	/** Records the university which an individual attended */
	University almaMater;
	
	/** The social network comprises of friends that an individual made in college which later serve to provide valuable connections 
	in the hiring process */
	ArrayList<Individual> socialNetwork;
	
	/** The parent's employer serves as the link of value between friendships made in college and connections that help an individual to be hired 
	by higher paying firms in the job search. It is the link between the working generation and the generation searching for work */
	Firms parentEmployer;
	
	/** Records the firm which hires an individual, and serves at the relevant network for the next generation when they are looking for
	advantageous contacts */
	Firms currentEmployer;
	
	public Grid grid;
	
	/** Constructor of the individual class */
	public Individual(Grid<Object> grid) {
		RandomHelper.createChiSquare(2);
		this.wealth = 5000*RandomHelper.getChiSquare().nextDouble();
		// this.wealth = RandomHelper.nextDoubleFromTo(0, 10000);	//
		
		RandomHelper.createChiSquare(3);
		this.talent = 50*RandomHelper.getChiSquare().nextDouble()/3;
		// this.talent = RandomHelper.nextDoubleFromTo(0, 100);	// TODO Pareto
		
		this.grid = grid;
		
		this.socialNetwork = new ArrayList<Individual>();
		
	}
	
	
	// Methods

	/** Valuation of a potential alma mater. The individual evaluates potential schools based on the mean talent that they give to alumni
	discounted by their risk aversion multiplied by the variance of that talent distribution 
	@return double */
	public double valUniversity(University u) {
		return u.meanTalentDist - riskAversion*u.varianceTalentDist;
	}
	
	
	/** After evaluating the universities, this method allows an individual to choose a university based on the maximum valuation
	Then the individual adds the university as their alma mater and the university adds the individual as an alumni. 
	@return University */
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
	
	
	/** Bestows updated talent on the individual based on their choice of university. The individual is given a random draw from the distribution
	of their university which is added to their initial talent.
	@return void */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=100)
	public void gainTalent() {
		University choice = chooseUniversity();
		RandomHelper.createNormal(almaMater.meanTalentDist, almaMater.varianceTalentDist);
		this.talent += RandomHelper.getNormal().nextDouble();
		// this.talent += random.nextGaussian()*choice.varianceTalentDist + choice.meanTalentDist;
	}
	
	
	/** The individual makes friends at university, which serve as valuable connections based on their parent's current employer 
	in the job search. Based on the probability of making friends and the size of the university, a number of individuals are 
	added to the social network as friends. 
	@return void */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=90)
	public void makeFriends() {
		for(Individual i:this.almaMater.alumni) {
			if(RandomHelper.nextDouble() <= University.probFriend) {
				this.socialNetwork.add(i);
			}
		}
	}


} // End class
