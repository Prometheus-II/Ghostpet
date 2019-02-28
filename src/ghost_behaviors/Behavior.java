package ghost_behaviors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import javax.swing.ImageIcon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ghostpet.*;
import ghostpet.GhostBase;

public abstract class Behavior {
	
	public String imageUsed; //The image the ghost displays while performing your behavior.
	String[] statements; //The phrases the ghost can say (selected randomly) when it begins execution of the behavior.
	public float actTime; //How long the ghost performs your behavior for, in seconds. -1 to make it last forever, but ONLY USE THAT IF THE GHOST WILL BE WAITING FOR USER INPUT, and even then it's not recommended - give it a few minutes at most.
	
	public transient GhostBase parent; //marked transient so that Gson won't panic trying to read or save it.
	private transient ImageIcon img;
	public transient GhostState entersState;
	
	public Behavior(GhostBase frame) 
	{
		parent = frame;
	} 
	
	public Behavior()
	{
		
	}
	
	public String getRandStmt()
	{
		Random r = new Random();
		int t = r.nextInt(statements.length);
		String out = statements[t];

		return out;
	}
	
	//To load up an instance of a Behavior-derived class: create the used object using the first constructor (that takes the parent frame), create another instance using the blank
	//constructor and loadInfo, then use set to make the two equal. It's convoluted, but it works. I hope.
	@SuppressWarnings("unchecked")
	public <T extends Behavior> T loadInfo(GhostBase frame)
	{
		String filename = this.getClass().getSimpleName();
		filename = "./resources/"+filename+".json";
		Gson gson = new GsonBuilder().create();
		BufferedReader br = null; //Just so it stops whining about "may not have been initialized"
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		T returner = (T) gson.fromJson(br, this.getClass());
		returner.parent = frame;
		if(returner.imageUsed != null)
			returner.setImg(returner.parent.ResizeImg(new ImageIcon("./resources/images/"+returner.imageUsed)));
		return returner;
	}

	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	
	@Override
	public String toString()
	{
		return this.getClass().getSimpleName();
	}
	
	/*
	 * Execution: 
	 * In your inherited class, you should write in the conditions under which your class executes here. It's just a comment, but it helps you and others keep track.
	 * To make it ACTUALLY execute, you'll need to write the "triggering" code for your behavior in the right place. I've included a few examples in README.txt if you're not the
	 * most technically inclined but still want to do this. 
	 */
}
