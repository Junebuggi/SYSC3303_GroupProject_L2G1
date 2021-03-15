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

public class SchedulerStateMachine implements Runnable{
	
	/**
	 * This variable is used to keep track of the current state of the scheduler.
	 * Initial state is to wait for a request.
	 */
	private static State current_state = State.WAIT_FOR_REQUEST;

	/**
	 * Instantiate a scheduler object.
	 */
	private Scheduler scheduler = new Scheduler();
	
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
	public SchedulerStateMachine() {
		
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


	/**
	 * This method will override the Runnable interface's run method.
	 * It contains a switch statement to implement the state machine.
	 */
	@Override
	public synchronized void run() {
		//Always run
		while (true) {
			switch (current_state) {
				case WAIT_FOR_REQUEST: {
					//If the scheduler is currently waiting for a request, continuously check if there are any pending requests
					while(!scheduler.isWork()) {
					}
					//If there are pending requests, change the state of the scheduler to handle the next activities
					current_state = State.SEND_ELEVATOR_TO_FLOOR;
					break;
				}
				case SEND_ELEVATOR_TO_FLOOR: {
					//If there are still no requests, change state to waiting for a request and exit case statement
					if(scheduler.getAllRequest().size() == 0) {
						current_state = State.WAIT_FOR_REQUEST;
						break;
					}
					//If there are requests, get first request and extract it's information
					String[] request = scheduler.parseData(scheduler.getAllRequest().get(0));
					int floor = Integer.parseInt(request[2]);
					String direction = request[3];
					int elevator = scheduler.checkForAvailableElevator(floor, direction);
					//System.out.println("Elevator " + elevator + " is available and on its way!");

					//Change state to waiting for elevator
					current_state = State.WAIT_FOR_ELEVATOR;
					break;
				}	
				case WAIT_FOR_ELEVATOR: {
					//This state receives acknowledgement and removes request accordingly
					long start = System.currentTimeMillis();
					//A timeout of 10 seconds in case elevator does not respond
					while(start + 10000 < System.currentTimeMillis()) {
						if(scheduler.getAcknowledgemnt() != null) {
							//If acknowledgement received, remove the request and wait for next request
							current_state = State.WAIT_FOR_REQUEST;
							scheduler.removeRequest(0);
							break;
						}	
					}
					//Change state of elevator to send elevator to floor (if timeout occurs)
					current_state = State.SEND_ELEVATOR_TO_FLOOR;	
					break;
				}
			}
		}
	}

}