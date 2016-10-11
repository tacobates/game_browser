import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	* Writes a String to a file path. Returns true if successful.
	* @param String path: Full path to text file
	* @param String path: Full path to text file
	*/
	public static boolean writeFile(String txt, String path) {
		try {
			FileWriter fw = new FileWriter(path, false);
			fw.write(txt);
			fw.close();
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
		return true;
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

	/**
	* Extracts a zip file to a path. Returns true if successful.
	* @param zip: full path of file to extract
	* @param dest: full path to extract to
	*/
	public boolean unzip(String zip, String dest) {
System.out.println("Extracting " + zip + " ==> " + dest);
		try {
			File f = new File(dest);
			if (!f.exists())
				f.mkdir();
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
			ZipEntry entry = zis.getNextEntry();
			while (entry != null) { //foreach entry in the zip
				String ePath = dest + File.separator + entry.getName();
				if (entry.isDirectory()) {
					File dir = new File(ePath); //make a matching sub-dir
					dir.mkdir();
				} else { //Extract the entry's file
					byte[] chunk = new byte[4096];
					int read = 0;
					BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(ePath));
					while ((read = zis.read(chunk)) != -1)
						bos.write(chunk, 0, read);
					bos.close();
				}
				zis.closeEntry();
				entry = zis.getNextEntry();
			}
			zis.close();
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
		return true; //Made it to the end with no Exceptions
	}
}

