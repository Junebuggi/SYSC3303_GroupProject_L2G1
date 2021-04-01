package ElevatorProject.GUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ElevatorProject.Information;
import ElevatorProject.GUI.Components.ElevatorButtonComponent;
import ElevatorProject.GUI.Components.ElevatorComponent;
import ElevatorProject.GUI.Components.FloorButtonsComponent;
import ElevatorProject.GUI.Components.StartButton;

public class ElevatorGridGUI extends JFrame{
	
	public static ElevatorComponent[] elevatorShaft;
	public static ElevatorButtonComponent[] elevatorButtons;
	public static FloorButtonsComponent[] floorButtons;
	
	public ElevatorGridGUI() {
		super("Scheduler GUI");
		this.setLayout(new BorderLayout());
		this.setSize(550, 650);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		Box box = Box.createVerticalBox();
		
		JPanel elevatorMainPanel = new JPanel();
		elevatorMainPanel.setLayout(new BoxLayout(elevatorMainPanel, BoxLayout.Y_AXIS));
		elevatorMainPanel.setPreferredSize(new Dimension(275, 500));
		
		
		
		
		JPanel elevatorsPanel = new JPanel();
		elevatorsPanel.setLayout(new GridLayout(1, Information.NUM_ELEVATORS+2, 0, 0));
		elevatorsPanel.setBorder(BorderFactory.createTitledBorder("Elevator Positon View"));
		
		elevatorShaft = new ElevatorComponent[Information.NUM_ELEVATORS];
		JPanel floorLabels = new JPanel();
		floorLabels.setLayout(new GridLayout(Information.NUM_FLOORS+1, 1, 0, 0));
		
		floorLabels.add(new JPanel());
		for(int i = Information.NUM_FLOORS; i > 0; i--) {
			JLabel floor = new JLabel("Floor " + i );
			floorLabels.add(floor);
		}
		elevatorsPanel.add(floorLabels);
		
		for(int i = 0; i < Information.NUM_ELEVATORS; i++) {
			JPanel elevatorPanel = new JPanel();
			elevatorPanel.setLayout(new GridLayout(Information.NUM_FLOORS+1, 1, 0, 0));
			elevatorShaft[i] = new ElevatorComponent(elevatorPanel, i+1);
			elevatorsPanel.add(elevatorPanel);
		}
		
		JPanel floorButtonsPanel = new JPanel();
		floorButtonsPanel.setLayout(new GridLayout(Information.NUM_FLOORS+1, 1, 0, 0));
		floorButtons = new FloorButtonsComponent[Information.NUM_FLOORS];
		floorButtonsPanel.add(new JLabel(" Dir Buttons"));
		
		for(int i = Information.NUM_FLOORS; i >= 1; i--) {
			JPanel panel = new JPanel();
			boolean topFloor = false, bottomFloor = false;
			if(i == 1)
				bottomFloor = true;
			else if (i == (Information.NUM_FLOORS))
				topFloor = true;
			panel.setLayout(new GridLayout(1, 2, 0, 0));
			floorButtons[i-1] = new FloorButtonsComponent(panel, topFloor, bottomFloor);
			floorButtonsPanel.add(panel);
		}
		elevatorsPanel.add(floorButtonsPanel);
		elevatorMainPanel.add(elevatorsPanel);
		
		box.add(elevatorMainPanel);
		
		
		JPanel elevatoButtonMainPanel = new JPanel();
		elevatoButtonMainPanel.setLayout(new GridLayout(1, Information.NUM_ELEVATORS+1, 0, 0));
		elevatorButtons = new ElevatorButtonComponent[Information.NUM_ELEVATORS];
		elevatoButtonMainPanel.add(new StartButton());
		for(int i = 0; i < Information.NUM_ELEVATORS; i++) {
			JPanel elevatorButtonPanel = new JPanel();
			elevatorButtonPanel.setBorder(BorderFactory.createTitledBorder("Car " + (i+1) + ": Buttons"));
			elevatorButtonPanel.setLayout(new GridLayout(5, 5, 0, 0));
			elevatorButtons[i] = new ElevatorButtonComponent(elevatorButtonPanel) ;
			elevatoButtonMainPanel.add(elevatorButtonPanel);
		}
		
		
		box.add(elevatoButtonMainPanel);
		
		getContentPane().add(box);
		
		
		
		
		
		
		
		
		
		
		//this.pack();
		
		
		
        
	}
	
	public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            var ex = new ElevatorGridGUI();
            ex.setVisible(true);
        });
	}
	
	
}


