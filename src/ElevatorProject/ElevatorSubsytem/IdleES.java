package ElevatorProject.ElevatorSubsytem;

import java.awt.Color;

import ElevatorProject.Information;
import ElevatorProject.Time;

/**
 * IdleES.java
 * 
 * This class implements the ElevatorState interface to set the State to Idle
 * and change characteristics of the Elevator.
 * 
 * @author Hasan Baig
 * @author Rutvik
 * @author Alden Wan Yeung Ng
 * @author Emma Boulay [Iteration 3]
 * 
 *         SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class IdleES implements ElevatorState {

	Elevator elevator;

	/**
	 * Constructor class used to initialize the object of the IdleES class.
	 * 
	 * @param newElevator instance of the elevator class
	 */
	public IdleES(Elevator newElevator) {
		elevator = newElevator;
	}

	@Override
	public void Moving() {
		if(!elevator.printFlag)
			elevator.transcript.setBackground( Color.decode("#d2e9af") ); //Light green
		
		if(Information.gui)
			elevator.elevGUI.setColour("IDLE", elevator.getCurrentFloor());
		
		elevator.appendText("\n" + "[" + Time.getCurrentTime() + "], ELEVATOR" + elevator.getElevatorNumber() + " STATE: IDLE\n", elevator.printFlag);
		// Entry action
		elevator.setMotorState("IDLE");
		elevator.setDoorState("OPEN");

		// The elevator will wait until floorsToVisit is no longer empty
		int nextFloor = elevator.getNextFloor();
		
		if(elevator.checkForError())
			return;

		elevator.appendText("[" + Time.getCurrentTime() + "], ELEVATOR" + elevator.getElevatorNumber() + ": requested at floor " + nextFloor + "\n", elevator.printFlag);

		String motorDirection = elevator.getDirection(nextFloor);

		// Elevator is already at floor
		if (motorDirection.equals("IDLE")) {
			elevator.appendText("[" + Time.getCurrentTime() + "], ELEVATOR" + elevator.getElevatorNumber() + ": Switching to Arrived state\n\n", elevator.printFlag);
			elevator.setState(elevator.getArrivedState());
		}
		// Turn on direction lamp
		elevator.appendText(
				"[" + Time.getCurrentTime() + "], ELEVATOR" + elevator.getElevatorNumber() + ": Turning on the " + motorDirection + " direction lamp\n", elevator.printFlag);
		elevator.CloseDoors();
		elevator.TurnOnDirectionLamp(motorDirection);
		elevator.setMotorState(motorDirection);

		elevator.appendText("[" + Time.getCurrentTime() + "], ELEVATOR" + elevator.getElevatorNumber() + ": Switching to MOVING state\n\n", elevator.printFlag);
		elevator.setState(elevator.getMovingState());
	}

}
