package ghost_behaviors;

import java.util.Random;

import javax.swing.ImageIcon;

import ghostpet.GhostBase;
import ghostpet.GhostState;

public class Behavior_LongTalk extends Behavior {
	
	private class ChainGroup
	{
		public String[] stmts;
		public float[] times;
		String[] imgStrs;
		public transient ImageIcon[] imgs;
	}
	
	public Boolean hasSetValue;
	public ChainGroup[] chains;
	
	public transient int inc = 0;
	public transient int numUsed;
	
	public Behavior_LongTalk(GhostBase frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	public Behavior_LongTalk() {
		entersState = GhostState.Speaking;
		// TODO Auto-generated constructor stub
	}
	
	public void setup()
	{
		ImageIcon prev = null;
		for(int j = 0; j < chains.length; j++)
		{
			chains[j].imgs = new ImageIcon[chains[j].imgStrs.length];
			for(int i = 0; i < chains[j].imgStrs.length; i++)
			{
				if(chains[j].imgStrs[i] == null)
					chains[j].imgs[i] = prev;
				else
				{
					chains[j].imgs[i] = parent.ResizeImg(new ImageIcon("./resources/images/"+chains[j].imgStrs[i]));
					prev = chains[j].imgs[i];
				}
			}
		}
		setNum();
	}
	
	@Override
	public <T extends Behavior> T loadInfo(GhostBase frame) {		
		T returner = super.loadInfo(frame);
		((Behavior_LongTalk)returner).setup();
		return returner;
	}
	
	public void setNum() //If it's not calculated, randomizes it. If it is, calls calculate() - which currently just randomizes it, but you can override that in your code.
	{
		if(hasSetValue)
			numUsed = calculate();
		else
		{
			Random r = new Random();
			numUsed = r.nextInt(chains.length);
		}
	}
	public void setNum(int i)
	{
		numUsed = i;
	}
	public int calculate() //Override this if you actually want to have the value of numUsed dependent on anything.
	{
		Random r = new Random();
		return r.nextInt(chains.length);
	}
	
	public Boolean isMaxed()
	{
		return(chains[numUsed].stmts.length > inc);
	}
	public String getNextLine()
	{
		return chains[numUsed].stmts[inc];
	}
	public float getNextTime()
	{
		return chains[numUsed].times[inc];
	}
	public ImageIcon getNextImage()
	{
		return chains[numUsed].imgs[inc];
	}
}
