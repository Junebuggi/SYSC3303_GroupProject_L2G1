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
package ElevatorProject;

import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler {

	private byte[] ack = null;
	private ArrayList<byte[]> workRequests = new ArrayList<>(); 
	private HashMap<Integer, ArrayList<Integer>> arrivals = new HashMap<Integer, ArrayList<Integer>>();


	/**
	 * The default constructor.
	 */
	public Scheduler() { }

	/**
	 * This method puts a floor request into the scheduler. This method will return
	 * when there is space for the floor request in the scheduler.
	 * 
	 * @param elevatorRequests An object representing the elevator request from the floor subsystem
	 */
	public synchronized void putRequest(byte[] request, byte[] type) {
			//Add the work requests passed in and notify all
			//This is an elevatorRequest message
			String[] data = parseData(request);
			if(new String(type).equals("floorRequest")) {
				this.workRequests.add(request);
			}
			
			//This is a elevator destination floor message
			if(new String(type).equals("arrivalSensor")) {
				if (arrivals.get((Integer.parseInt(data[3]))) == null) {
				    arrivals.put(Integer.parseInt(data[3]), new ArrayList<Integer>());
				}
				arrivals.get(Integer.parseInt(data[3])).add(Integer.parseInt(data[2]));
			}
			
			notifyAll();
			return;
	}
	
	public String[] parseData(byte[] data) {
		return new String(data).split(" ");
	}
	/**
	 * This synchronized method returns the first floor request (position 0) and removes it from the scheduler. 
	 * 
	 * @return floorRequest	the floor request that should be completed next (the first floor request)
	 */
	public synchronized byte[] getRequest() {
		//wait if there are no work requests
		while (!isWork()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		//return the request to be completed next, remove from workRequests ArrayList and notify all
		byte[] floorRequest = workRequests.remove(0);
		notifyAll();
		return floorRequest;
	}
	
  /**
	 * This synchronized method sets the acknowledgement private variable to byte[] ack passed in and notifies all.
	 * 
	 * @param ack The acknowledgement to be put into byte ack
	 */
	public synchronized void acknowledgeRequest(byte[] ack) {
		this.ack = ack;
		notifyAll();
	}
	
		
  /**
	 * This synchronized method is used to send an acknowledgement and reset private ack variable to null.
	 * 
	 * @return ackReturn The acknowledgement to be sent 
	 */	
	public synchronized byte[] getAcknowledgemnt() {	
		//wait if acknowledgement is null
		while (this.ack == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		//return acknowledgement and set private acknowledgement variable to null
		byte[] ackReturn = this.ack;
		this.ack = null;
		notifyAll();
		return ackReturn;
	}

	/**
	 * This synchronized method is used to determine if there are any work requests pending.
	 * 
	 * @return false  if there are no work requests pending
	 * @return true   if there are pending work requests
	 */	
	public synchronized boolean isWork() {
		return !workRequests.isEmpty();
	}

	/**
	 * This method returns the arrayList containing all the requests.
	 * 
	 * @return workRequests  arrayList containing all requests (type byte[])
	 */	
	public ArrayList<byte[]> getAllRequest(){
		return workRequests;
	}
	
	public int checkForAvailableElevator(int Floor, String direction) {
		//Logic to determine which elevator will service request will be implemeted
		//later
		return 1;
	}
	
	public void removeRequest(int i) {
		workRequests.remove(i);
	}
}


