package ElevatorProject;

public class DirectionLamp {

	private Direction dir;
	private LampState onOff;
	
	public enum Direction{
		UP, DOWN
	}
	
	public enum LampState{
		ON, OFF
	}
	public DirectionLamp(String dir) {
		this.dir = Direction.valueOf(dir);
		setLamp("OFF");
	}
	
	public Direction getDirection() {
		return this.dir;
	}
	
	public void toggleOn() {
		if((LampState.OFF).equals(onOff)) 
			onOff = LampState.ON;
		else
			onOff = LampState.OFF;
	}
	
	public LampState getLampState() {
		return this.onOff;
	}
	
	public void setLamp(String lampState) {
		this.onOff = LampState.valueOf(lampState);
	}
	

}
