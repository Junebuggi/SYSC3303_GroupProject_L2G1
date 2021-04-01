package ElevatorProject.GUI.Components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ElevatorProject.Information;
import ElevatorProject.ElevatorSubsytem.ElevatorSubsystem;
import ElevatorProject.FloorSubsystem.FloorSubsystem;
import ElevatorProject.SchedulerSubsystem.SchedulerStateMachine;

@SuppressWarnings("serial")
public class StartButton extends JButton{
	
	public StartButton() {
		super("Start!");
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
