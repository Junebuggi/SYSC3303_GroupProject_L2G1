package ElevatorProject.FloorSubsystem;
/**
 * This class simulates a floor button on a floor
 * @author Emma Boulay [Iteration 3]
 *
 */
public class FloorButton {
	
	private Direction dir;
	private boolean isPressed;
	
	/**
	 * The floor button can either be an "UP" or "DOWN" button
	 */
	public enum Direction{
		UP, DOWN
	}
	
	/**
	 * The constructor method
	 * @param dir floor button direction, "UP" or "DOWN"
	 */
	public FloorButton(String dir) {
		this.dir = Direction.valueOf(dir);
		this.isPressed = false;
	}
	
	/**
	 * This is a getter method that returns the direction of the button
	 * @return button direction, "UP" or "DOWN"
	 */
	public Direction getDirection() {
		return this.dir;
	}
	
	/**
	 * This is a setter method that will turn the button off or on
	 * @param onOff, if true: button turns on, false: button turns off
	 */
	public void turnOnOfButton(boolean onOff) {
		isPressed = onOff;
	}
	
	/**
	 * This method checks if the button is pressed
	 * @return true if button is pressed
	 */
	public boolean isPressed() {
		return this.isPressed;
	}
	
	/**
	 * This is a getter method that returns the state of the button
	 * @return button state: "ON" or "OFF"
	 */
	public String getState() {
		return (isPressed) ? "ON" : "OFF";
	}

}
