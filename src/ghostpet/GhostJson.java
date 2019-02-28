package ghostpet;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import javax.swing.ImageIcon;

import com.google.gson.*;

public class GhostJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6533652617060291305L;
	public String ghostName;
	public String UserName;
	public String DefaultImg;
	public String TxtBoxImg;
	public String FontFile;
	
	public int maxHeight;
	public int maxWidth;
	
	public transient ImageIcon defImg;
	public transient ImageIcon TextBox;
	public transient Font font = null;
	
	GhostJson()
	{
		 
	}
	
	public static GhostJson buildGson()
	{
		Gson gson = new GsonBuilder().create();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("./resources/GhostJson.json")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		GhostJson data = gson.fromJson(br, GhostJson.class);
		data.defImg = new ImageIcon("./resources/images/"+data.DefaultImg);
		data.TextBox = new ImageIcon("./resources/images/"+data.TxtBoxImg);
		if(data.FontFile != null) //Yes, Font is allowed to be null if you just want to use the default.
		{
			try {
				data.font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("./resources/"+data.FontFile));
				data.font = data.font.deriveFont((float) 12.00); //I don't know why, but (at least for the font I used in testing) it sets it to 1-point size. So I set it to 12 instead.
			} catch (FontFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
}