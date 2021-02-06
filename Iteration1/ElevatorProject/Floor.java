
package ElevatorProject;

import java.io.File;
import java.util.ArrayList;


/**
 * @author rutvikshah
 *
 */

import java.util.Scanner;

public class Floor implements Runnable{
	private Scheduler scheduler;
	private int floorLevel;
	private int floorButton;
	private Information.directionLamp dirLamp = Information.directionLamp.NOT_PRESSED;
	private File inputFile = new File(System.getProperty("user.dir") + "/Iteration1/ElevatorProject/floorRequest.txt");
	private ArrayList<String >requestList;
	
	public Floor (Scheduler scheduler) {
		this.scheduler = scheduler;
		requestList = getRequestFromFile(inputFile);
	}
	
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
	
	public void requestHandler(String request) {
		synchronized (this.scheduler) {
			scheduler.putRequest(request.getBytes());
			System.out.println("Floor Subsystem: sent request to scheduler");
			String[] strAck;
			
			do{
				strAck = (new String(scheduler.getAcknowledgemnt())).split(" ");
			}while(!"ACK".equals(strAck[0]));
			
			System.out.println("Floor SubSystem: " + strAck[0] + " recieved, Elevator " + strAck[1] + " is on it's way!\n");
			
		}
	}
	
	@Override
	public synchronized void run() {

		while(true) {
			requestHandler(requestList.remove(0));
			
			if(requestList.isEmpty()) {System.exit(0);}
			
		}
	}
}

