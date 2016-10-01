/**
* For N64 games to be run in an Emulator
*/
public class GameNX extends Game {
	public GameNX(String conf) throws Exception {
		super(conf);
		type = TYPE_NX;
	}
}
