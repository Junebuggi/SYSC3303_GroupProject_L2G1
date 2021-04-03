/**
 * FloorSubsystem.java
 * 
 * The FloorSubsystem thread will read in events from an input file and will try to put a request 
 * in the scheduler and then peek ahead to the next request and sleep until time elapsed to send 
 * the next floor request to the scheduler
 *
 * @author Emma Boulay [Iteration 3]
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject.FloorSubsystem;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import ElevatorProject.Information;
import ElevatorProject.Network;
import ElevatorProject.Time;
import ElevatorProject.GUI.ElevatorGridGUI;
import ElevatorProject.GUI.Components.FloorButtonsComponent;

/**
 * This is the Floor Subsystem class. This will create all the floors for the
 * subystem to control and will read in all the floorRequests from a file and
 * then send these requests to the scheduler with the appropriate delay between
 * them.
 * 
 * @author Emma Boulay [Iteration 3]
 *
 */
public class FloorSubsystem extends Network implements Runnable {
	// The input file that contains all the floor requests
	private static File inputFile = new File(
			System.getProperty("user.dir") + "/src/ElevatorProject/FloorSubsystem/floorRequest.txt");
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

	private int schedulerPort;
	private int timeout;
	private int nFloors;
	private int nShafts;
	private Floor[] floors;
	protected FloorButtonsComponent[] floorButtonsGUI;

	/**
	 * The constructor method creates a floor subsystem with nFloors and each floor
	 * has nShafts. It will communicate to the scheduler with the specified port and
	 * timeout
	 * 
	 * @param nFloors       The number of floors the the elevator system spans
	 * @param nShafts       The number of elevator shafts
	 * @param schedulerPort The port the scheduler is configured to listen on
	 *                      (Usually 23)
	 * @param timeout       The time in milliseconds, if a packet is not ACK it will
	 *                      send again
	 */
	public FloorSubsystem(int nFloors, int nShafts, int schedulerPort, int timeout) {
		this.schedulerPort = schedulerPort;
		this.timeout = timeout;
		this.nFloors = nFloors;
		this.nShafts = nShafts;
		
		if(Information.gui)
			floorButtonsGUI = ElevatorGridGUI.floorButtons;

	}

	/**
	 * This method creates each floor in the floor subsystem and communicates which
	 * port it is listening on for this session to the scheduler
	 * 
	 * @param nFloors The number of floors the system spans
	 * @param nShafts The number of elevator shafts the system has
	 */
	public void createFloorThreads(int nFloors, int nShafts) {

		floors = new Floor[nFloors];

		for (int i = 0; i < nFloors; i++) {
			floors[i] = new Floor(i + 1, nFloors, nShafts, this.schedulerPort);
			floors[i].setUp(); // Communicate listening port to scheduler
			new Thread(floors[i], "Floor " + (i + 1)).start();
		}
	}

	/**
	 * Method used to parse the request file and store the requests in an array list
	 * (requestList)
	 * 
	 * @param file the input file that contains the list of elevator requests to be
	 *             performed.
	 * @return requestList the arraylist containing all the requests to be performed
	 */
	public ArrayList<String> getRequestFromFile(File file) {
		ArrayList<String> requestList = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(inputFile);

			// Scan each line from the txt file and store it to the requestList
			while (scanner.hasNextLine()) {
				requestList.add(scanner.nextLine());
			}
			
			scanner.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return requestList;
	}

	/**
	 * This method turns on the lamp of the floorButton after being pressed
	 * 
	 * @param request The request read from the input file
	 */
	public void turnOnLamp(String request) {

		String[] reqArr = request.split(" ");
		int floor = Integer.parseInt(reqArr[1]);
		String dir = reqArr[2];
		System.out.println("Elevator Requested at floor " + floor + ", " + dir + " button pressed and is now on");
		floors[floor - 1].turnOnOffLamp(dir, true);
		
		if(Information.gui)
			floorButtonsGUI[floor - 1].pressButton(dir);
	}

	/**
	 * The run method for the floorSubsystem. It will read in the input file and
	 * send them to the scheduler. After sending a request it will turn on the
	 * designate floor direction lamp
	 */
	@Override
	public void run() {
		// Parse input file
		ArrayList<String> requests = getRequestFromFile(inputFile);

		// Instantiate and setup floors
		createFloorThreads(nFloors, nShafts);

		// This lets the scheduler know that all floors have been initialized
		byte[] initMsg = null;
		try {
			initMsg = ("floorInitEnd").getBytes(pac.getEncoding());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		rpc_send(schedulerPort, initMsg, 500);

		// Send the next request in the array
		int i = 1;
		
		Time.startTimeReference = sdf.format(new Date());
		while (!requests.isEmpty()) {
			String curRequest = requests.remove(0);
			turnOnLamp(curRequest); // Button has been pressed, turn on lamp

			// RPC send the floor request to the scheduler from a new socket (which is
			// closed on completion) with a timeout
			try {
				rpc_send(schedulerPort, ("floorRequest " + curRequest).getBytes(pac.getEncoding()), timeout);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println("Elevator Subsystem: Sending request " + i);
			i++;
			// If the last request sent was not the last request sleep until next request is
			// read to be sent
			if (requests.size() > 0) {
				try {
					long now = Time.getMilli(curRequest.split(" ")[0]);
					long nextTime = Time.getMilli(requests.get(0).split(" ")[0]);
					Thread.sleep(Math.abs((int) ((nextTime - now) * (Information.TIME_MULTIPLIER))));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * The main method of FloorSubsystem. It creates the floorSubystem and starts
	 * all threads
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int nFloors = Information.NUM_FLOORS;
		int nShafts = Information.NUM_ELEVATORS;
		int schedulerPort = Information.SCHEDULER_PORT;
		int timeout = 2000; // in milliseconds

		new Thread(new FloorSubsystem(nFloors, nShafts, schedulerPort, timeout), "FloorSubsystem").start();

	}
}
