package ElevatorProject;

/**
 * IdleES.java
 * 
 * This class implements the ElevatorState interface to set the State to Idle 
 * and change characteristics of the Elevator.
 * 
 * @author Hasan Baig
 * @author Rutvik
 * @author Alden Wan Yeung Ng
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class IdleES implements ElevatorState{
	
	Elevator elevator;
   
	/**
	 * Constructor class used to initialize the object of the IdleES class.
	 * 
	 * @param newElevator		instance of the elevator class
	 */
	public IdleES(Elevator newElevator) {
		elevator = newElevator;
		elevator.stationary = true;
	}
	
	/**
	 * Pressing the button will turn on the lamp and close the door.
	 */
	@Override
	public void ButtonPress() {
		System.out.println("\nELEVATOR STATE: IDLE");
		System.out.println("Elevator Button has been pressed");
		TurnOnLamp();
		CloseDoors();
		
		System.out.println("Switching to MOVING state\n");
		
		// Change Elevator to MOVING state
		elevator.SetState(elevator.getMovingState());
	}
	
	@Override
	public void Moving() {}

	@Override
	public void StopMoving() {}
	
	/*
	 * Method to close the doors.
	 */
	@Override
	public void CloseDoors() {
		try {
			Thread.sleep(Information.TIME_CLOSE_DOOR);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		elevator.doorState = Information.doorState.CLOSE;
		System.out.println("Elevator door closed.");
	}

	@Override
	public void OpenDoors() {}
	
	/*
	 * Method to turn on the lamp.
	 */
	@Override
	public void TurnOnLamp() {
		elevator.lampState = Information.lampState.ON;
		System.out.println("Elevator button lamp on.");
	};
	
	@Override
	public void TurnOffLamp() {};
	
	/**
	 *  Invoke a request to scheduler for data.
	 */
	@Override
	public void ReceivedRequest() {
		// Receive the request
		elevator.requestSchedulerforData();
		ButtonPress();
	}
	
	@Override
	public void SendAck() {}

}
 