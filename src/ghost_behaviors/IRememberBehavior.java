package ghost_behaviors;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

public interface IRememberBehavior {
	
	public default void save()
	{
		String filename = this.getClass().getSimpleName();
		filename = "./resources/"+filename+".json";
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		try {
			FileWriter fr = new FileWriter(filename);
			JsonWriter js = gson.newJsonWriter(fr);
			gson.toJson(this, this.getClass(), js);
			js.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void calcValues();
}
