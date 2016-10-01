/**
* For N64 games to be run in an Emulator
*/
public class GameN64 extends Game {
	public GameN64(String conf) throws Exception {
		super(conf);
		type = TYPE_N64;
	}
}
