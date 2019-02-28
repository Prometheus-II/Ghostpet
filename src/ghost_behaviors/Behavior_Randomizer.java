package ghost_behaviors;

import java.util.Random;

import javax.swing.JOptionPane;

import ghostpet.GhostBase;

public abstract class Behavior_Randomizer extends Behavior {
	
	String[] behaves;
	public transient Behavior[] behaviors;
	
	public Behavior_Randomizer(GhostBase frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	public Behavior_Randomizer() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public <T extends Behavior> T loadInfo(GhostBase frame) {		
		T returner = super.loadInfo(frame);
		((Behavior_Randomizer)returner).setup();
		return returner;
	}
	
	public <T extends Behavior> void setup() {
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
					behaviors[i] = C.newInstance().loadInfo(parent);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(parent, "The randomizer behavior "+getClass().getSimpleName()+
							"could not load the behavior "+behaves[i]+": Class does not exist. Did you remove a .json file without updating references to it, or name a class that you didn't compile?",
							"Behavior loading error", JOptionPane.ERROR_MESSAGE);
					behaviors[i] = null;
				}
			}
		}
	}
	
	public void SelectRandom()
	{
		Random r = new Random();
		int t = r.nextInt(behaviors.length);
		if(behaviors[t] == null)
		{
			return;
		}
		else
		{
			parent.Say(behaviors[t]);
		}
			
	}
}
