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
		//If the static gui variable is true, launch the GUI
		if(Information.gui) {
			ElevatorGridGUI.main(null);
		} 
		//Launch the three subsystems without the GUI
		else {
			SchedulerStateMachine.main(null);
			ElevatorSubsystem.main(null);
			FloorSubsystem.main(null);
		}
	}
}
