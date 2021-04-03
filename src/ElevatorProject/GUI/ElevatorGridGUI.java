package ElevatorProject.GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import ElevatorProject.Information;
import ElevatorProject.GUI.Components.ElevatorButtonComponent;
import ElevatorProject.GUI.Components.ElevatorComponent;
import ElevatorProject.GUI.Components.FloorButtonsComponent;
import ElevatorProject.GUI.Components.SmartScroller;
import ElevatorProject.GUI.Components.StartButton;

@SuppressWarnings("serial")
public class ElevatorGridGUI extends JFrame{
	
	public static ElevatorComponent[] elevatorShaft;
	public static ElevatorButtonComponent[] elevatorButtons;
	public static FloorButtonsComponent[] floorButtons;
	public static JTextArea schedulerNotificationsTA;
	public static JTextArea errorNotificationsTA;
	public static JScrollPane schedulerNotificationPane;
	
	public ElevatorGridGUI() {
		super("Scheduler GUI");
		this.setLayout(new BorderLayout());
		this.setSize(900, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		
		Box vbox = Box.createVerticalBox();
		
		JPanel elevatorMainPanel = new JPanel();
		elevatorMainPanel.setLayout(new BoxLayout(elevatorMainPanel, BoxLayout.Y_AXIS));
		elevatorMainPanel.setPreferredSize(new Dimension(250, 500));
		
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
		
		vbox.add(elevatorMainPanel);
		
		
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
		
		
		vbox.add(elevatoButtonMainPanel);;
		
		schedulerNotificationsTA = new JTextArea();
		schedulerNotificationsTA.setEditable(false);
		
		errorNotificationsTA = new JTextArea();
		errorNotificationsTA.setEditable(false);
    	
	    schedulerNotificationPane = new JScrollPane(schedulerNotificationsTA, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
	    schedulerNotificationPane.setBorder(BorderFactory.createTitledBorder("Scheduler Notifications"));
	    
	    new SmartScroller(schedulerNotificationPane, SmartScroller.VERTICAL, SmartScroller.END);
	    
	    JScrollPane errorNotificationPane = new JScrollPane(errorNotificationsTA, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    errorNotificationPane.setBorder(BorderFactory.createTitledBorder("Error Notifications"));
	    new SmartScroller(errorNotificationPane, SmartScroller.VERTICAL, SmartScroller.END);
		JSplitPane notifcationsRightSide = new JSplitPane(JSplitPane.VERTICAL_SPLIT, schedulerNotificationPane, errorNotificationPane);
		notifcationsRightSide.setVisible(true);	    
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vbox, notifcationsRightSide);
	    
		getContentPane().add(splitPane);
		
		
		
		
		
		
		//this.pack();
		
		
		
        
	}
	
	public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            var ex = new ElevatorGridGUI();
            ex.setVisible(true);
        });
	}
	
	
}


