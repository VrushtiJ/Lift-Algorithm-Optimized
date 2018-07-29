package lift.algo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBException;

public class LiftAlgorithmMain {

	public static void main(String[] args) throws Exception, JAXBException {

		List<LiftProperties> lifts = new ArrayList<>();
		//ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 3, 3000, TimeUnit.SECONDS, null);// 
		ExecutorService executorService= Executors.newFixedThreadPool(3);
		Scanner scan = new Scanner(System.in);
		for(int i=0;i<3;i++)
		{
			LiftProperties obj = new LiftProperties();
			lifts.add(obj);
		}
		while(true)
		{
			if(scan.hasNext())
			{
				System.out.println(" Input is found");
				String[] str = scan.nextLine().split(" ");
				int liftNo = Integer.parseInt(str[0]);
				LiftStatus action = LiftStatus.valueOf(str[1]);

				System.out.println();
				LiftProperties toBeUsedLift = findToBeUsedLift(lifts, liftNo, action);
				toBeUsedLift.floorSet.add(liftNo);
				toBeUsedLift.status = action;
			}
			System.out.println("Lift size ==========="+lifts.size());
			Iterator<LiftProperties> itr = (Iterator<LiftProperties>) lifts.iterator(); 
			while(itr.hasNext())
			{
				LiftProperties l = itr.next();
				Runnable liftJob = new Runnable() {

					@Override
					public void run() {
						try {
							System.out.println(this.hashCode()+" is running.. Lift "+l.hashCode()+" status"+l.status);
							if(LiftStatus.DOWN.equals(l.status))
							{
								for(int i : l.floorSet.descendingSet())
								{
									System.out.println("Passing "+i+"  floor.........................");
									System.out.println("HASHCODE of the lift "+ l.hashCode()+" -- Current floor is "+i+" Going "+l.status);
									l.floorSet.remove(i);
									Thread.sleep(10000);
								}
							}
							else if(LiftStatus.UP.equals(l.status))
							{
								for(int i : l.floorSet)
								{
									System.out.println("Passing "+i+"  floor.........................");
									System.out.println("HASHCODE of the lift "+ l.hashCode()+" -- Current floor is "+i+" Going "+l.status);
									l.floorSet.remove(i);
									Thread.sleep(10000);
								}
							}
							else
							{
								System.out.println(" Lift "+l.hashCode()+" status"+l.status);
							}
							l.status=LiftStatus.STILL;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				executorService.execute(liftJob);
			}
			//	}
			System.out.println("No input is found");
		}

	}

	public static LiftProperties findToBeUsedLift(List<LiftProperties> lifts, int reqFloor, LiftStatus action)
	{
		LiftProperties toBeUsedLift = new LiftProperties();
		int chosenLiftDist = 100;
		for(LiftProperties l: lifts)
		{
			int cnt=0;
			int minFloorDiff = 100;
			switch(l.status) {
			case DOWN:
			{
				if(LiftStatus.UP.equals(action) || (LiftStatus.DOWN.equals(action) && reqFloor > l.floorSet.last()))
				{
					/**
					 * when the lift is in opposite direction total distance to reach the requested floor would be 
					 *the sum of diff b/w first and last element of queue and diff b/w last and requested floor 
					 */
					cnt=Math.abs(l.floorSet.first()-l.floorSet.last())+Math.abs(reqFloor-l.floorSet.first());
					minFloorDiff = cnt<minFloorDiff? cnt: minFloorDiff;
				}
				else
				{
					if(reqFloor < l.floorSet.last())
					{
						/**
						 * When requested floor and one of the lifts moving downwards and current floor is greater than the requested one,
						 * distance would be diff b/w current and req floor
						 */
						cnt = l.floorSet.last()-reqFloor;
						minFloorDiff = cnt<minFloorDiff? cnt: minFloorDiff;
					}
				}
			}
			break;

			case UP:
			{
				if(LiftStatus.DOWN.equals(action) || (LiftStatus.UP.equals(action) && reqFloor<l.floorSet.first()))
				{
					/**
					 * If lift is moving upwards and requested floor is upwards then sum would be the diff b/w requested floor and destination of list
					 * and diff b/w current and destination floor
					 */
					cnt = Math.abs(l.floorSet.first()-l.floorSet.last())+Math.abs(l.floorSet.last()-reqFloor);
					minFloorDiff = cnt<minFloorDiff? cnt: minFloorDiff;
				}
				else
				{
					if(reqFloor>l.floorSet.first())
					{
						/**
						 * If lift and requested floor moving upwards and reqfloor is higher than the current floor then floor diatcnes woudl be 
						 * diff b/w destination of floor and reque floor
						 */
						cnt = reqFloor-l.floorSet.first();
						minFloorDiff = cnt<minFloorDiff? cnt: minFloorDiff;
					}
				}
			}
			break;

			case STILL:
			{
				minFloorDiff = reqFloor<minFloorDiff? reqFloor: minFloorDiff;
				break;
			}
			}
			if(chosenLiftDist>minFloorDiff)
			{
				chosenLiftDist = minFloorDiff;
				toBeUsedLift = l;
			}
		}
		toBeUsedLift.floorSet.add(reqFloor);
		return toBeUsedLift;

	}
}

