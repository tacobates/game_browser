import java.io.IOException;
import java.lang.reflect.Field;

/**
* For standard Linux games
*/
public class GameX11 extends Game {
	public GameX11() {
		super();
		setType(TYPE_X11);
	}

	public long launch() {
System.out.println("Trying to launch " + getName() + " (ID: " +
Integer.toString(getID()) + ")");
		long pid = -1;

String path = "/usr/games/_education/Arithmetic.sh";
		try {
			Runtime rt = Runtime.getRuntime();
			String[] cmdArray = {"lxterminal", "-e", path};
			Process p = rt.exec(cmdArray); //.waitFor();

			if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
				Field f = p.getClass().getDeclaredField("pid");
				f.setAccessible(true);
				pid = f.getLong(p);
				f.setAccessible(false);
System.out.println("Pid: " + Long.toString(pid));
			}
		} catch (Exception e) {
System.out.println("###ERROR: Generic###");
System.out.println(e);
		}

		return pid;
	}
}
