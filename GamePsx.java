/**
* For Play Station X games to be run in an Emulator
*/
public class GamePsx extends Game {
	public GamePsx(String conf) throws Exception {
		super(conf);
		type = TYPE_PSX;
	}
}
