package ElevatorProject.ElevatorSubsytem;

import java.awt.Color;

import ElevatorProject.Information;

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
	
	public void Moving() {
		Error();
	}

	public void Error() {
		
		if(!elevator.printFlag)
			elevator.transcript.setBackground( Color.decode("#e9afaf") ); //Light red
		
		if(Information.gui)
			elevator.elevGUI.setColour("ERROR", elevator.getCurrentFloor());
		
		elevator.appendText("\n" + "Elevator" + elevator.getElevatorNumber() + " STATE: ERROR\n", elevator.printFlag);

		elevator.setMotorState("IDLE");

		elevator.appendText("Elevator" + elevator.getElevatorNumber() + " is stuck between floor: " + elevator.getCurrentFloor() + " and floor: " + elevator.getNextFloor() + "\n", elevator.printFlag);
		elevator.appendText("Elevator" + elevator.getElevatorNumber() + " will stay in the ERROR state as it is shut down.\n", elevator.printFlag);
		
		elevator.running = false;	
	}

}
