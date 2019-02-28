package ghostpet;

import javax.swing.Icon;
import javax.swing.JLabel;


public class AskChoiceLabel<T> extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3327413298397045092L;
	Class<T> cls;
	
	public AskChoiceLabel() {
		// TODO Auto-generated constructor stub
	}

	public AskChoiceLabel(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public AskChoiceLabel(Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	public AskChoiceLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public AskChoiceLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public AskChoiceLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public AskChoiceLabel(String text, T behave)
	{
		super(text);
		cls = (Class<T>) behave.getClass();
	}

}
