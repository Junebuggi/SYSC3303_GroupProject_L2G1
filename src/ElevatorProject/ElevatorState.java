package ElevatorProject;

/**
 * ElevatorState.java
 * 
 * This interface holds all the methods that the Elevator States use to implement the logic.
 * 
 * @author Hasan Baig
 * @author Rutvik
 * @author Alden Wan Yeung Ng
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

public interface ElevatorState {
	public void ButtonPress(int floorLevel);
	public void Moving();
	public void StopMoving();
	public void CloseDoors();
	public void OpenDoors();
	public void TurnOnDirectionLamp(String direction);
	public void TurnOffDirectionLamp(String direction);
	public void TurnOffButtonLamp(int btnNumber);
	public void TurnOnButtonLamp(int btnNumber);
}
