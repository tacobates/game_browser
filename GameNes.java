import java.io.IOException;
import java.lang.reflect.Field;

/**
* For NES games to be run in an Emulator
*/
public class GameNes extends Game {
	public GameNes() {
		super();
		setType(TYPE_NES);
		setDir(meta.dirNes);
	}

	public long launch() {
		long pid = -1;
		String gg = "";
		if (true) //TODO: check flag for Game Genie
			gg = "-gg 1 ";

		//TODO: check that fceux is installed
		//TODO: if not "sudo apt-get install fceux"

		//Options: www.fceux.com/web/help/fceux.html?CommandLineOptions.html
		String path = "\"" + filePath() + "\"";
		String cmd = "fceux " + gg + path;
//System.out.println(cmd);

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
