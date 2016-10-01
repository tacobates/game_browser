/**
* For Wii U games to be run in an Emulator
*/
public class GameWU extends Game {
	public GameWU(String conf) throws Exception {
		super(conf);
		type = TYPE_WU;
	}
}
