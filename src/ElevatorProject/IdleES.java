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
	public void ButtonPress(int floorLevel) {
		System.out.println("\nELEVATOR STATE: IDLE");
		System.out.println("Elevator Button: " + floorLevel + ", has been pressed");
		TurnOnButtonLamp(floorLevel); //Illuminate the designated floor button
		TurnOnDirectionLamp(elevator.getDirection(floorLevel)); //Illuminate the designated direction lamp
		CloseDoors(); //Close Doors
		
		System.out.println("Switching to MOVING state\n");
		
		// Change Elevator to MOVING state
		elevator.setState(elevator.getMovingState());
	}
	
	@Override
	public void Moving() {
		int nextFloor = elevator.getNextFloor();
		if(nextFloor != -1) {
			ButtonPress(nextFloor);
			elevator.setState(elevator.getMovingState());
		}
	}

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
		elevator.setDoorState("CLOSE");
		System.out.println("Elevator door closed.");
	}

	@Override
	public void OpenDoors() {}
	
	/*
	 * Method to turn on the lamp.
	 */
	@Override
	public void TurnOnDirectionLamp(String direction) {
		elevator.getDirectionLamp(direction).setLamp("ON");
		System.out.println("Elevator button lamp on.");
	};
	
	@Override
	public void TurnOffDirectionLamp(String direction) {};
	
	/**
	 *  Invoke a request to scheduler for data.
	 */

	@Override
	public void TurnOffButtonLamp(int btnNumber) {
		elevator.getElevatorButton(btnNumber).setLampState("OFF");
		
	}

	@Override
	public void TurnOnButtonLamp(int btnNumber) {
		elevator.getElevatorButton(btnNumber).setLampState("ON");
		
	}
	

}
 