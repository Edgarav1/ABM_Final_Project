package aBMFinal;

import java.util.ArrayList;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

/** 
 * Class for all individuals which study, then work.
 * @author Hackett, Avalos, Morales
 */
public class Individual {

	// Instance Variables
	
/** 
 * Gives the proportion of an individuals characteristics which are passed onto the next generation; wealth, talent, network.
 */
	static double propInheritance;
	
/*
 * Salary paid to the individual in that period. 
 */
	double salary;
	
/**
 * Marks where in the distribution the individual begins his or her life.
 */
	int distributionStart;
	
/**
 * Marks where in the distribution the individual ends his or her life. 
 */
	int distributionEnd;
	
/**
 * This variable is used to compute the mean of the wealth distribution
 * among individuals.
 */
	static double sumWealth;
	
/** 
 * Risk aversion in choosing universities: Describes the amount which individuals discount a university's mean talent 
 * by its variance.
 */
	static final double riskAversion=0;
	
/**
 * This variable is used to compute the mean of the talent distribution
 * among individuals.
*/
	static double meanTalent;
	
/** 
 * Gives the wealth of the individual; at the start of his/her life wealth is an inherited proportion of their parent's wealth,
 * which then is modified by the salary obtained after college.
 */
	double wealth;
	
/** 
 * Talent is an exogenous value inherited partially from parents which is then modified by attending university.
 */
	double talent;
	
/** 
 * Records the university which an individual attended.
 */
	University almaMater;
	
/** 
 * The social network comprises of friends that an individual made in college which later serve to provide valuable connections 
 * in the hiring process.
 */
	ArrayList<Individual> socialNetwork;
	
/**
 * The parent's employer serves as the link of value between friendships made in college and connections that help an individual to be hired
 * by higher paying firms in the job search. It is the link between the working generation and the generation searching for work.
 */
	Firms parentEmployer;
	
/**
 * Records the firm which hires an individual, and serves at the relevant network for the next generation when they are looking for
 * advantageous contacts.
 */
	Firms currentEmployer;
	
	public Grid grid;
	
/**
 * Constructor of the individual class.
 */
	public Individual(Grid<Object> grid) {
		RandomHelper.createChiSquare(2);
		this.wealth = 5000*RandomHelper.getChiSquare().nextDouble();	// TODO: We are also moving the variance by a lot... Is that ok?
		// this.wealth = RandomHelper.nextDoubleFromTo(0, 10000);	//
		
		RandomHelper.createChiSquare(3);
		this.talent = 50*RandomHelper.getChiSquare().nextDouble()/3;
		// this.talent = RandomHelper.nextDoubleFromTo(0, 100);	// TODO Pareto
		
		this.grid = grid;
		
		this.socialNetwork = new ArrayList<Individual>();
		
	}
	
	
	// Methods

/**
 * Valuation of a potential alma mater. The individual evaluates potential schools based on the mean talent that they give to alumni
 * discounted by their risk aversion multiplied by the variance of that talent distribution.
 * @return double
 */
	public double valUniversity(University u) {
		return u.meanTalentDist - riskAversion*u.varianceTalentDist;
	}
	
/**
 * This method resets, each period, the variables that keep track of the talent and wealth distributions means.
 * @return void
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=1000)
	public void resetMeanTalentAndWealth() {
		Individual.meanTalent = 0;
		Individual.sumWealth = 0;
	}
	
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=210)
	public void sumWealthStart() {
		Individual.sumWealth = Individual.sumWealth * this.wealth;
	}
	
/**
 * This method computes if an individual is below or above the wealth mean at the start of each period.
 * @return void
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=200)
	public void distributionMarker() {
		if(this.wealth < Individual.sumWealth/1000) {
			distributionStart=0;
		}
		else {
			distributionStart=1;
		}
/*
		double wealthStartMean = 0;
		Context myContext = ContextUtils.getContext(this);
		
		for(Object i:myContext) {
			if(i instanceof Individual){
				wealthStartMean = wealthStartMean + ((Individual) i).wealth;
			}
		}
		wealthStartMean = wealthStartMean / 1000;
		if(this.wealth < wealthStartMean) {
		distributionStart = 1; 
		} else {
			distributionStart = 0;
		}
*/
	} // end method
	
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=190)
	public void resetSumWealthStart() {
		Individual.sumWealth = 0;
	}
	
/**
 * This method computes if an individual is below or above the wealth mean at the end of each period.
 * @return void
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=15)
	public void distributionEnder() {
		if(this.wealth<Individual.sumWealth/1000) {
			this.distributionEnd = 0;
		} else {
			this.distributionEnd = 1;
		}
	}
	
/**
 * This method returns the value 0 if and individual is below the wealth distribution at the start
 * of the period or 1 otherwise.
 * @return int
 */
	public int getDistEnd() {
		return this.distributionEnd;
	}
	
/**
 * This method returns the value 0 if and individual is below the wealth distribution at the end
 * of the period or 1 otherwise.
 * @return int
 */
	public int getDistStart() {
		return this.distributionStart;
	}
	
/**
 * After evaluating the universities, this method allows an individual to choose a university based on the maximum valuation.
 * Then the individual adds the university as their alma mater and the university adds the individual as an alumni. 
 * @return University
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=110)
	public University chooseUniversity() {
		University choice = null;
		Context myContext = ContextUtils.getContext(this);
		
		for(Object u:myContext) {
			if (u instanceof University) {
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
		
	}// End method
	
/*
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
*/
	
	
/**
 * Bestows updated talent on the individual based on their choice of university. The individual is given a random draw from the distribution
 * of their university which is added to their initial talent.
 * @return void
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=100)
	public void gainTalent() {
		this.talent += RandomHelper.createNormal(almaMater.meanTalentDist, almaMater.varianceTalentDist).nextDouble();
		Individual.meanTalent = Individual.meanTalent + this.talent;
	}

	
/**
 * This method computes the talent distribution mean.
 * @return double
 */
	public double computeMeanTalent() {
		double meanTalent = 0;
		Context myContext = ContextUtils.getContext(this);
		
		for(Object u:myContext) {
			if (u instanceof Individual) {
				meanTalent = meanTalent + ((Individual) u).talent;
			}
		}
		
		meanTalent = meanTalent/1000;
		
		return meanTalent;
	}
	
	
/**
 * This method computes the mean of salaries paid by all firms.
 * @return double
 */
	public double computeMeanSalaries() {
		double meanSalary = 0;
		Context myContext = ContextUtils.getContext(this);
		
		for(Object m:myContext) {
			if(m instanceof Firms) {
				meanSalary = meanSalary + ((Firms) m).averageSalariesFirm();
			} // end if that searches for firms
		} // end for looping over objects
		
		meanSalary = meanSalary/20;
		
		return meanSalary;
		
	}
	
/**
 * This method is used to compute the sum of all individuals' wealth and then this value
 * is used to compute the mean of the wealth distribution.
 * @return void
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=72)
	public void sumWealth() {
		Individual.sumWealth = Individual.sumWealth + this.wealth;
		System.out.printf("%s", sumWealth);
	}
	
/**
 * This method represents the passing of an Individual characteristics onto her offspring.
 * First, we clear an individual alma mater an her social network of friends.
 * Then, we move the individual current employer onto former employer and empty current employer.
 * Afterwards, the individual inherits part of her wealth and talent to her offspring.
 * Note: the values are modifies so that the mean of the distribution of wealth and talent remain
 * the same as in the initialization. Also, if either wealth or talent happen to take on negative
 * values, we change this and set them to zero.
 * @return void
 */
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=10)
	public void death() {
		System.out.printf("%s, %s %n", this.wealth, this.talent);
		
		this.almaMater = null;
		this.socialNetwork.clear();
		this.parentEmployer = this.currentEmployer;
		this.currentEmployer = null;
		
		// this.wealth *= Individual.propInheritance;
		this.wealth = this.wealth*5000*1000/Individual.sumWealth;
	
		this.talent = this.talent*1000*50/Individual.meanTalent;
		//Individual.meanTalent=0;
		
		//this.talent = this.talent*50/computeMeanTalent();
		
		System.out.printf("%s%n", Individual.meanTalent);
		
		System.out.printf("%s, %s %n", this.wealth, this.talent);
		
		if(this.wealth<0) {
			this.wealth=0;
		}
		
		if(this.talent<0) {
			this.talent=0;
		}
		
		System.out.printf("%s, %s %n%n", this.wealth, this.talent);
		

	}
	
/*
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=100)
	public void gainTalent() {
		University choice = chooseUniversity();
		RandomHelper.createNormal(almaMater.meanTalentDist, almaMater.varianceTalentDist);
		this.talent += RandomHelper.getNormal().nextDouble();
		// this.talent += random.nextGaussian()*choice.varianceTalentDist + choice.meanTalentDist;
	}
*/
	
	
/*
	@ScheduledMethod(start=1, interval=1, shuffle=true, priority=90)
	public void makeFriends() {
		for(Individual i:this.almaMater.alumni) {
			if(RandomHelper.nextDouble() <= University.probFriend) {
				this.socialNetwork.add(i);
			}
		}
	}
*/

/**
 * This method gets the individual's (true) talent.
 * @return double
 */
	public double getTalent() {
		return this.talent;
	}
	

/**
 * This method gets the individual's wealth.
 * @return double
 */
	public double getWealth() {
		return this.wealth;
	}


/**
 * This method gets the individual's salary.
 * @return double
 */
	public double getSalary() {
		return this.salary;
	}


} // End class
