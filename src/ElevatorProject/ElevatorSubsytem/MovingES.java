package ElevatorProject.ElevatorSubsytem;

import ElevatorProject.Information;
import ElevatorProject.ElevatorSubsytem.Elevator.Motor;

/**
 * MovingES.java
 * 
 * This class implements the ElevatorState interface to set the State to Moving
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

public class MovingES implements ElevatorState {

	Elevator elevator;

	/**
	 * Constructor class used to initialize the object of the MovingES class.
	 * 
	 * @param newElevator instance of the elevator class
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

		while (nextFloor != elevator.getCurrentFloor()) {
			// This will decrease or increase the current floor by 1 depending on motor
			// direction.
			if (moveFloor() == -1) {
				StopMoving();
				return;
			}
			;
			// Takes 9.5 seconds to arrive to floor
			try {
				Thread.sleep(Information.TRAVEL_TIME_PER_FLOOR);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Approaching floor, arrival sensor is triggered and informing the scheduler
			System.out.println(
					"Elevator" + elevator.getElevatorNumber() + " approaching floor " + elevator.getCurrentFloor());
			System.out.println("Elevator" + elevator.getElevatorNumber() + " arrival sensor is informing scheduler\n");
			// The scheduler will inform the elevator if it should stop at this floor
			String[] returnMessage = elevator.arrivalSensor(elevator.getCurrentFloor(), elevator.getElevatorNumber(),
					elevator.getMotorDirection().toString());

			// If it should stop, it will stop, open doors and passengers will press their
			// elevator buttons
			if (!returnMessage[0].equals("ACK") && returnMessage.length == 5) {
				System.out.println("Elevator" + elevator.getElevatorNumber() + ": request at this floor");
				int floorButton = Integer.valueOf(returnMessage[4]);
				elevator.ButtonPress(floorButton);
				elevator.addFloorToVisit(floorButton);
				StopMoving();
				return;
			}

		}
		System.out.println("Elevator" + elevator.getElevatorNumber() + " Switching to ARRIVED state\n");
		StopMoving();
	}

	/**
	 * This is a helper function. It is called when the elevator should stop moving
	 * and switch to the arrived state
	 */
	private void StopMoving() {
		System.out.println("Elevator" + elevator.getElevatorNumber() + " has stopped, arriving at destination.");
		elevator.TurnOffDirectionLamp(elevator.getMotorDirection().toString());
		elevator.setMotorState("IDLE");
		elevator.OpenDoors();

		// Change Elevator to ARRIVED state
		elevator.setState(elevator.getArrivedState());
	}

	/**
	 * This function determines what the new floor of the elevator is after one
	 * floor cycle depending on the current motor direction.
	 * 
	 * @return -1 if the elevator is attempting something illegal, 1 otherwise
	 */
	private int moveFloor() {
		int newFloor = elevator.getCurrentFloor();

		if (newFloor == elevator.getMaxFloor() && elevator.getMotorDirection().equals("UP")) {
			return -1;
		}
		if (newFloor == 1 && elevator.getMotorDirection().equals("DOWN")) {
			return -1;
		}
		if (elevator.getMotorDirection().equals(Motor.UP)) {
			newFloor++;
		} else if (elevator.getMotorDirection().equals(Motor.DOWN))
			newFloor--;

		elevator.setCurrentFloor(newFloor);

		return 1;

	}

}
