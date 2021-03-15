package ElevatorProject;

/**
 * MovingES.java
 * 
 * This class implements the ElevatorState interface to set the State to Moving 
 * and change characteristics of the Elevator.
 * 
 * @author Hasan Baig
 * @author Rutvik
 * @author Alden Wan Yeung Ng
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class MovingES implements ElevatorState {

	Elevator elevator;
	
    /**
	 * Constructor class used to initialize the object of the MovingES class.
	 * 
	 * @param newElevator		instance of the elevator class
	 */
	public MovingES(Elevator newElevator) {
		elevator = newElevator;
		elevator.stationary = false;
	}
	
	@Override
	public void ButtonPress() {}

	/*
	 * Method to start moving the elevator.
	 */
	@Override
	public void Moving() {
		System.out.println("ELEVATOR STATE: MOVING");
		System.out.println("Elevator is moving.");
		
		// Takes 5 seconds (arbitrary) to arrive to floor
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StopMoving();
	}

	/*
	 * Method to stop moving the elevator.
	 */
	@Override
	public void StopMoving() {
		System.out.println("Elevator has stopped, arriving at destination.");
		OpenDoors();
		System.out.println();
		
		// Change Elevator to ARRIVED state
		elevator.SetState(elevator.getArrivedState());
	}
	
	@Override
	public void CloseDoors() {}

	/*
	 * Method to open the doors.
	 */
	@Override
	public void OpenDoors() {
		try {
			Thread.sleep(Information.TIME_OPEN_DOOR);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		elevator.doorState = Information.doorState.OPEN;
		System.out.println("Elevator door opened.");
	}
	
	@Override
	public void TurnOnLamp() {}
	
	@Override
	public void TurnOffLamp() {}
	
	@Override
	public void ReceivedRequest() {}
	
	@Override
	public void SendAck() {}

}
