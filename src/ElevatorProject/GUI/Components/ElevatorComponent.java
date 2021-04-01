package ElevatorProject.GUI.Components;

import java.awt.Color;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ElevatorProject.Information;
import ElevatorProject.Network.ReturnData;

public class ElevatorComponent {
	
	private Color errorColour = Color.decode("#e9afaf");
	private Color idleColour = Color.decode("#d2e9af");
	private Color movingColour = Color.decode("#e9d8f2");
	private Color arrivedColour = Color.decode("#c3e4e8");
	private Color noColour = Color.white;
	private JPanel floors[];
	private int elevatorID;
	
	public ElevatorComponent(JPanel panel, int elevatorID) {
		this.elevatorID = elevatorID;
		floors = new JPanel[Information.NUM_FLOORS];
		for(int i = 0; i < Information.NUM_FLOORS; i++) {
			floors[i] = new JPanel();
			floors[i].setBackground(Color.WHITE);
			floors[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
		}
		panel.add(new JLabel("Car " + elevatorID));
		for(int i = Information.NUM_FLOORS-1; i >= 0; i--) {
			panel.add(floors[i]);
		}
		
		setColour("IDLE", 1);

	}
	
	public void setColour(String state, int floor) {
		
		reset();
		
		if(state.equals("IDLE"))
			floors[floor-1].setBackground(idleColour);
		else if(state.equals("MOVING"))
			floors[floor-1].setBackground(movingColour);
		else if(state.equals("ARRIVED"))
			floors[floor-1].setBackground(arrivedColour);
		else if(state.equals("ERROR")) {
			
			new Thread()
			{
			    public void run() {
			    	while(true) {
			    		try {
			    			floors[floor-1].setBackground(errorColour);
							Thread.sleep(500);
							floors[floor-1].setBackground(noColour);
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			    	}
			    }

			}.start();
			
		}
			
		else if(state.equals("NOELEVATOR"))
			floors[floor-1].setBackground(noColour);
		
	}
	
	public void reset() {
		for(JPanel floor: floors) {
			floor.setBackground(Color.WHITE);
		}
	}
	
		

}
