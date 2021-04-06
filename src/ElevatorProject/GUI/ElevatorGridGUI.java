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
import ElevatorProject.GUI.Components.DirectionLampComponent;
import ElevatorProject.GUI.Components.ElevatorButtonComponent;
import ElevatorProject.GUI.Components.ElevatorComponent;
import ElevatorProject.GUI.Components.FloorButtonsComponent;
import ElevatorProject.GUI.Components.SmartScroller;
import ElevatorProject.GUI.Components.StartButton;


/**
 * This class lays out the Elevator Scheduler Project GUI
 * It has the following layout
 * 
 * mainHorizantalSplitPane
 * |___vBox
 * |	|___elevatorMainPanel
 * |	|	 |___elevatorsPanel
 * |	|		  |___floorLabels
 * |	|		  |	   |___floor (JLabel)
 * |    |         |___elevatorPanel
 * |    |         |	   |___elevatorComponent
 * |    |         |___floorButtonsPanel
 * |    |              |___floorButtonsComponent
 * |    |___elevatorButtonMainPanel
 * |     	 |___elevatorButtonPanel  
 * |              |___ElevatorButtonComponent             
 * |___notificationPane
 *      |___LegendPane
 *      |___notificationVerticalSplitPane
 *           |___schedulerNotificationPane
 *           |    |___schedulerNotificationTA
 *           |___errorNotificationPane
 *                |___errorNotificationTA
 * 
 * 
 * @author Emma Boulay
 *
 */
@SuppressWarnings("serial")
public class ElevatorGridGUI extends JFrame{

	public static ElevatorComponent[] elevatorShaft;
	public static ElevatorButtonComponent[] elevatorButtons;
	public static FloorButtonsComponent[] floorButtons;
	public static JTextArea schedulerNotificationsTA;
	public static JTextArea errorNotificationsTA;
	public static DirectionLampComponent[] dirLamp;

	public ElevatorGridGUI() {
		super("Scheduler GUI");
		this.setLayout(new BorderLayout());
		this.setSize(1100, 700);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);

		Box vbox = Box.createVerticalBox();

		JPanel elevatorMainPanel = new JPanel();
		elevatorMainPanel.setLayout(new BoxLayout(elevatorMainPanel, BoxLayout.Y_AXIS));
		vbox.setPreferredSize(new Dimension(1100, 800));
		dirLamp = new DirectionLampComponent[Information.NUM_ELEVATORS];
		for(int i = 0; i < Information.NUM_ELEVATORS; i++) {
			dirLamp[i] = new DirectionLampComponent();
		}

		this.createElevatorPanel(elevatorMainPanel);

		vbox.add(elevatorMainPanel);

		this.createElevatorButtonMainPanel(vbox);
		
		JSplitPane mainHorizantalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vbox, createNotificationArea());

		getContentPane().add(mainHorizantalSplitPane);	

	}
	
	public void createElevatorPanel(JPanel elevatorMainPanel) {
		JPanel elevatorsPanel = new JPanel();
		elevatorsPanel.setLayout(new GridLayout(1, Information.NUM_ELEVATORS+2, 0, 0));
		elevatorsPanel.setBorder(BorderFactory.createTitledBorder("Elevator Positon View"));

		elevatorShaft = new ElevatorComponent[Information.NUM_ELEVATORS];
		
		this.createFloorLabels(elevatorsPanel);
		this.createElevatorComponent(elevatorsPanel);
		this.createFloorButtons(elevatorsPanel);
		
		elevatorMainPanel.add(elevatorsPanel);	
	}
	
	public void createFloorLabels(JPanel elevatorsPanel) {
		
		JPanel floorLabelsPanel = new JPanel();
		//Layout in a grid format, 1 column, NUM_Floors + 1 rows
		floorLabelsPanel.setLayout(new GridLayout(Information.NUM_FLOORS+2, 1, 0, 0));

		floorLabelsPanel.add(new JPanel());
		floorLabelsPanel.add(new JPanel());
		
		for(int i = Information.NUM_FLOORS; i > 0; i--) {
			JLabel floor = new JLabel("Floor " + i );
			floorLabelsPanel.add(floor);
		}
		elevatorsPanel.add(floorLabelsPanel);
	}
	
	
	public void createElevatorComponent(JPanel elevatorsPanel) {
		for(int i = 0; i < Information.NUM_ELEVATORS; i++) {
			JPanel elevatorPanel = new JPanel();
			elevatorPanel.setLayout(new GridLayout(Information.NUM_FLOORS+2, 1, 0, 0));
			elevatorShaft[i] = new ElevatorComponent(elevatorPanel, i+1);
			elevatorsPanel.add(elevatorPanel);
		}
	}
	
	public void createFloorButtons(JPanel elevatorsPanel) {
		JPanel floorButtonsPanel = new JPanel();
		floorButtonsPanel.setLayout(new GridLayout(Information.NUM_FLOORS+2, 1, 0, 0));
		floorButtons = new FloorButtonsComponent[Information.NUM_FLOORS];
		floorButtonsPanel.add(new JLabel(" Dir Buttons"));
		floorButtonsPanel.add(new JLabel());

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
	}
	
	public void createElevatorButtonMainPanel(Box vbox) {
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
		vbox.add(elevatoButtonMainPanel);
	}
	
	public JSplitPane createNotificationArea() {
		schedulerNotificationsTA = new JTextArea();
		schedulerNotificationsTA.setEditable(false);
		JTextArea legend = new JTextArea();
		legend.append("IDLE: Green\nMOVING: Purple\nARRIVED: Blue\nERROR: Red");
		legend.setEditable(false);
		legend.setBorder(BorderFactory.createTitledBorder("Legend"));
		errorNotificationsTA = new JTextArea();
		errorNotificationsTA.setEditable(false);

		JScrollPane schedulerNotificationPane = new JScrollPane(schedulerNotificationsTA, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		schedulerNotificationPane.setBorder(BorderFactory.createTitledBorder("Scheduler Notifications"));

		new SmartScroller(schedulerNotificationPane, SmartScroller.VERTICAL, SmartScroller.END);

		JScrollPane errorNotificationPane = new JScrollPane(errorNotificationsTA, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		errorNotificationPane.setBorder(BorderFactory.createTitledBorder("Error Notifications"));
		new SmartScroller(errorNotificationPane, SmartScroller.VERTICAL, SmartScroller.END);
		JSplitPane p1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, legend, schedulerNotificationPane);
		JSplitPane notifcationsRightSide = new JSplitPane(JSplitPane.VERTICAL_SPLIT, p1, errorNotificationPane);
		notifcationsRightSide.setVisible(true);	
		
		return notifcationsRightSide;
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			var ex = new ElevatorGridGUI();
			ex.setVisible(true);
		});
	}


}


