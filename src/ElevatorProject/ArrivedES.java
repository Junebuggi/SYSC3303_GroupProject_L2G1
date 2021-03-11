package ElevatorProject;

/**
 * ArrivedES.java
 * 
 * This class implements the ElevatorState interface to set the State to Arrived 
 * and change characteristics of the Elevator.
 * 
 * @author Hasan Baig
 * @author Rutvik
 * @author Alden Wan Yeung Ng
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

public class ArrivedES implements ElevatorState{

	Elevator elevator;
	
   /**
	 * Constructor class used to initialize the object of the ArrivedES class.
	 * 
	 * @param elevator		instance of the elevator class
	 */
	public ArrivedES(Elevator newElevator) {
		elevator = newElevator;
	}
	

	@Override
	public void Moving() {
		
		elevator.arrivingAtFloor(elevator.getCurrentFloor());
		System.out.println("\n" + "Elevator" + elevator.getElevatorNumber() + " STATE: ARRIVED");
		
		int floor = elevator.getCurrentFloor();
		elevator.TurnOffButtonLamp(floor);
		
		elevator.setMotorState("IDLE");
		
		System.out.println("Elevator" + elevator.getElevatorNumber() + " Switching to IDLE state\n");
		
		elevator.setState(elevator.getIdleState());		
	}

}
