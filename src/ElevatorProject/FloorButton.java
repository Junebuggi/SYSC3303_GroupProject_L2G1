package ElevatorProject;

public class FloorButton {
	
	private Direction dir;
	private boolean isPressed;
	
	public enum Direction{
		UP, DOWN
	}
	
	public FloorButton(String dir) {
		this.dir = Direction.valueOf(dir);
		this.isPressed = false;
	}
	
	public Direction getDirection() {
		return this.dir;
	}
	
	public void turnOnOfButton(boolean onOff) {
		isPressed = onOff;
	}
	
	public boolean isPressed() {
		return this.isPressed;
	}
	
	public String getState() {
		return (isPressed) ? "ON" : "OFF";
	}

}
