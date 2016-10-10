/**
* For Play Station X games to be run in an Emulator
*/
public class GamePsx extends Game {
	public GamePsx() {
		super();
		setType(TYPE_PSX);
		setDir(meta.dirPSX);
	}
}
