package ghost_behaviors;

import java.util.Random;

import javax.swing.JOptionPane;

import ghostpet.GhostBase;
import ghostpet.GhostState;

public class Behavior_Timed extends Behavior { //Definitely doesn't need to be a Behavior; however, making it one lets me reuse all the loading things I've written. 

	transient int secSinceLast = 0;
	int minSinceLast;
	int maxSinceLast;
	String BehaviorExecuted;
	public transient Behavior b;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Behavior> T loadInfo(GhostBase frame) {
		T returner = super.loadInfo(frame);
		
		try {
			Class<T> C = (Class<T>) Class.forName("ghost_behaviors."+((Behavior_Timed) returner).BehaviorExecuted);
			((Behavior_Timed) returner).b = C.newInstance().loadInfo(returner.parent);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			JOptionPane.showMessageDialog(frame, "The timed behavior "+returner.getClass().getSimpleName()+
					"could not load the executed behavior "+BehaviorExecuted+": Class does not exist. Did you remove a .json file without updating references to it, or name a class that you didn't compile?",
					"Behavior loading error", JOptionPane.ERROR_MESSAGE);
			((Behavior_Timed) returner).b = null;
			e.printStackTrace();
		}
		return returner;
	}
	
	public Behavior_Timed(GhostBase frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	public Behavior_Timed() {
		// TODO Auto-generated constructor stub
	}
	
	public void tick()
	{
		System.out.println(secSinceLast);
		secSinceLast++;
		if((((secSinceLast >= minSinceLast) && ((new Random()).nextInt(2) == 1)) || (secSinceLast >= maxSinceLast)) && (parent.state == GhostState.Passive))
		{
			parent.Say(b);
			secSinceLast = 0;
		}
	}
}
