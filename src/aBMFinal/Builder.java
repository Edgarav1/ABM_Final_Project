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
/*
		for(int i=0; i<10; i++) {
			context.add(new University(grid));
		}
*/
		 //(minTalent, minWealth, meanTalentDist, varianceTalentdist)
		
		context.add(new University(grid, 0, 0, 0, 10)); // IPN
		
		context.add(new University(grid, 50, 0, 50, 50)); // UNAM
		
		context.add(new University(grid, 80, 8000, 50, 10)); // ITAM
		
		context.add(new University(grid, 30, 8000, 10, 10)); // Anahuac
		
		context.add(new University(grid, 80, 5000, 30, 20)); // Ibero

/*
		ArrayList<University> Campuses = new ArrayList<University>();
		
		University IPN = new University(grid, 0, 0, 0, 10);
		Campuses.add(IPN);
		
		University UNAM = new University(grid, 20, 0, 50, 50);
		Campuses.add(UNAM);
		
		University ITAM = new University(grid, 40, 80, 50, 10);
		Campuses.add(ITAM);
		
		University Anahuac = new University(grid, 10, 80, 10, 10);
		Campuses.add(Anahuac);
		
		University Ibero = new University(grid, 40, 50, 30, 20);
		Campuses.add(Ibero);
		
		
*/
		for(int i=0; i<20; i++) {
			context.add(new Firms(grid, 2-0.05*i));
		}
		
		Individual.propInheritance=21;
		Individual.propInheritance /= 40;
		
		Individual.meanTalent=0;
		Individual.sumWealth=0;
		
		Parameters params = RunEnvironment.getInstance().getParameters();

		Firms.networkImportance = params.getDouble("networkImportance");
		
		if(RunEnvironment.getInstance().isBatch()) {
			
			RunEnvironment.getInstance().endAt(100);
		}
		else {
			RunEnvironment.getInstance().pauseAt(10);
		}
		
		
		return context;
		
	}
	
	

	

} // End class