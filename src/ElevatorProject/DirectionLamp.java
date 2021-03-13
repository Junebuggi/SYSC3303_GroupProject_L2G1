package ElevatorProject;

/**
 * This class models the direction lamps inside and outside the elevator. It
 * indicates the direction of the elevator. It has a direction and status of on
 * or off.
 * 
 * @author Emma Boulay [Iteration 3]
 *
 */
public class DirectionLamp {

	private Direction dir;
	private LampState onOff;

	// The type of direction lamp
	public enum Direction {
		UP, DOWN
	}

	// The status of the direciton lamp
	public enum LampState {
		ON, OFF
	}

	/**
	 * The constructor for a direction lamp
	 * 
	 * @param dir The direction, "UP" or "DOWN"
	 */
	public DirectionLamp(String dir) {
		this.dir = Direction.valueOf(dir);
		setLamp("OFF");
	}

	/**
	 * This method returns the direction of the lamp
	 * 
	 * @return the lamp direction, "UP" or "DOWN"
	 */
	public Direction getDirection() {
		return this.dir;
	}

	/**
	 * This method will toggle the lamp status. If it is on, it is turned off and
	 * vice versa.
	 */
	public void toggleOn() {
		if ((LampState.OFF).equals(onOff))
			onOff = LampState.ON;
		else
			onOff = LampState.OFF;
	}

	/**
	 * This method returns the current lamp state
	 * 
	 * @return the lamp state, "ON" or "OFF"
	 */
	public LampState getLampState() {
		return this.onOff;
	}

	/**
	 * This method will turn on or off the lamp given a status string.
	 * 
	 * @param lampState The status the lamp should be set to, "ON" or "OFF"
	 */
	public void setLamp(String lampState) {
		this.onOff = LampState.valueOf(lampState);
	}

}
