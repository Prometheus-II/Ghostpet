package ghost_behaviors;

import javax.swing.JOptionPane;

import ghostpet.GhostBase;
import ghostpet.GhostState;

public class Behavior_Sequence extends Behavior {
	
	String[] behaves;
	public transient Behavior[] behaviors;
	
	public transient Behavior prevBehavior;
	public int numOfBehavior;

	public Behavior_Sequence() {
		entersState = GhostState.Passive;
	}
	
	@Override
	public <T extends Behavior> T loadInfo(GhostBase frame) {	
		T returner = super.loadInfo(frame);
		return returner;
	}
	
	public <T extends Behavior> void setup() {
		numOfBehavior = 0;
		behaviors = new Behavior[behaves.length];
		String str;
		for(int i = 0; i < behaves.length; i++)
		{
			if(behaves[i] == null)
			{
				behaviors[i] = null;
			}
			else
			{
				str = "ghost_behaviors."+behaves[i];
				try {
					@SuppressWarnings("unchecked")
					Class<T> C = (Class<T>) Class.forName(str);
					behaviors[i] = parent.getBehavior(C);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(parent, "The sequence behavior "+getClass().getSimpleName()+
							"could not load the behavior "+behaves[i]+": Class does not exist. Did you remove a .json file without updating references to it, or name a class that you didn't compile?",
							"Behavior loading error", JOptionPane.ERROR_MESSAGE);
					behaviors[i] = null;
				}
			}
		}
	}
	
	public Boolean ExecNextSequence()
	{
		parent.Say(behaviors[numOfBehavior]);
		numOfBehavior++;
		if(numOfBehavior >= behaviors.length)
		{
			numOfBehavior = 0;
			return true;
		}
		return false;
	}
}
