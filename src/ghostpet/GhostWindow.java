package ghostpet;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.JWindow;

public class GhostWindow extends JWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public GhostWindow() {
		// TODO Auto-generated constructor stub
		init();
	}

	public GhostWindow(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		init();
	}

	public GhostWindow(Frame arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		init();
	}

	public GhostWindow(Window arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		init();
	}

	public GhostWindow(Window arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public GhostBase ghost;
	public void init()
	{
		setAlwaysOnTop(true);
		setBackground(new Color(0,0,0,0));
		
		ghost = new GhostBase(this);
		getContentPane().add(ghost);
        
        setSize(ghost.getWidth(), ghost.getHeight());
        
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect = ge.getMaximumWindowBounds();
		int x = (int) rect.getMaxX() - getWidth() - 10;
        int y = (int) rect.getMaxY() - getHeight();
        setLocation(x, y);
        getContentPane().setLayout(null);
	}
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
            		GhostWindow w = new GhostWindow();
            		w.setVisible(true);
            	} catch(Exception e){
            		e.printStackTrace();
            	}
            }
        });
    }
}
