package ElevatorProject.GUI.Components;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DirectionLampComponent extends JLabel{
	
	private ImageIcon idle;
	private ImageIcon down;
	private ImageIcon up;
	
	public DirectionLampComponent() {
		super();
		idle = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/DirectionLampsIDLE.png")
				.getImage().getScaledInstance(90, 25, Image.SCALE_DEFAULT));

		down = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/DirectionLampsDOWN.png")
				.getImage().getScaledInstance(90, 25, Image.SCALE_DEFAULT));
		up = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/DirectionLampsUP.png")
				.getImage().getScaledInstance(90, 25, Image.SCALE_DEFAULT));
		
		
		this.setIcon(idle);
	}
	
	public void setDirection(String dir) {
		if(dir.equals("IDLE")) {
			super.setIcon(idle);
		}
		else if(dir.equals("DOWN")) {
			super.setIcon(down);
		}
		else if(dir.equals("UP")) {
			super.setIcon(up);
		}
	}

}
