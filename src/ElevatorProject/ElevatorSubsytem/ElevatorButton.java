package ElevatorProject.ElevatorSubsytem;

public class ElevatorButton {
	private final int btnNumber;
	private ElevatorLamp lamp = ElevatorLamp.OFF;
	
	public enum ElevatorLamp{
		ON, OFF
	}
	
	public ElevatorButton(int btnNumber) {
		this.btnNumber = btnNumber;
	}
	
	public ElevatorLamp getLampState() {
		return lamp;
	}
	
	public void setLampState(String lampState) {
		this.lamp = ElevatorLamp.valueOf(lampState);
	}
	
	public int getBtnNumber() {
		return btnNumber;
	}

}
