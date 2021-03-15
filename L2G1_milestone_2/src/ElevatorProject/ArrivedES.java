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
		elevator.stationary = true;
	}
	
	@Override
	public void ButtonPress() {}

	@Override
	public void Moving() {}
	
	@Override
	public void StopMoving() {}
	
	@Override
	public void CloseDoors() {}

	@Override
	public void OpenDoors() {}
	
	@Override
	public void TurnOnLamp() {}
	
	/*
	 * Method to turn off the lamp.
	 */
	@Override
	public void TurnOffLamp() {
		elevator.lampState = Information.lampState.OFF;
		System.out.println("Elevator button lamp off.");
	}

	@Override
	public void ReceivedRequest() {}
	
	/**
	 * Sends an acknowledgement to the floor that the elevator has arrived.
	 */
	@Override
	public void SendAck() {
		System.out.println("ELEVATOR STATE: ARRIVED");
		System.out.println("Elevator has arrived.");
		TurnOffLamp();
		System.out.println();
		
		//send ACK
		elevator.sendAcknowledgement();	
		
		// Change Elevator to IDLE state
		elevator.SetState(elevator.getIdleState());
	}

}
