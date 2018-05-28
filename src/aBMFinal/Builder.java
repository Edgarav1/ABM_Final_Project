package aBMFinal;

import java.util.ArrayList;

import com.sun.medialib.codec.jp2k.Params;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class Builder implements ContextBuilder<Object> {
	
	public Context build(Context<Object> context) {
		context.setId("ABMFinal");
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, 50,50));
		
		
		for(int i=0; i<1000; i++) {
			context.add(new Individual(grid));
		}

		int scenario=1;		// We can change the characteristics of the universities by changing this value.
		
		// Add universities
		//(minTalent, minWealth, meanTalentDist, varianceTalentdist)
		
		switch(scenario) {

		case 1: // Original
			context.add(new University(grid, 0, 0, 0, 10)); // IPN
			context.add(new University(grid, 50, 0, 50, 50)); // UNAM
			context.add(new University(grid, 80, 8000, 51, 10)); // ITAM
			context.add(new University(grid, 30, 8000, 10, 10)); // Anahuac
			context.add(new University(grid, 80, 5000, 30, 20)); // Ibero
			break;
		
		case 2: // Less spread in talent distribution
			context.add(new University(grid, 0, 0, 10, 10)); // IPN
			context.add(new University(grid, 50, 0, 35, 50)); // UNAM
			context.add(new University(grid, 80, 8000, 40, 10)); // ITAM
			context.add(new University(grid, 30, 8000, 15, 10)); // Anahuac
			context.add(new University(grid, 80, 5000, 30, 20)); // Ibero
			break;
	
		case 3: // Lower talent barriers
			context.add(new University(grid, 0, 0, 0, 10)); // IPN
			context.add(new University(grid, 40, 0, 50, 50)); // UNAM
			context.add(new University(grid, 60, 8000, 51, 10)); // ITAM
			context.add(new University(grid, 30, 8000, 10, 10)); // Anahuac
			context.add(new University(grid, 80, 5000, 30, 20)); // Ibero
			break;
		
		case 4: // Both
			context.add(new University(grid, 0, 0, 10, 10)); // IPN
			context.add(new University(grid, 40, 0, 35, 50)); // UNAM
			context.add(new University(grid, 60, 8000, 40, 10)); // ITAM
			context.add(new University(grid, 30, 8000, 15, 10)); // Anahuac
			context.add(new University(grid, 80, 5000, 30, 20)); // Ibero
			break;
			
		default:
			break;
		} // End switch

		
		// Add firms
		for(int i=0; i<20; i++) {
			context.add(new Firms(grid, 2-0.05*i));
		}
		
		
		// Define scalars and parameters
		Individual.meanTalent=0;
		Individual.sumWealth=0;
		
		Parameters params = RunEnvironment.getInstance().getParameters();

		Firms.networkImportance = params.getDouble("networkImportance");
		
		
		// Define batch parameters
		if(RunEnvironment.getInstance().isBatch()) {
			
			RunEnvironment.getInstance().endAt(10);
		}
		else {
			RunEnvironment.getInstance().pauseAt(10);
		}
		
		
		return context;
		
	} // End main
	
	

} // End class