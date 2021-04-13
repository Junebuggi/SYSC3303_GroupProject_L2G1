/**
 * Scheduler.java
 * 
 * The Scheduler is responsible for handling incoming requests from the floor Subsystem and passing requests
 * to the elevator subsystem. This is done with the implementation of a state machine. 
 *
 * @version 1.0
 * @author Abeer Rafiq
 * @author Emma Boulay [Iteration 1, 2, 3]
 *
 * (Group 1 - SYSC 3303 L2)
 *
 */
package ElevatorProject.SchedulerSubsystem;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import ElevatorProject.Information;
import ElevatorProject.Network;
import ElevatorProject.Time;
import ElevatorProject.GUI.ElevatorGridGUI;

public class Scheduler extends Network {
	//An array list of all the floor requests that are waiting to be serviced
	private ArrayList<String[]> workRequests = new ArrayList<>(); 
	//Each floor and their corresponding port
	private HashMap<Integer, Integer> floorPorts = new HashMap<Integer, Integer>();
	//-1 if the elevatorPort hasn't been initialized yet
	private int elevatorPort = -1;
	//A mapping between all the elevators by number and their current state(floor and motor direction)
	HashMap<Integer, Elevator> elevators = new HashMap<Integer, Elevator>();
	private int timeout; //The timeout to be used when wait for an ACK
	protected ElevatorFaultMonitor elevatorMonitors[];
	
	/**
	 * A simple class that models an elevator's current floor and motor state
	 * @author Emma Boulay [Iteration 3]
	 *
	 */
	protected class Elevator {
		private String state;
		private int floor;
		/**
		 * The constructor method
		 * @param state //The motor state, "UP", "DOWN", "IDLE" or "OUT_OF_ORDER"
		 * @param floor the current floor of the elevator
		 */
		Elevator(String state, int floor){
			this.state = state;
			this.floor = floor;
		}
		/**
		 * Getter method to return current floor
		 * @return current floor
		 */
		public int getFloor() {
			return this.floor;
		}
		/**
		 * Getter method to return elevator motor state
		 * @return motor state, "UP", "DOWN" or "IDLE"
		 */
		public String getState() {
			return this.state;
		}
		
	}
	
	/**
	 * The default constructor.
	 */
	public Scheduler(String name, int listeningPort, int timeout) { 
		super.name = name;
		super.sockets = new DatagramSocket[1];
		this.timeout = timeout;
		try {
			sockets[0] = new DatagramSocket(listeningPort);
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
		}
	}

	/**
	 * This method puts a floor request into the scheduler. This method will return
	 * when there is space for the floor request in the scheduler.
	 * 
	 * @param elevatorRequests An object representing the elevator request from the floor subsystem
	 */
	public synchronized byte[] putRequest(byte[] request) {
			//Add the work requests passed in and notify all
			String[] data = pac.parseData(request);
			
			//This is an elevatorRequest message
			if(data[0].equals("floorRequest") && (data.length == 5 || data.length == 6)) {
				if(!arrayContainsRequest(data, workRequests)) {
					
					this.workRequests.add(data);
					print("[" + Time.getCurrentTime() + "], SCHEDULER: Received elevator request from Floor " + data[2]);
				}	
			}
			
			//This is a elevator destination floor message
			if(data[0].equals("arrivalSensor") && data.length == 5) {
				
				//Arrival notification arrived, cancel fault timer
				elevatorMonitors[Integer.parseInt(data[3])-1].cancelTimer();
				//If the elevator's state is not IDLE, it's still moving, start fault timer
				print("[" + Time.getCurrentTime() + "], SCHEDULER: Arrival Notification received from Elevator " + data[3] + ", canceling timer.");
				
				if(elevators.get(Integer.parseInt(data[3])).getState().equals("Out_of_Order")) {
					printError("[" + Time.getCurrentTime() + "], SCHEDULER: Elevator Timer previously timed out but Arrival Notification received with delay. Elevator " + data[3] + " is schedulable again.");

				}
				
				
				if(!data[4].equals("IDLE")) {
					print("[" + Time.getCurrentTime() + "], SCHEDULER: Elevator " + data[3] + " moving, starting timer.");
					elevatorMonitors[Integer.parseInt(data[3])-1].startTimer();	
				}
							
				
				arrivalNotificationToFloor(data, Integer.valueOf(data[2]));

				elevators.put(Integer.parseInt(data[3]), new Elevator(data[4], Integer.parseInt(data[3])));
				//Check if a passenger is waiting at this floor on this direction
				String servicableRequest = checkIfRequestPendingAtFloor(Integer.valueOf(data[2]), data[4]);
				if(servicableRequest != null) {		
					//Passenger is waiting!
					print("[" + Time.getCurrentTime() + "], SCHEDULER: Instructing Elevator " + data[3] + " to service request at Floor " + data[2]);
					notifyAll();
					return pac.toBytes(servicableRequest);
				}

			}
			notifyAll();
			return Network.createACK();
	}
	
	/**
	 * This method will check if there is a pending request at the floor with the given direction, if there
	 * are multiple matching pending requests, it will append the passenger's destination.
	 * @param floor the floor the passenger is waiting at
	 * @param direction the direction the passenger wants to go
	 * @return the request if found, null if none found
	 */
	private synchronized String checkIfRequestPendingAtFloor(int floor, String direction) {
		
		//If there is other outstanding requests at the floor, the passengers destination will be tacked on at the end
		boolean foundOne = false;
		String pendingRequest = null;
		int nPassenger = 0;
		for(int i = 0; i < workRequests.size() && nPassenger < 3; i++) {
			if(workRequests.get(i)[3].equals(direction) && Integer.valueOf(workRequests.get(i)[2]) == floor && workRequests.get(i).length == 5) {
				//If this is the first request, add the whole request to the string to be returned
				if(!foundOne) {
					pendingRequest = pac.joinStringArray(workRequests.remove(i));
					foundOne = true;
				} 
				//If there are additional matching requests, add the passengers destination
				else {
					String destination = workRequests.remove(i)[4];
					pendingRequest = pendingRequest + "," + destination;

				}
				i--; //Decrement position to reflect request removed
				nPassenger++;
			}
		}	
		return pendingRequest;	
	}
	
	/**
	 * This method will check if the request has already been added to the array. This is useful
	 * in case the request was sent multiple times by the floor subsystem
	 * 
	 * @param request the request to check against the array
	 * @param allRequests the array to compare with
	 * @return true if array contains request
	 */
	private boolean arrayContainsRequest(String[] request, ArrayList<String[]> allRequests) {
		for( String[] req : allRequests) {
			if(Arrays.equals(request, req))
				return true;
		}
		
		return false;
	}
	
	/**
	 * This synchronized method returns the first floor request (position 0) and removes it from the scheduler. 
	 * 
	 * @return floorRequest	the floor request that should be completed next (the first floor request)
	 */
	public synchronized String[] getRequest() {
		//wait if there are no work requests
		while (!isWork()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		//return the request to be completed next, remove from workRequests ArrayList and notify all
		String[] floorRequest = workRequests.remove(0);
		notifyAll();
		return floorRequest;
	}

	/**
	 * This synchronized method is used to determine if there are any work requests pending.
	 * 
	 * @return false  if there are no work requests pending
	 * @return true   if there are pending work requests
	 */	
	public synchronized boolean isWork() {
		notifyAll();
		return !workRequests.isEmpty();
	}

	/**
	 * This method returns the arrayList containing all the requests.
	 * 
	 * @return workRequests  arrayList containing all requests (type byte[])
	 */	
	public ArrayList<String[]> getAllRequest(){
		return workRequests;
	}
	
	/**
	 * This method will remove request from workRequests at index i
	 * 
	 * @param i The index of the request to be removed
	 */
	public synchronized void removeRequest(int i) {
		workRequests.remove(i);
		notifyAll();
	}
	
	/**
	 * This method returns the port the elevator subsystem is listening on
	 * @return elevator subsystem port
	 */
	public int getElevatorPort() {
		return this.elevatorPort;
	}
	
	/**
	 * This method will wait for floor or elevator initialization messages and store the ports
	 * supplied. It will wait until received all InitEnd messages
	 *  
	 * @param socket the socket to listen on for messages
	 */
	public void setUp(DatagramSocket socket) {
		
			boolean floor = true;
			//Loop on receive until all initialization messages are received
			while(floor || elevatorPort == -1) {
				System.out.println("Listening on port: " + socket.getLocalPort());
				ReturnData portData = receive(socket);
				//Send an ACK after message received
				send(portData.getPort(), createACK(), socket);

				String[] fromSystem = Network.pac.parseData(portData.getData());
				//This is a floorInit message, it provides the floor's number and its listening port
				if(fromSystem[0].equals("floorInit")) {
					System.out.println("Initializing floor " + Integer.parseInt(fromSystem[1]) + " to port: " + Integer.parseInt(fromSystem[2]));
					floorPorts.put(Integer.parseInt(fromSystem[1]), Integer.parseInt(fromSystem[2]));
				}
				//This is a elevatorInit message, it provides the number of elevators and it's listening port
				else if (fromSystem[0].equals("elevatorInit")) {
					elevatorPort = Integer.parseInt(fromSystem[2]);
					createElevatorsReference(Integer.parseInt(fromSystem[1]));
				}	
				//This is a floorInitEnd message from the floor subsystem, it is only sent after all floors have initialized
				else if (fromSystem[0].equals("floorInitEnd")) {
					System.out.println("Received floorInitEnd message");
					floor = false;
				}	
			}
		}
	
	/**
	 * This method will try to find the closest IDLE elevator to the floor requested
	 * 
	 * @param floor The floor waiting for an elevator
	 * @return the closest elevator's number, if none found -1
	 */
	public int getClosestIdleElevator(int floor) {
		//Initialize both to -1 because if elevatorNum doesn't change then there are no idle elevators
		int closestDistance = -1;
		int elevatorNum = -1;
		
		if(!elevatorIsIdle()) {
			return -1;
		}
		//Iterate over the elevators to find the closest idle elevator to the floor requested
		for(Integer i : elevators.keySet()) {
			if(elevators.get(i).getState().equals("IDLE")) {
				int distance = Math.abs(floor - elevators.get(i).getFloor());
				//If this is the first idle elevator found, set closestDistance
				if(closestDistance == -1) {
					closestDistance = distance;
					elevatorNum = i; //Keep track of which elevator this is
				}
				else {
					if(distance < closestDistance) {
						closestDistance = distance;
						elevatorNum = i; //Keep track of which elevator this is
					}
				}
			}
		}
		
			return elevatorNum; //No elevators are currently idle
	}
	
	/**
	 * This method checks if any elevators are idle
	 * 
	 * @return true if there is at least one IDLE elevators
	 */
	public boolean elevatorIsIdle() {
		for(Integer i : elevators.keySet()) {
			if(elevators.get(i).getState().equals("IDLE")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method creates all the initial elevator references. All elevators
	 * are assumed to have an initial motor state of "IDLE"
	 * 
	 * @param nElevators The number number of elevators in the system
	 */
	public void createElevatorsReference(int nElevators) {
		
		elevatorMonitors = new ElevatorFaultMonitor[nElevators];
		for(int i = 1; i <= nElevators; i++ ) {
			elevators.put(i, new Elevator("IDLE", 1));
			elevatorMonitors[i-1] = new ElevatorFaultMonitor(this, Information.TRAVEL_TIME_PER_FLOOR + Information.TIME_CLOSE_DOOR + Information.TIME_OPEN_DOOR, i);

		}
	}
	
	public Elevator createNewElevatorReference(int elevator, String state) {
		
		return new Elevator(state, elevator);
	}
	
	/**
	 * This method creates the request to be sent to the elevator subsystem. It inserts the elevator
	 * number into the original work request so the elevator subsystem can determine which elevator to
	 * pass it along to
	 * 
	 * @param request The request to be modified
	 * @param elevator The elevator's number who the request is for
	 * @return the new request as a byte array
	 */
	public byte[] createElevatorRequest(String[] request, int elevator) {
		String req = request[0];
		req += (" " + elevator);
		for(int i = 2; i < request.length; i++)
			req += (" " + request[i]);
		print("[" + Time.getCurrentTime() + "], SCHEDULER: Routing Elevator " + elevator + " to floor " + request[2]);
		return pac.toBytes(req);
	}
	
	/**
	 * This method sends a message to the given floor that a elevator has arrived at the floor
	 * @param request the request to be sent
	 * @param floor the floor to send the request to
	 */
	public void arrivalNotificationToFloor(String[] request, int floor) {
		String str = "floorArrival";
		for(int i = 1; i < request.length; i++) {
			str += " " + request[i];
		}	
		print("[" + Time.getCurrentTime() + "], SCHEDULER: Notifying Floor " + floor + " at port: " + floorPorts.get(floor) + " that Elevator " + request[3] + " arrived." );
		rpc_send(floorPorts.get(floor), Network.pac.toBytes(str));
	}
	
	/**
	 * 
	 * 
	 * @param state The state of the Elevator "IDLE", "DOWN" "UP" "OUT_OF_ORDER"
	 * @param floor the floor number the elevator is on
	 * @return
	 */
	public Elevator newElevator(String state, int floor) {
		return new Elevator(state, floor);
	}
	
	/**
	 * This is the print method, that if the gui variable in the Information class is
	 * TRUE will also print to the "Scheduler Notification" text area.
	 * 
	 * @param str String to print
	 */
	public void print(String str) {
		if(Information.gui) {
			ElevatorGridGUI.schedulerNotificationsTA.append(str + "\n");
		}
		//System.out.println(str);
	}
	
	public void printError(String str) {
		if(Information.gui) {
			ElevatorGridGUI.errorNotificationsTA.append(str + "\n");
		}
		//System.out.println(str);
	}
	
}
