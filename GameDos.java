import java.io.IOException;
import java.lang.reflect.Field;

/**
* For DOS games to be run in Dosbox
*/
public class GameDos extends Game {
	public GameDos() {
		super();
		setType(TYPE_DOS);
		setDir(meta.dirDos);
	}

	public long launch() {
		long pid = -1;
		EZFile ez = EZFile.getInstance();
		String cmd = "";
		String conf = dir + "/_conf/temp.conf";
		String path = filePath();
		String epath = "\"" + path + "\"";
		String templateP = this.getClass().getResource("/templates/dos.conf")
			.getPath();
		String template = ez.readFile(templateP); //TODO: test in JAR
		String zip = getFile();
		String zipName = Integer.toString(getID());
			//zip.substring(0, zip.length() - 4);
			//Can't use zipName as DOS can only handle 8 char names

//zipName = "carmen_europe"; //TODO: delete this test line
String LAUNCHER = "carmen.exe"; //TODO: get these from config

//TODO: write any special config (like slowdown for bcw)
		//Write conf file
		template += "\n" +
			"mount c " + dir + "\n" +
			"c:\n" +
			"cd _inst\n" + 
			"cd " + zipName + "\n" +
			LAUNCHER + "\n";
		boolean written = ez.writeFile(template, conf);
		if (!written) {
			System.out.println("Couldn't write Dosbox conf file: " + conf);
			return pid;
		}

		//TODO: check that dosbox is installed
		//TODO: if not "sudo apt-get install dosbox"

		//Ensure Unzipped
		String dest = dir + "/_inst/" + zipName;
		String edest = "\"" + dir + "/_inst/" + zipName + "\""; //escaped
		if (!ez.pathExists(dest))
			cmd = "unzip " + epath + " -d " + edest + " && ";

		//Options: dosbox.com/wiki/usage#Command_Line_Parameters
		cmd += "dosbox -fullscreen -conf " + conf;
System.out.println(cmd);

		try {
			Runtime rt = Runtime.getRuntime();
			String[] cmdArray = {"lxterminal", "-e", cmd};
			Process p = rt.exec(cmdArray);

			if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
				Field f = p.getClass().getDeclaredField("pid");
				f.setAccessible(true);
				pid = f.getLong(p);
				f.setAccessible(false);
				System.out.println("Pid: " + Long.toString(pid));
			}

			p.waitFor();
		} catch (Exception e) {
			System.out.println("###ERROR: Generic###");
			System.out.println(e);
		}

		return pid;
	}
}
