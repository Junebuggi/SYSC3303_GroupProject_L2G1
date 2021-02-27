package ElevatorProject;


public class SchedulerStateMachine implements Runnable{
	
	/**
	 * These are the states that the scheduler will go through.
	 * 
	 */
	public enum State {
		WAIT_FOR_REQUEST, SEND_ELEVATOR_TO_FLOOR, WAIT_FOR_ELEVATOR
	}

	/**
	 * This variable is used to keep track of the current state of the scheduler.
	 * Initial state is to wait for a request.
	 */
	private static State current_state = State.WAIT_FOR_REQUEST;
	private Scheduler scheduler = new Scheduler();
	private boolean verbose;
	
	public SchedulerStateMachine(String v) {
		if (v.equals("verbose")) {
			this.verbose = true;
		}
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
	public void run() {
		while (true) {
			switch (current_state) {
				case WAIT_FOR_REQUEST: {
					while(!scheduler.isWork()) {		
					}
					current_state = State.SEND_ELEVATOR_TO_FLOOR;
					if(verbose)
						System.out.println("Current State: WAIT_FOR_REQUEST\nWork Request Recieved\nNew State: SEND_ELEVATOR_TO_FLOOR");
					break;
				}
				case SEND_ELEVATOR_TO_FLOOR: {
						String[] request = scheduler.parseData(scheduler.getAllRequest().get(0));
						int floor = Integer.parseInt(request[2]);
						String direction = request[3];
						int elevator = scheduler.checkForAvailableElevator(floor, direction);
						System.out.println("Elevator " + elevator + " is availble and on its way!");

						current_state = State.WAIT_FOR_ELEVATOR;
					break;
				}	
				case WAIT_FOR_ELEVATOR: {
					long start = System.currentTimeMillis();
					//A timeout of 10 seconds in case elevator does not respond
					while(start + 10000 < System.currentTimeMillis()) {
						if(scheduler.getAcknowledgemnt() != null) {
							current_state = State.WAIT_FOR_REQUEST;
							scheduler.removeRequest(0);
							break;
						}	
					}
					current_state = State.SEND_ELEVATOR_TO_FLOOR;	
					break;
				}
			}

		}
	}

}
