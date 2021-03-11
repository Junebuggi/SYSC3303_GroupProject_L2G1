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
	}
	
	
	@Override
	public void Moving() {
		System.out.println("\n" + "Elevator" + elevator.getElevatorNumber() + " STATE: IDLE");
		elevator.setMotorState("IDLE");
		elevator.setDoorState("OPEN");
		
		elevator.arrivalSensor(elevator.getCurrentFloor(), elevator.getElevatorNumber(), elevator.getMotorDirection().toString());

		int nextFloor = elevator.getNextFloor();
		
		System.out.println("Elevator" + elevator.getElevatorNumber() + ": requested at floor : " + nextFloor);
		elevator.CloseDoors();
		String motorDirection = elevator.getDirection(nextFloor);
		System.out.println("Elevator" + elevator.getElevatorNumber() + " Turning on the " + motorDirection + " direction lamp");
		elevator.TurnOnDirectionLamp(motorDirection);
		elevator.setMotorState(motorDirection);
				
		System.out.println("Elevator" + elevator.getElevatorNumber() + ": Switching to MOVING state\n");
		elevator.setState(elevator.getMovingState());
	}








	

}
 