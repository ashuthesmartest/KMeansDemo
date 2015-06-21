package tasty.minerals.kmeans.datareader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataReader {
	private final String filename;
	private String line = null;
	private StringBuilder content = new StringBuilder();

	public DataReader(String fname) {
		filename = fname;
	}

	/* Reading data from file and returning a String */
	public String readData() {
		InputStream stream = DataReader.class.getResourceAsStream(filename);
		try (BufferedReader bf = new BufferedReader(new InputStreamReader(
				stream))) {

			while ((line = bf.readLine()) != null) {
				content.append(line);
				content.append(System.getProperty("line.separator"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
