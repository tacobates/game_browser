/**
* For DOS games to be run in Dosbox
*/
public class GameDos extends Game {
	public GameDos() {
		super();
		setType(TYPE_DOS);
	}

	public int launch() {
		//TODO: launch specific to DOS
		int pid = 1234; //TODO: get real pid for process tracking
		return pid;
	}
}
