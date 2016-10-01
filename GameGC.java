/**
* For Gamecube games to be run in an Emulator
*/
public class GameGC extends Game {
	public GameGC(String conf) throws Exception {
		super(conf);
		type = TYPE_GC;
	}
}
