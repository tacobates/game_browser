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

		String path = filePath();
//TODO: check that fceux is installed
		String cmd = "fceux \"" + path + "\"";
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
