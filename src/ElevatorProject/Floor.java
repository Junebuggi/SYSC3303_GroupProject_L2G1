
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
		File inputFile = new File(dir + "/src/ElevatorProject/floorRequest.txt");
		try {
			Scanner scanner = new Scanner(inputFile);
			String line = scanner.nextLine();
			while(scanner.hasNextLine() ) {
				byte[] floorRequest = scanner.nextLine().getBytes();
				synchronized (this.scheduler) {
					scheduler.putRequest(floorRequest);
					System.out.println("Floor Subsystem: sent request to scheduler");
					
					byte[] ack = scheduler.getAcknowledgemnt();
					
					String strAck[] = (new String(ack)).split(" ");
					System.out.println("Floor System: " + strAck[0] + " recieved, Elevator " + strAck[1] + " is on it's way!\n");

				}
			
			}
			//send requests to the elevator via scheduler	
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}

