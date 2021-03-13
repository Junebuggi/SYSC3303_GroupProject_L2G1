package ElevatorProject.ElevatorSubsytem;

/**
 * This class simulates a floor button inside an elevator
 * 
 * @author Emma Boulay [Iteration 3]
 *
 */
public class ElevatorButton {
	private final int btnNumber;
	private ElevatorLamp lamp = ElevatorLamp.OFF;

	/**
	 * States of the button, the button is ON or OFF
	 */
	public enum ElevatorLamp {
		ON, OFF
	}

	/**
	 * This is the constructor method
	 * 
	 * @param btnNumber The floor the button corresponds to
	 */
	public ElevatorButton(int btnNumber) {
		this.btnNumber = btnNumber;
	}

	/**
	 * A getter to return the lamp state of the button
	 * 
	 * @return lamp state, "ON" or "OFF"
	 */
	public ElevatorLamp getLampState() {
		return lamp;
	}

	/**
	 * A setter to set the lamp state of the button
	 * 
	 * @param lampState "ON" or "OFF"
	 */
	public void setLampState(String lampState) {
		this.lamp = ElevatorLamp.valueOf(lampState);
	}

	/**
	 * A getter for the button number of the button
	 * 
	 * @return button number
	 */
	public int getBtnNumber() {
		return btnNumber;
	}

}
