package ghost_behaviors;

import ghostpet.*;

public class PetBehavior extends Behavior {
	
	int PetStrokes;
	transient int StrokeTrack = 0;
	
	public PetBehavior(GhostBase frame)
	{
		super(frame);
	}
	
	public PetBehavior()
	{
		entersState = GhostState.Petted;
	}
	
	public void Execute() {
		if(parent.state != GhostState.Petted)
		{
			StrokeTrack++;
			if(StrokeTrack == PetStrokes)
			{
				parent.Say(this);
				StrokeTrack = 0;
			}
		}
	}
	
	/*
	 * Execution: 
	 * Executes every time a MouseMove triggers over the GhostPanel.
	 */
}
