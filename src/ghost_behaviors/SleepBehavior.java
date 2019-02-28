package ghost_behaviors;

import ghostpet.GhostBase;

public class SleepBehavior extends Behavior {

	public SleepBehavior(GhostBase frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	public SleepBehavior() {
		// TODO Auto-generated constructor stub
	}
	
	public void Execute() {
		for(int i = 0; i < parent.behaviorList.size(); i++)
		{
			if(parent.behaviorList.get(i) instanceof IRememberBehavior)
				((IRememberBehavior) parent.behaviorList.get(i)).save();
		}
		
		System.exit(0);
	}
}
