package ghostpet;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import ghost_behaviors.Behavior;
import ghost_behaviors.Behavior_Ask;
import ghost_behaviors.Behavior_LongTalk;
import ghost_behaviors.Behavior_Randomizer;
import ghost_behaviors.Behavior_Sequence;
import ghost_behaviors.Behavior_Timed;
import ghost_behaviors.SleepBehavior;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GhostBase extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 557122829397947123L;
	
	GhostPanel gpanel;
	SpeakPanel spanel;
	Container parent;
	
	Timer InnerClock;
	Timer TimedBehaviorClock;
	
	Behavior current; //Necessary for implementing sequences.
	
	public GhostState state;
	public GhostJson data;
	public ArrayList<Behavior> behaviorList;
	
	@SuppressWarnings("unchecked") //Yes, it's bad practice, but in this case it works. The cast from Behavior to T DOES get checked in the if statement before the return. Do NOT pass in one of the abstract classes.
	public <T extends Behavior> T getBehavior(Class<T> cls)
	{
		for(int i = 0; i < behaviorList.size(); i++)
		{
			if(behaviorList.get(i).getClass().isAssignableFrom(cls))
			{
				return (T) behaviorList.get(i);
			}
		}
		return null;
	}
	
	public <T extends Behavior> void MakeBehaviors()
	{
		File resFolder = new File("resources");
		File[] resourceList = resFolder.listFiles();
		behaviorList = new ArrayList<Behavior>(resourceList.length); //Definitely longer than needed, but also definitely not SHORTER than needed, and this way the number doesn't need to be changed.
		String jsonFileName;
		T behave;
		for(int i = 0; i < resourceList.length; i++)
		{
			jsonFileName = resourceList[i].getName();
			
			if(jsonFileName.indexOf("Behavior") == -1) //If "Behavior" isn't in there, it's not a behavior data file, and can thus be discounted.
				continue;
			
			jsonFileName = jsonFileName.substring(0, jsonFileName.length()-5); //Removes ".json"
			jsonFileName = "ghost_behaviors."+jsonFileName;
			
			try {
				@SuppressWarnings("unchecked") //Bad practice, but I've manually ensured that EVERY .json Behavior file can be translated to the class name with the above code. Also, it's in a try block anyway.
				Class<T> C = (Class<T>) Class.forName(jsonFileName);
				
				behave = C.newInstance();
				behave = behave.loadInfo(this);
				behaviorList.add(behave);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}	
		}
		for(Behavior b : behaviorList)
		{
			b.setup();
		}
	}
	
	public GhostBase(Container frame) {
		parent = frame;
		
		data = GhostJson.buildGson(); 
		data.defImg = ResizeImg(data.defImg);
		InnerClock = new Timer(60, null);
		InnerClock.setRepeats(false);
		
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TimedBehaviorTick();
			}
		};
		TimedBehaviorClock = new Timer(1000, taskPerformer);
		TimedBehaviorClock.start();
		state = GhostState.Passive;
		MakeBehaviors();
		
		constructPanel();
	}
	
	private void constructPanel()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		spanel = new SpeakPanel(this);
		GridBagConstraints gbc_spanel = new GridBagConstraints();
		gbc_spanel.weighty = 1.0;
		gbc_spanel.fill = GridBagConstraints.BOTH;
		gbc_spanel.gridx = 0;
		gbc_spanel.gridy = 0;
		add(spanel, gbc_spanel);
		
		gpanel = new GhostPanel(this);
		GridBagConstraints gbc_gpanel = new GridBagConstraints();
		gbc_gpanel.weightx = 1.0;
		gbc_gpanel.gridx = 1;
		gbc_gpanel.gridy = 0;
		gbc_gpanel.anchor = GridBagConstraints.SOUTHEAST;
		add(gpanel, gbc_gpanel);

		setBackground(new Color(0,0,0,0));
		this.setSize(new Dimension(gpanel.getWidth()+spanel.getWidth(), spanel.getHeight()));
		
		revalidate();
	}
	
	public ImageIcon ResizeImg(ImageIcon i)
	{
		int w;
		int h;
		
		if(data.maxHeight >= i.getIconHeight() || data.maxHeight == 0)
			h = i.getIconHeight();
		else
			h = data.maxHeight; 
		if(data.maxWidth >= i.getIconWidth() || data.maxWidth == 0)
			w = i.getIconWidth();
		else
			w = data.maxWidth;
		if(h == i.getIconHeight() && w == i.getIconWidth())
			return i;
		
		
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(i.getImage(), 0, 0, w, h, null);
	    g2.dispose();
	    
	    ImageIcon r = new ImageIcon(resizedImg);
		
		return r;
	}
	
	public <T extends Behavior> Boolean AskResultSay(T b)
	{
		if(state != GhostState.Asking)
		{
			return false;
		}
		else
		{
			SayAction(b);
			return true;
		}
	}
	
	public <T extends Behavior> void Say(T b)
	{
		if(b == null)
			return;
		if(((state == GhostState.LongTalk) && (b instanceof Behavior_LongTalk)) || (state == GhostState.Passive))
		{
			SayAction(b);
		}
	}
	
	public <T extends Behavior> void SayAction(T b)
	{
		InnerClock.stop();
		InnerClock = new Timer(60, null); //Resets the timer to clear out all action listeners
		Clear();
		
		if(b.entersState != null) {
			state = b.entersState;
		}
			
		
		if(b instanceof Behavior_Ask)
		{
			AskSay((Behavior_Ask) b);
			InnerClock.setInitialDelay(Math.round(b.actTime*1000));
			InnerClock.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					InnerClock.stop();
					if(((Behavior_Ask) b).giveUpBehavior != null) //Only executes if it's not null. If it is, just skips it.
						AskResultSay(((Behavior_Ask) b).giveUpBehavior);
					else {
						Reset();
					}
						
				}
			});
			InnerClock.start();
		}
		else if(b instanceof Behavior_LongTalk)
		{
			if(((Behavior_LongTalk) b).isMaxed())
			{
				Say(((Behavior_LongTalk) b).getNextLine(), ((Behavior_LongTalk) b).getNextImage());
				InnerClock.setInitialDelay(Math.round(((Behavior_LongTalk) b).getNextTime() * 1000));
				InnerClock.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						InnerClock.stop();
						Say(b);
					}
				});
				InnerClock.start();
				((Behavior_LongTalk) b).inc++;
			}
			else
			{
				((Behavior_LongTalk) b).inc = 0;
				((Behavior_LongTalk) b).setNum();
				Reset();
			}
		}
		else if(b instanceof Behavior_Timed)
			Say(((Behavior_Timed) b).b); //Cycles back, allowing timed behaviors to essentially just be timed containers for the real behaviors.
		else if(b instanceof Behavior_Randomizer)
		{
			((Behavior_Randomizer) b).SelectRandom();
		}
		else if(b instanceof Behavior_Sequence)
		{
			if(((Behavior_Sequence) b).ExecNextSequence())
			{
				current = null;
			}
			else 
			{
				current = b;
			}
		}
		else if(b instanceof SleepBehavior) //Last special case. Used specifically for shutting down the program.
		{
			Say(b.getRandStmt(), b.getImg());
			InnerClock.setInitialDelay(Math.round(b.actTime*1000));
			InnerClock.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					InnerClock.stop();
					Reset();
					((SleepBehavior) b).Execute();
				}
			});
			InnerClock.start();
		}
 		else
		{
			Say(b.getRandStmt(), b.getImg());
			InnerClock.setInitialDelay(Math.round(b.actTime*1000));
			InnerClock.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					InnerClock.stop();
					Reset();
				}
			});
			InnerClock.start();
		}
	}
	
	public void TimedBehaviorTick()
	{
		for(int i = 0; i < behaviorList.size(); i++)
		{
			if(behaviorList.get(i) instanceof Behavior_Timed)
			{
				((Behavior_Timed) behaviorList.get(i)).tick();;
			}
		}
	}
	
	public void AskSay(Behavior_Ask b)
	{
		spanel.Say(b.getRandStmt(), b.opts);
		gpanel.Say(b.getImg());
		parent.repaint();
	}
	public void Say(String state, ImageIcon in)
	{
		spanel.Say(state);		
		gpanel.Say(in);
		parent.repaint();
	}
	public void Clear()
	{
		spanel.Reset();
		gpanel.Reset();
		parent.repaint();
	}
	public void Reset() //Specifically only gets called when a behavior is done, thus ensuring that if the behavior is in a sequence it'll only call the next behavior when it's ready.
	{
		Clear();
		state = GhostState.Passive;
		if(current != null)
		{
			Say(current);
		}
	}
}
