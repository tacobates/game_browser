
import java.awt.Image;
import javax.swing.*;

/**
* JPanel containing a rendered representation of a Game object.
* This object can then be added to a GUI to render the Game it contains.
* Game objects are prolific, so they need to be very lightweight in memory.
* This object is a little heavier, but will be reused on each Browser page.
*
* TODO: this class can then be added to our GUI
* TODO: we'll make an array of them to use when paging.
*   -if page isn't full, render a few empty games
*/
public class RenderGame extends JPanel {
	/********* Constants *********/
//TYPE_{BASH,X11,DOS,NES,SNES,N64,GC,WII,WU,NX,GB,GBA,DS,DS3,PSX}
//String[] TYPES = {ordinal list of names for TYPE_*}

	/********* Variables *********/
	protected static int counter = 0;
	protected int row = 0;

	protected final Browser browser;
	protected Game game;
	protected ImageIcon icon = new ImageIcon();
	protected JLabel descrip = new JLabel();
	protected JLabel genre = new JLabel();
	protected JLabel name = new JLabel("TODO: deleteme!!!");
	protected JLabel numP = new JLabel();
	protected JLabel rating = new JLabel(); //TODO: switch to pictoral represent
	protected JLabel type = new JLabel();
	protected Meta meta;

	/********* Getters *********/
	public Game getGame() { return game; }

	/********* Setters *********/
	public void setGame(Game g) { game = g; refreshGUI(); }


	/********* Methods *********/

	/**
	* Creates an empty RenderGame
	* @param Browser b: reference to the parent display frame
	*/
	public RenderGame(Browser b) {
		browser = b;
		row = counter++;
		meta = Meta.getInstance(); //Holds Game Data & Conf
		initLayout();
	}

	/**
	* Adds the GUI elements (will be overriden by subclasses)
	*/
	public void initLayout() {
		add(name);
	}

	/**
	* Refreshes the GUI elements to display current Game data
	*/
	public void refreshGUI() {
		//TODO: make legit
		descrip.setText(game.getDescription());
		genre.setText(game.getGenre());
		String y = " (" + Integer.toString(game.getYear()) + ")";
		name.setText(game.getName() + y);
		numP.setText(Integer.toString(game.getNumPlayers()));
		rating.setText(Double.toString(game.getRating()));
//TODO: precision 1 on Double???
		type.setText(game.getTypeName());

System.out.println("Game set to: " + game.getName() + " (" + game.getTypeName() + ")");

		repaint(); //TODO: do this? Or let JFrame do it?
//TODO: JFrame Browser will handle pack() after it sets all of our Game objs
	}
}

