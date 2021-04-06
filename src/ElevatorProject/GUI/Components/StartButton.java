package ElevatorProject.GUI.Components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import ElevatorProject.Information;
import ElevatorProject.ElevatorSubsytem.ElevatorSubsystem;
import ElevatorProject.FloorSubsystem.FloorSubsystem;
import ElevatorProject.SchedulerSubsystem.SchedulerStateMachine;

/**
 * This is a small class to model the start button. When clicked it will begin
 * running the Elevator project simulation.
 * 
 * @author Emma Boulay [Iteration 5]
 *
 */
@SuppressWarnings("serial")
public class StartButton extends JButton {

	public StartButton() {
		super("Start!");

		// When the button is clicked, it will begin simulation and
		// become inactive (can't try to run already running simulation)a
		super.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Information.gui = true;
				SchedulerStateMachine.main(null);
				ElevatorSubsystem.main(null);
				FloorSubsystem.main(null);
				setVisible(false);

			}
		});
	}

}
