/**
 * SchedulerStateMachine.java
 * 
 * The SchedulerStateMachine is responsible for implementing the activities that happen in each state of the scheduler.
 *
 * @version 1.0
 * @author Emma Boulay, Abeer Rafiq
 *
 * (Group 1 - SYSC 3303 L2)
 *
 */

package ElevatorProject;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import ElevatorProject.Network.ReturnData;

public class SchedulerStateMachine implements Runnable{
	
	/**
	 * This variable is used to keep track of the current state of the scheduler.
	 * Initial state is to wait for a request.
	 */
	private static State current_state = State.WAIT_FOR_REQUEST;
	private Scheduler scheduler;
	private DatagramSocket sendReceiveSocket;
	
	/**
	 * These are the states that the scheduler will go through.
	 * 
	 */
	public enum State {
		WAIT_FOR_REQUEST, SEND_ELEVATOR_TO_FLOOR, WAIT_FOR_ELEVATOR
	}

	/**
	 * The default constructor.
	 */
	public SchedulerStateMachine(String name, int listeningPort, int timeout) {
		scheduler = new Scheduler(name, listeningPort, timeout);
		
		try {
			sendReceiveSocket = new DatagramSocket();
			sendReceiveSocket.setSoTimeout(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Specify scheduler constructor
	 */
	public SchedulerStateMachine(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/* 
	 * Used to receive current state of scheduler.
 	*/
	public State getState() {
		return current_state;
	}
	
	
	public DatagramSocket getSocket() {
		if(Thread.currentThread().getName().equals("listeningThread")) {
			return scheduler.getSocket(0);
		}
		
		return null;
	}

	/**
	 * This method will override the Runnable interface's run method.
	 * It contains a switch statement to implement the state machine.
	 */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName());

		boolean run = true;
		int elevator = 0;
		//Wait to receive port initialization messages from the floor and elevator subsystems
		
		DatagramSocket socket = getSocket();
		if(socket != null) {
			scheduler.setUp(socket);
		}
		

		
		while (run) {
				//If this is the listening thread then wait to receive and place messages into appropriate list
				while(Thread.currentThread().getName().equals("listeningThread")){
					System.out.println(Thread.currentThread().getName());
					ReturnData returnData = scheduler.receive(scheduler.getSocket(0));
					byte[] returnMessage = scheduler.putRequest(returnData.getData());
					scheduler.send(returnData.getPort(), returnMessage , scheduler.getSocket(0));	
				}
				
				switch (current_state) {
					case WAIT_FOR_REQUEST: {
						
						//If there are requests to be serviced and an elevator is available
						if(scheduler.getAllRequest().isEmpty()) {
							break;
						}else {
							current_state = State.SEND_ELEVATOR_TO_FLOOR;
						}
						
					}
					case SEND_ELEVATOR_TO_FLOOR: {
						
						//If there are still no requests, change state to waiting for a request and exit case statement
						if(scheduler.getAllRequest().size() == 0) {
							current_state = State.WAIT_FOR_REQUEST;
							break;
						}
						
						//If there are requests, get first request and extract it's information
						String[] request = scheduler.getAllRequest().get(0);
						
						int floor = Integer.parseInt(request[2]);
						elevator = scheduler.getClosestIdleElevator(floor);
						
						//send ElevatorRequest to elevatorPort from new socket
						if(elevator == -1) {
							current_state = State.WAIT_FOR_REQUEST;
							break;
						}
						System.out.println("Sending message to elevator");
						byte[] msg = scheduler.createElevatorRequest(request, elevator);
						scheduler.send(scheduler.getElevatorPort(), msg, sendReceiveSocket);
						
						//
	
						//Change state to waiting for elevator
						current_state = State.WAIT_FOR_ELEVATOR;
						break;
					}	
					case WAIT_FOR_ELEVATOR: {
						System.out.println("Waiting for Elevator");
		
						//A timeout is set in case elevator does not respond
						ReturnData returnData = scheduler.receive(sendReceiveSocket);
						
						if(returnData.getData() != null && returnData.getPort() != -1) {
							//If acknowledgement received, remove the request and wait for next request
							scheduler.removeRequest(0);
							current_state = State.WAIT_FOR_REQUEST;
							System.out.println("Elevator " + elevator + " is available and on its way!");
							break;	
						}
						
						//Change state of elevator to send elevator to floor (if timeout occurs)
						current_state = State.SEND_ELEVATOR_TO_FLOOR;	
						System.out.println("Elevator not found");
						break;
					}
				}
			}
	}
	
	/**
	 * The main method of the SchedulerStateMachine
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		SchedulerStateMachine schedulerFSM = new SchedulerStateMachine("Scheduler", 23, 10000);
		Thread schedulerElevatorThread = new Thread(schedulerFSM, "activeThread");
		Thread schedulerFloorThread = new Thread(schedulerFSM, "listeningThread");
		schedulerElevatorThread.start();
		schedulerFloorThread.start();
		

	}

}