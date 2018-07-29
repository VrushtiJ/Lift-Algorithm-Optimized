package lift.algo;

import java.util.TreeSet;

public class LiftProperties {


	TreeSet<Integer> floorSet;
	LiftStatus status;
	public LiftProperties()
	{

		floorSet = new TreeSet<>();
		status = LiftStatus.STILL;
	}


}
