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
		elevator.stationary = false;
	}
	
	/**
	 * Pressing the button will turn on the lamp and close the door.
	 */
	@Override
	public void ButtonPress(int floorLevel) {
		System.out.println("Elevator Button: " + floorLevel + ", has been pressed");
		TurnOnButtonLamp(floorLevel); //Illuminate the designated floor button
		TurnOnDirectionLamp(elevator.getDirection(floorLevel)); //Illuminate the designated direction lamp
		CloseDoors(); //Close Doors
		
		// Change Elevator to MOVING state
		elevator.setState(elevator.getMovingState());
	}
	/*
	 * Method to start moving the elevator.
	 */
	@Override
	public void Moving() {
		System.out.println("ELEVATOR STATE: MOVING");
		System.out.println("Elevator is moving.");
		int nextFloor = elevator.getNextFloor();
		while(nextFloor != elevator.getCurrentFloor());
		// Takes 5 seconds (arbitrary) to arrive to floor
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int newFloor = moveFloor();	
		System.out.println("Approaching floor " + newFloor);
		System.out.println("Arrival sensor is informing scheduler");
		String[] returnMessage = elevator.arrivalSensor(newFloor, elevator.getElevatorNumber(), elevator.getMotorDirection().toString());
		
		TurnOffButtonLamp(newFloor);
		if(!returnMessage[0].equals("ACK")){
			int floorButton = Integer.valueOf(returnMessage[4]);
			elevator.addFloorToVisit(floorButton);
			StopMoving();	
		}
		
		StopMoving();
	}

	/*
	 * Method to stop moving the elevator.
	 */
	@Override
	public void StopMoving() {
		System.out.println("Elevator has stopped, arriving at destination.");
		OpenDoors();
		System.out.println();
		
		// Change Elevator to ARRIVED state
		elevator.setState(elevator.getArrivedState());
	}
	
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

	/*
	 * Method to open the doors.
	 */
	@Override
	public void OpenDoors() {
		try {
			Thread.sleep(Information.TIME_OPEN_DOOR);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		elevator.setDoorState("OPEN");
		System.out.println("Elevator door opened.");
	}
	
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
	
	private int moveFloor() {
		int newFloor;
		
		if(elevator.getMotorDirection().equals(Motor.UP))
			newFloor = elevator.getCurrentFloor() + 1;
		else
			newFloor = elevator.getCurrentFloor() - 1;
		
		elevator.setCurrentFloor(newFloor);
		
		return newFloor;
	}

}
