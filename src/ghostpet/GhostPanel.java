package ghostpet;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.BorderLayout;

public class GhostPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3988480078155656515L;

	/**
	 * Create the panel.
	 */
	
	JLabel CharLabel;
	ImageIcon def;
	
	public GhostPanel(GhostBase parent) {
		def = parent.data.defImg;
		setBackground(new Color(0,0,0,0));
		setSize(def.getIconWidth(),def.getIconHeight());
		setMinimumSize(getSize());
		setLayout(new BorderLayout(0, 0));
		
		CharLabel = new JLabel("");
		MouseUser m = new MouseUser(parent);
		CharLabel.addMouseMotionListener(m);
		CharLabel.addMouseListener(m);
		CharLabel.setIcon(def);
				
		add(CharLabel, BorderLayout.CENTER);
	}
	
	public void Say(ImageIcon in)
	{
		CharLabel.setIcon(in);
	}
	
	public void Reset()
	{
		CharLabel.setIcon(def);
		updateUI();
	}
}
