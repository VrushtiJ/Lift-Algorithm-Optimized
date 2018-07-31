
import java.util.TreeSet;

public class LiftProperties {


	TreeSet<Integer> floorSet;
	LiftStatus status;
	Role role;
	public LiftProperties()
	{

		floorSet = new TreeSet<>();
		status = LiftStatus.STILL;
	}
	
	public LiftProperties(Role r)
	{

		floorSet = new TreeSet<>();
		status = LiftStatus.STILL;
		this.role = r;
	}


}
