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
	public void Moving() {}
	
	@Override
	public void StopMoving() {}
	
	@Override
	public void CloseDoors() {}

	@Override
	public void OpenDoors() {}
	
	
	/*
	 * Method to turn off the lamp.
	 */
	@Override
	public void TurnOffButtonLamp(int btnNumber) {
		elevator.getElevatorButton(btnNumber).setLampState("OFF");
		System.out.println("Elevator button " + btnNumber + " lamp off.");
	}
	
	/**
	 * Sends an acknowledgement to the floor that the elevator has arrived.
	 */
	public void SendAck() {
		System.out.println("ELEVATOR STATE: ARRIVED");
		System.out.println("Elevator has arrived.");
		TurnOffButtonLamp(1);
		System.out.println();
		
		//send ACK
		//elevator.sendF(port, Network.createACK())	
		
		// Change Elevator to IDLE state
		elevator.setState(elevator.getIdleState());
	}

	@Override
	public void TurnOnButtonLamp(int btnNumber) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void ButtonPress(int floorLevel) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void TurnOnDirectionLamp(String direction) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void TurnOffDirectionLamp(String direction) {
		// TODO Auto-generated method stub
		
	}

}
