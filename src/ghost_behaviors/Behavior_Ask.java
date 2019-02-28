package ghost_behaviors;

import javax.swing.JOptionPane;

import ghostpet.GhostBase;
import ghostpet.GhostState;

public class Behavior_Ask extends Behavior {
	
	public class Option
	{
		public String line;
		public String behave;
		public transient Behavior bhvr;
		
		public Option()
		{
			
		}
	}
	
	public Option[] opts;
	private String giveUp;
	public transient Behavior giveUpBehavior;
	
	public <T extends Behavior> void setup()
	{
		for(int i = 0; i < opts.length; i++) //I tried making this a method, but it wouldn't work, because the opts couldn't "know" what the parent was.
		{
			if(opts[i].behave == null)
			{
				continue;
			}
			String str = "ghost_behaviors."+opts[i].behave;
			try {
				@SuppressWarnings("unchecked")
				Class<T> C = (Class<T>) Class.forName(str);
				opts[i].bhvr = C.newInstance().loadInfo(parent);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(parent, "The Ask behavior "+getClass().getSimpleName()+
						"could not load the behavior "+opts[i].behave+": Class does not exist. Did you remove a .json file without updating references to it, or name a class that you didn't compile?",
						"Behavior loading error", JOptionPane.ERROR_MESSAGE);
				opts[i].bhvr = null;
			}
		}
		
		if(giveUp == null) //Unfilled giveUp behavior is allowed, in which case it'll just do nothing after it times out, aside from returning to normal state.
		{
			return;
		}
		String str = "ghost_behaviors."+giveUp;
		try {
			@SuppressWarnings("unchecked")
			Class<T> C = (Class<T>) Class.forName(str);
			giveUpBehavior = C.newInstance().loadInfo(parent);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public <T extends Behavior> T loadInfo(GhostBase frame) {
		T returner = super.loadInfo(frame);
		((Behavior_Ask) returner).setup();
		return returner;
	}
	
	public Behavior_Ask(GhostBase frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	public Behavior_Ask() {
		entersState = GhostState.Asking;
		// TODO Auto-generated constructor stub
	}

}
