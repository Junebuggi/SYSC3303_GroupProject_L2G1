/**
 * Scheduler.java
 * 
 * The Scheduler is responsible for handling incoming requests from the floor Subsystem and passing requests
 * to the elevator subsystem. This is done using a shared resource between the three threads and with the 
 * implementation of a state machine. 
 *
 * @version 1.0
 * @author Emma Boulay, Abeer Rafiq
 *
 * (Group 1 - SYSC 3303 L2)
 *
 */
package ElevatorProject.SchedulerSubsystem;

import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ElevatorProject.Network;

public class Scheduler extends Network {

	private ArrayList<String[]> workRequests = new ArrayList<>(); 
	private HashMap<Integer, Integer> floorPorts = new HashMap<Integer, Integer>();
	private int elevatorPort = -1;
	private HashMap<Integer, Elevator> elevators = new HashMap<Integer, Elevator>();
	private int timeout;
	
	protected class Elevator {
		private String state;
		private int floor;

		Elevator(String state, int floor){
			this.state = state;
			this.floor = floor;
		}

		public int getFloor() {
			return this.floor;
		}

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
			//This is an elevatorRequest message
			String[] data = pac.parseData(request);
			System.out.println("Request: " + Arrays.toString(data));
			if(data[0].equals("floorRequest") && data.length == 5) {
				if(!arrayContainsRequest(data, workRequests)) {
					this.workRequests.add(data);
					System.out.println("Added to workRequests array");
				}
					
			}
			
			//This is a elevator destination floor message
			if(data[0].equals("arrivalSensor") && data.length == 5) {
				arrivalNotificationToFloor(data, Integer.valueOf(data[2]));
				elevators.put(Integer.parseInt(data[3]), new Elevator(data[4], Integer.parseInt(data[3])));
				String servicableRequest = checkIfRequestPendingAtFloor(Integer.valueOf(data[2]), data[4]);
				if(servicableRequest != null) {		
					
					System.out.println("Servicable Request: " + servicableRequest);
					notifyAll();
					return pac.toBytes(servicableRequest);
				}
			}
			notifyAll();
			return Network.createACK();
	}
	
	private synchronized String checkIfRequestPendingAtFloor(int floor, String direction) {
		
		for(int i = 0; i < workRequests.size(); i++) {
			System.out.println("Floor: " + floor + " Direction: " + direction);
			if(workRequests.get(i)[3].equals(direction) && Integer.valueOf(workRequests.get(i)[2]) == floor) {
				System.out.println("Pending request found");
				return Network.pac.joinStringArray(workRequests.remove(i));

			}
		}
		
		return null;
		
	}
	
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
	
	
	public synchronized void removeRequest(int i) {
		workRequests.remove(i);
		notifyAll();
	}
	
	public int getElevatorPort() {
		return this.elevatorPort;
	}
	
	public void setUp(DatagramSocket socket) {
		
			boolean floor = true;
			
			while(floor || elevatorPort == -1) {
				System.out.println("Listening on port: " + socket.getLocalPort());
				ReturnData portData = receive(socket);
				
				try {
					System.out.println(new String(portData.getData(), pac.getEncoding()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				send(portData.getPort(), createACK(), socket);

				String[] fromSystem = Network.pac.parseData(portData.getData());
				
				if(fromSystem[0].equals("floorInit")) {
					System.out.println("Initializing floor " + Integer.parseInt(fromSystem[1]) + " to port: " + Integer.parseInt(fromSystem[2]));
					floorPorts.put(Integer.parseInt(fromSystem[1]), Integer.parseInt(fromSystem[2]));
				}
				
				else if (fromSystem[0].equals("elevatorInit")) {
					elevatorPort = Integer.parseInt(fromSystem[2]);
					createElevatorsReference(Integer.parseInt(fromSystem[1]));
				}	
				
				else if (fromSystem[0].equals("floorInitEnd")) {
					System.out.println("Received floorInitEnd message");
					floor = false;
				}

			
			}
		}
	
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
				System.out.println(floor + " - " + elevators.get(i).getFloor() + "= " + distance);
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
	
	public boolean elevatorIsIdle() {
		for(Integer i : elevators.keySet()) {
			if(elevators.get(i).getState().equals("IDLE")) {
				return true;
			}
		}
		return false;
	}
	
	public void createElevatorsReference(int nElevators) {
		for(int i = 1; i <= nElevators; i++ ) {
			elevators.put(i, new Elevator("IDLE", 1));
		}
	}
	
	public byte[] createElevatorRequest(String[] request, int elevator) {
		String req = request[0];
		req += (" " + elevator);
		for(int i = 2; i < request.length; i++)
			req += (" " + request[i]);
		System.out.println("Request to elevator " + elevator + ": " + req.toString());
		return pac.toBytes(req);
	}
	
	public void arrivalNotificationToFloor(String[] request, int floor) {
		String str = "floorArrival";
		for(int i = 1; i < request.length; i++) {
			str += " " + request[i];
		}	
		System.out.println("Message to floor: " + floor + " at port: " + floorPorts.get(floor) + "\nis: " + str);
		rpc_send(floorPorts.get(floor), Network.pac.toBytes(str));
	}
	
	public int getTimeout() {
		return this.timeout;
	}
}


