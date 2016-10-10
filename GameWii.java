/**
* For Wii games to be run in an Emulator
*/
public class GameWii extends Game {
	public GameWii() {
		super();
		setType(TYPE_WII);
		setDir(meta.dirWii);
	}
}
