/**
* For Nintendo DS games to be run in an Emulator
*/
public class GameDS extends Game {
	public GameDS(String conf) throws Exception {
		super(conf);
		type = TYPE_DS;
	}
}
