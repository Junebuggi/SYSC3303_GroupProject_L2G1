package ElevatorProject.ElevatorSubsytem;

import java.awt.Color;

/**
 * ErrorES.java
 * 
 * This class implements the ElevatorState interface to set the State to Error
 * and change characteristics of the Elevator.
 * 
 * @author Hasan Baig
 * @author Alden Wan Yeung Ng
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class ErrorES implements ElevatorState {

	Elevator elevator;

	/**
	 * Constructor class used to initialize the object of the ErrorES class.
	 * 
	 * @param newElevator instance of the elevator class
	 */
	public ErrorES(Elevator newElevator) {
		elevator = newElevator;
	}
	
	public void Moving() {/*do nothing - doesn't use this method*/}

	@Override
	public void Error(boolean hard_error) {
		if(!elevator.printFlag)
			elevator.transcript.setBackground( Color.decode("#e9afaf") ); //Light red
		
		elevator.appendText("\n" + "Elevator" + elevator.getElevatorNumber() + " STATE: ERROR\n", elevator.printFlag);

		//HARD FAULT - ELEVATOR STUCK BETWEEN FLOORS
		if(hard_error) {
			elevator.setMotorState("IDLE");

			elevator.appendText("Elevator" + elevator.getElevatorNumber() + " is stuck between floor: " + elevator.getCurrentFloor() + " and floor: " + elevator.getNextFloor() + "\n", elevator.printFlag);
			elevator.appendText("Elevator" + elevator.getElevatorNumber() + " will stay in the ERROR state as it is shut down.\n", elevator.printFlag);
		} 
		
		//TRANSIENT FAULT - DOORS STUCK
		else {
			elevator.setDoorState("OPEN");
			
			//do something
		}
			



		// Elevator is already at floor
//		if (motorDirection.equals("IDLE")) {
//			elevator.appendText("Elevator" + elevator.getElevatorNumber() + ": Switching to Arrived state\n\n", elevator.printFlag);
//			elevator.setState(elevator.getArrivedState());
//		}
//		// Turn on direction lamp
//		elevator.appendText(
//				"Elevator" + elevator.getElevatorNumber() + " Turning on the " + motorDirection + " direction lamp\n", elevator.printFlag);
//		elevator.CloseDoors();
//		elevator.TurnOnDirectionLamp(motorDirection);
//		elevator.setMotorState(motorDirection);
//
//		elevator.appendText("Elevator" + elevator.getElevatorNumber() + ": Switching to MOVING state\n\n", elevator.printFlag);
//		elevator.setState(elevator.getMovingState());
	}

}
