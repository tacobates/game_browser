/**
* For Gameboy games to be run in an Emulator
*/
public class GameGb extends Game {
	public GameGb() {
		super();
		setType(TYPE_GB);
		setDir(meta.dirGB);
	}
}
