/**
* For N64 games to be run in an Emulator
*/
public class GameNX extends Game {
	public GameNX() {
		super();
		setType(TYPE_NX);
		setDir(meta.dirNX);
	}
}
