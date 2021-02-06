package ElevatorProject;

public class Information {
	
	public static final Integer NUM_FLOORS = 7;
	
	//Time measured and averaged in Iteration 0 (unit: milliseconds)
	public static final Integer TRAVEL_TIME_PER_FLOOR = 19800;
	public static final Integer TIME_OPEN_DOOR = 4600;
	public static final Integer TIME_CLOSE_DOOR = 4600;
	
	public enum lampState{ON, OFF}
	public enum motorDirection{UP, DOWN, IDLE}
	public enum doorState{OPEN, CLOSE}
	public enum directionLamp{UP, DOWN, NOT_PRESSED}

}
