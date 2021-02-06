/**
 * Floor.java
 * 
 * The Floor thread will read in events from an input file and will try to put a request 
 * in the scheduler while the previous request has been acknowledged.
 *
 * @author rutvikshah
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Floor implements Runnable{
	
	//private final Integer NUMBER_OF_FLOORS = 7;
	private Scheduler scheduler;
	private int floorLevel;
	private int floorButton;
	private Information.directionLamp dirLamp = Information.directionLamp.NOT_PRESSED;
	private File inputFile = new File(System.getProperty("user.dir") + "/Iteration1/ElevatorProject/floorRequest.txt");
	private ArrayList<String> requestList;
	
	/**
	 * Constructor class used to initialize the object of the Floor class.
	 * 
	 * @param scheduler		the schedule where the actions of the floor are passed to
	 */
	public Floor (Scheduler scheduler) {
		this.scheduler = scheduler;
		requestList = getRequestFromFile(inputFile);
	}
	
	/**
	 * 
	 * 
	 * @param file
	 */
	@SuppressWarnings({ "resource", "unused" })
	public ArrayList<String> getRequestFromFile(File file){
		ArrayList<String >requestList = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(inputFile);
			String line = scanner.nextLine();
			while(scanner.hasNextLine()) {
				requestList.add(scanner.nextLine());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		
		return requestList;
	}
	
	/**
	 * 
	 * 
	 * @param request
	 */
	public void requestHandler(String request) {
		synchronized (this.scheduler) {
			scheduler.putRequest(request.getBytes());
			System.out.println("Floor Subsystem: sent request to scheduler");
			String[] strAck;
			
			do{
				strAck = (new String(scheduler.getAcknowledgemnt())).split(" ");
			}while(!"ACK".equals(strAck[0]));
			
			System.out.println("Floor System: " + strAck[0] + " recieved, Elevator " + strAck[1] + " is on it's way!\n");
			
		}
	}
	
	/**
	 * Overrides the run method of the Runnable interface.
	 */	
	@Override
	public synchronized void run() {

		while(true) {
			requestHandler(requestList.remove(0));
			
			if(requestList.isEmpty()) {System.exit(0);}
			
		}
	}
}

