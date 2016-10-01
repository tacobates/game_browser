/**
* For Wii games to be run in an Emulator
*/
public class GameWii extends Game {
	public GameWii(String conf) throws Exception {
		super(conf);
		type = TYPE_WII;
	}
}
