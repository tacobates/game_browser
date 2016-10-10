/**
* For Gamecube games to be run in an Emulator
*/
public class GameGC extends Game {
	public GameGC() {
		super();
		setType(TYPE_GC);
		setDir(meta.dirGC);
	}
}
