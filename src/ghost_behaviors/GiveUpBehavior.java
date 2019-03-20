package ghost_behaviors;

import ghostpet.GhostBase;
import ghostpet.GhostState;

public class GiveUpBehavior extends Behavior {

	//NOTE: Use this class for any Ask behavior as the "cancel" or "never mind" option, and for when the ask "times out."
	
	public GiveUpBehavior(GhostBase frame) {
		super(frame);
	}

	public GiveUpBehavior() {
		entersState = GhostState.Speaking;
	}

	//@Override
	public void Execute() {
		parent.Say(this);
	}
	/*
	 * Executes when: the user either times out the ask or selects a "never mind" option 
	 */
}
