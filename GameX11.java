/**
* For standard Linux games
*/
public class GameX11 extends Game {
	public GameX11(String conf) throws Exception {
		super(conf);
		type = TYPE_X11;
	}
}
