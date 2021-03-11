package ElevatorProject;

import ElevatorProject.Elevator.Motor;

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
	}
	
	
	/**
	 * Method to start moving the elevator.
	 */
	@Override
	public void Moving() {
		System.out.println("ELEVATOR" + elevator.getElevatorNumber() + " STATE: MOVING");
		System.out.println("Elevator" + elevator.getElevatorNumber() + " is moving.");
		int nextFloor = elevator.getNextFloor();
		elevator.setMotorState(elevator.getDirection(nextFloor));
		System.out.println("For Elevator" + elevator.getElevatorNumber() + " Next floor: " + nextFloor);
		while(nextFloor != elevator.getCurrentFloor()) {
		// Takes 5 seconds (arbitrary) to arrive to floor
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

			moveFloor();
			
			System.out.println("Elevator" + elevator.getElevatorNumber() + " approaching floor " + elevator.getCurrentFloor());
			System.out.println("Elevator" + elevator.getElevatorNumber() + " arrival sensor is informing scheduler\n");
			String[] returnMessage = elevator.arrivalSensor(elevator.getCurrentFloor(), elevator.getElevatorNumber(), elevator.getMotorDirection().toString());
			elevator.TurnOffButtonLamp(elevator.getCurrentFloor());
			if(!returnMessage[0].equals("ACK") && returnMessage.length == 5){
				System.out.println("Elevator" + elevator.getElevatorNumber() + ": request at this floor");
				int floorButton = Integer.valueOf(returnMessage[4]);
				elevator.ButtonPress(floorButton);
				elevator.addFloorToVisit(floorButton);
				StopMoving();	
			}
			
			nextFloor = elevator.getNextFloor();
			elevator.setMotorState(elevator.getDirection(nextFloor));
			if(elevator.getMovingState().toString().equals("IDLE")) {
				StopMoving();
				break;
			}
			
		}
		System.out.println("Elevator" + elevator.getElevatorNumber() + " Switching to ARRIVED state\n");
		StopMoving();
	}

	private void StopMoving() {
		System.out.println("Elevator" + elevator.getElevatorNumber() + " has stopped, arriving at destination.");
		elevator.TurnOffDirectionLamp(elevator.getMotorDirection().toString());
		elevator.setMotorState("IDLE");
		elevator.OpenDoors();
		System.out.println();
		
		// Change Elevator to ARRIVED state
		elevator.setState(elevator.getArrivedState());
	}
	
	private void moveFloor() {
		int newFloor = elevator.getCurrentFloor();
		if(newFloor == elevator.getMaxFloor()) {
			return;
		}
		if(elevator.getMotorDirection().equals(Motor.UP)) {
			newFloor++;
		}
		else if(elevator.getMotorDirection().equals(Motor.DOWN))
			newFloor--;
		
		elevator.setCurrentFloor(newFloor);
		
	}

}
