package ElevatorProject.GUI.Components;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FloorButtonsComponent {
	private JLabel[] buttons;
	private ImageIcon upPressed;
	private ImageIcon upNotPressed;
	private ImageIcon downPressed;
	private ImageIcon downNotPressed;
	private boolean topFloor;
	private boolean bottomFloor;
	
	public FloorButtonsComponent(JPanel panel, boolean topFloor, boolean bottomFloor) {
		this.topFloor = topFloor;
		this.bottomFloor = bottomFloor;
		
		upNotPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorUpButtonNotPressed.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		upPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorUpButtonPressed.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		
		downNotPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorDownButtonNotPressed.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		downPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorDownButtonPressed.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		
		buttons = new JLabel[2];
		if(topFloor) {
			buttons[0] = new JLabel();
			buttons[1] = new JLabel(downNotPressed);
			
		} else if(bottomFloor) {
			buttons[0] = new JLabel(upNotPressed);
			buttons[1] = new JLabel();
		} else {
			buttons[0] = new JLabel(upNotPressed);
			buttons[1] = new JLabel(downNotPressed);	
		}
		panel.add(buttons[0]);
		panel.add(buttons[1]);
		
	}
	
	public void pressButton(String direction) {
		if(direction.equals("UP")){
			buttons[0].setIcon(upPressed);
		} else if( direction.equals("DOWN")) {
			buttons[1].setIcon(downPressed);
		}
	}
	
	public void turnOffButton(String direction) {
		if(direction.equals("UP") && !topFloor){
			buttons[0].setIcon(upNotPressed);
		} else if( direction.equals("DOWN") && !bottomFloor) {
			buttons[1].setIcon(downNotPressed);
		}
		
		if(direction.equals("DOWN") && bottomFloor){
			buttons[0].setIcon(upNotPressed);
		} else if( direction.equals("UP") && topFloor) {
			buttons[1].setIcon(downNotPressed);
		}
	}

}
