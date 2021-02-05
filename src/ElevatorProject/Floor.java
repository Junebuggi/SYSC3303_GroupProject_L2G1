
package ElevatorProject;

import java.io.File;

/**
 * @author rutvikshah
 *
 */

import java.util.Scanner;

public class Floor implements Runnable{
	//private final Integer NUMBER_OF_FLOORS = 7;
	private Scheduler scheduler;

	
	public Floor (Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	@SuppressWarnings({ "resource", "unused" })
	@Override
	public synchronized void run() {
		//find the path for the input file
		String dir = System.getProperty("user.dir");
		File inputFile = new File(dir + "/src/floorRequest.txt");
		try {
			Scanner scanner = new Scanner(inputFile);
			String line = scanner.nextLine();
			while(scanner.hasNextLine() ) {
				byte[] floorRequest = scanner.nextLine().getBytes();
				synchronized (this.scheduler) {
					scheduler.putRequest(floorRequest);
					System.out.println("sent request to scheduler");

				}
				while(!scheduler.getAcknowledgemnt()) {
					
				}
				System.out.println("Elevator is on it's way!");
			}
			//send requests to the elevator via scheduler	
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}

