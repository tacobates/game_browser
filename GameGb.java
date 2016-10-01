/**
* For Gameboy games to be run in an Emulator
*/
public class GameGb extends Game {
	public GameGb(String conf) throws Exception {
		super(conf);
		type = TYPE_GB;
	}
}
