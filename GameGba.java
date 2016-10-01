/**
* For Gameboy Advanced games to be run in an Emulator
*/
public class GameGba extends Game {
	public GameGba(String conf) throws Exception {
		super(conf);
		type = TYPE_GBA;
	}
}
