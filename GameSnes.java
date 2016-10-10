/**
* For SNES games to be run in an Emulator
*/
public class GameSnes extends Game {
	public GameSnes() {
		super();
		setType(TYPE_SNES);
		setDir(meta.dirSnes);
	}
}
