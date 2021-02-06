/**
 * Scheduler.java
 * 
 * The scheduler thread is a shared resource between the floor and elevator subsystems. The floorSubsystem 
 * will place requests into the Scheduler and the elevatorSubsystem will get and handle those requests.
 *
 * @author Emma Boulay
 * @author Abeer Rafiq
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject;

import java.util.ArrayList;

public class Scheduler implements Runnable {

	private byte[] ack = null;
	private ArrayList<byte[]> workRequests = new ArrayList<>(); 

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
	public synchronized void putRequest(byte[] elevatorRequests) {
			//Add the work requests passed in and notify all
			this.workRequests.add(elevatorRequests);
			notifyAll();
			return;
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
	 * Run method to keep scheduler always running.
	 */	
	@Override
	public void run() {
		while (true) {

		}
	}

	/**
	 * This method returns the arrayList containing all the requests.
	 * 
	 * @return workRequests  arrayList containing all requests (type byte[])
	 */	
	public ArrayList<byte[]> getAllRequest(){
		return workRequests;
	}

}
