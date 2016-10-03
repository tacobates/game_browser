import java.io.*;
import java.util.*;

/**
* Easy file reading, including TSV files
*/
public final class EZFile {
	private static EZFile instance = null; //Singleton

	/**
	* Singleton empty constructor
	*/
	protected EZFile() {
		//Do nothing
	}

	/**
	* Gets the Singleton instance
	*/
	public static EZFile getInstance() {
		if (instance == null)
			instance = new EZFile();
		return instance;
	}

	/**
	* Return the contents of a file as a String ("" if there is a problem)
	*/
	public static String readFile(String path) {
		String rtn = "";
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			rtn = sb.toString();
System.out.println(rtn); //TODO: delete after testing
		} catch (Exception e) {
			//Do nothing, as rtn is already ""
		}

		return rtn;
	}

	/**
	* Assume no skipping of first TSV row
	*/
	public static ArrayList readTSV(String path) {
		return readTSV(path, false);
	}
	/**
	* Returns the contents of a TSV file as a List of String[]
	*/
	public static ArrayList readTSV(String path, boolean skipRow1) {
		ArrayList<String[]> rtn = new ArrayList();
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line = br.readLine();
			while (line != null) {
				if (line.length() > 1 && !skipRow1) { //Skip lines with no data
					rtn.add(line.split("\\t"));
				}
				line = br.readLine();
				skipRow1 = false; //Whether or not it was ever true
			}
		} catch (Exception e) {
		}

		return rtn;
	}
}

