import java.io.IOException;
import java.lang.reflect.Field;

/**
* For SNES games to be run in an Emulator
*/
public class GameSnes extends Game {
	public GameSnes() {
		super();
		setType(TYPE_SNES);
		setDir(meta.dirSnes);
	}

	public long launch() {
		long pid = -1;

		//TODO Install/Prompt: https://sourceforge.net/projects/pisnes/

		String path = "\"" + filePath() + "\"";
		String cmd = meta.dirBash + "/pisnes/snes9x " + path;
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
