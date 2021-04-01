package ElevatorProject;

import ElevatorProject.ElevatorSubsytem.ElevatorSubsystem;
import ElevatorProject.FloorSubsystem.FloorSubsystem;
import ElevatorProject.GUI.ElevatorGridGUI;
import ElevatorProject.SchedulerSubsystem.SchedulerStateMachine;

public class Project {
	
	/**
	 * This will run the three different subsystems
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(Information.gui) {
			ElevatorGridGUI.main(null);
		} else {
			SchedulerStateMachine.main(null);
			ElevatorSubsystem.main(null);
			FloorSubsystem.main(null);
		}
		
		
	}

}
