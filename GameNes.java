/**
* For NES games to be run in an Emulator
*/
public class GameNes extends Game {
	public GameNes(String conf) throws Exception {
		super(conf);
		type = TYPE_NES;
	}
}
