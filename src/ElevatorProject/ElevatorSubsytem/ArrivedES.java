package ElevatorProject.ElevatorSubsytem;

import java.awt.Color;

/**
 * ArrivedES.java
 * 
 * This class implements the ElevatorState interface to set the State to Arrived
 * and change characteristics of the Elevator.
 * 
 * @author Hasan Baig
 * @author Rutvik
 * @author Alden Wan Yeung Ng
 * @author Emma Boulay [Iteration 3] SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class ArrivedES implements ElevatorState {

	Elevator elevator;

	/**
	 * Constructor class used to initialize the object of the ArrivedES class.
	 * 
	 * @param elevator instance of the elevator class
	 */
	public ArrivedES(Elevator newElevator) {
		elevator = newElevator;
	}

	@Override
	public void Moving() {
		if(!elevator.printFlag)
			elevator.transcript.setBackground( Color.decode("#c3e4e8") );

		elevator.appendText("\n" + "Elevator" + elevator.getElevatorNumber() + " STATE: ARRIVED\n", elevator.printFlag);
		elevator.TurnOffDirectionLamp(elevator.getMotorDirection().toString());
		elevator.setMotorState("IDLE");
		elevator.OpenDoors();
		
		elevator.arrivingAtFloor(elevator.getCurrentFloor());
		elevator.arrivalSensor(elevator.getCurrentFloor(), elevator.getElevatorNumber(), elevator.getDirection(0));

		elevator.appendText("Elevator" + elevator.getElevatorNumber() + " Switching to IDLE state\n\n", elevator.printFlag);
		elevator.setState(elevator.getIdleState());
	}
	
	public void Error(boolean hard_error) {/*do nothing - doesn't use this method*/}

}
