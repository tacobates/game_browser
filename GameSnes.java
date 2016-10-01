/**
* For SNES games to be run in an Emulator
*/
public class GameSnes extends Game {
	public GameSnes(String conf) throws Exception {
		super(conf);
		type = TYPE_SNES;
	}
}
