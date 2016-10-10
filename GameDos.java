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
		//TODO: launch specific to DOS
		long pid = -1; //TODO: get real pid for process tracking
		return pid;
	}
}
