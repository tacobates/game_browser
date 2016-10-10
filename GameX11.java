import java.io.IOException;
import java.lang.reflect.Field;

/**
* For standard Linux games
*/
public class GameX11 extends Game {
	public GameX11() {
		super();
		setType(TYPE_X11);
		setDir(meta.dirX11);
	}

	public long launch() {
		long pid = -1;

		String path = getFile();
		//Add default directory
		if (!path.startsWith("/"))
			path = dir + meta.SEP + path;

		try {
			Runtime rt = Runtime.getRuntime();
			String[] cmdArray = {"lxterminal", "-e", path};
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
