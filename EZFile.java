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
	* @param String path: Full path to text file
	* @param String onNull: [opt] String to return in the case of error
	*/
	public static String readFile(String path) { return readFile(path, ""); }
	public static String readFile(String path, String onNull) {
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
		} catch (Exception e) {
			rtn = onNull;
		}

		return rtn;
	}

	/**
	* Returns the contents of a TSV file as a List of String[]
	* @param String path: Full path of file to read
	* @param int skip: [opt] number of rows to skip (header rows)
	*/
	public static ArrayList readTSV(String path) {return readTSV(path, 0);}
	public static ArrayList readTSV(String path, int skip) {
		int i = 1;
		ArrayList<String[]> rtn = new ArrayList();
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line = br.readLine();
			while (line != null) {
				if (line.length() > 1 && i++ > skip) { //Skip lines with no data
					rtn.add(line.split("\\t"));
				}
				line = br.readLine();
			}
		} catch (Exception e) {
		}

		return rtn;
	}

	/**
	* Returns true if the path exists, false otherwise
	* @param String path: Full path of file to read
	*/
	public static boolean pathExists(String path) {
		File f = new File(path);
		if (f.exists())
			return true;
		return false;
	}
}

