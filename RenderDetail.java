import java.awt.Image;
import javax.swing.*;

/**
* Renders a game for the detail pane
*/
public class RenderDetail extends JPanel {
	/********* Constants *********/
//DEF_DIR    = "/usr/share/games";
//DEF_ICON  = "/_icon";
//DEF_SCREEN = "/_screen";
//TYPE_{BASH,X11,DOS,NES,SNES,N64,GC,WII,WU,NX,GB,GBA,DS,DS3,PSX}
//String[] TYPES = {ordinal list of names for TYPE_*}

	/********* Variables *********/
	private JButton bTemp1 = new JButton();
	private JLabel descrip = new JLabel();
//TODO: 1 object to render screenshots, but controls to thumb through more


	/********* Methods *********/

	/**
	* Creates an empty RenderDetail
	* @param Browser b: reference to the parent display frame
	*/
	public RenderDetail(Browser b) {
		super(b);
		initGUI();
	}

	/**
	* Instantiates the GUI elements to display Game data
	*/
	public void initGUI() {
		//TODO: make legit
bTemp1 = new JButton("Temp 1");
bTemp2 = new JButton("Temp 2");
		
	}

	public int launch() {
		System.out.println("This type of game is not yet supported.");
		return 0; //TODO: get and return the PID so we can track run time?
	}
}

