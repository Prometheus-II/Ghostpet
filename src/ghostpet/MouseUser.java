package ghostpet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

import ghost_behaviors.HoverAskBehavior;
import ghost_behaviors.Behavior;
import ghost_behaviors.PetBehavior;

public class MouseUser extends MouseAdapter {
	GhostBase parent;
	
	Timer times;
	int count = 0;
	
	public MouseUser() {
		// TODO Auto-generated constructor stub
	}
	
	public MouseUser(GhostBase p)
	{
		parent = p;
		times = new Timer(100, null);
		times.setRepeats(true);
		times.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				count++;
				if(parent.state != GhostState.Passive)
				{
					count = 0;
				}
				if(count >= 30)
				{
					count = 0;
					parent.Say(parent.getBehavior(HoverAskBehavior.class));
				}
				
			}
		});
	}
	
	@SuppressWarnings("unchecked") //Potentially bad practice, but it DOES get checked in the if() statement. 
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(e.getSource() instanceof AskChoiceLabel)
		{
			parent.Reset();
			parent.Say(parent.getBehavior(((AskChoiceLabel<Behavior>) e.getSource()).cls));
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if(e.getSource() == parent.gpanel.CharLabel) //Probably don't need to be this finicky, but it's a safety precaution.
		{
			if(parent.getBehavior(PetBehavior.class) != null) //Needs to be checked. Someone COULD delete PetBehavior.
				parent.getBehavior(PetBehavior.class).Execute();
			count = 0;
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		if(e.getSource() == parent.gpanel.CharLabel)
			times.restart();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == parent.gpanel.CharLabel)
		{
			times.stop();
			count = 0;
		}
	}
	
	
}
