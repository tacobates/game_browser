/**
* For Gameboy Advanced games to be run in an Emulator
*/
public class GameGba extends Game {
	public GameGba() {
		super();
		setType(TYPE_GBA);
		setDir(meta.dirGBA);
	}
}
