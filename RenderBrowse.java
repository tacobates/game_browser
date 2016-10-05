import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
* Renders a game for the Browser pane
*/
public class RenderBrowse extends RenderGame {
	/********* Constants *********/
//TYPE_{BASH,X11,DOS,NES,SNES,N64,GC,WII,WU,NX,GB,GBA,DS,DS3,PSX}
//String[] TYPES = {ordinal list of names for TYPE_*}

	/********* Variables *********/
	private Color cBlue = new Color(124,174,255);
	private Color cGray = new Color(220,220,220);
	private Color cWhite = new Color(245,245,245);
	private ImageIcon icon = new ImageIcon();
	private JButton bTemp1 = new JButton();
	private JLabel lTemp1 = new JLabel();


	/********* Methods *********/

	/**
	* Creates an empty RenderBrowse
	* @param Browser b: reference to the parent display frame
	*/
	public RenderBrowse(Browser b) {
		super(b);
		defaultBackground();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
System.out.println("Show details for "+ game.getName());
				//browser.setDetailGame(game); //TODO: something like this
				browser.card(2);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(cBlue);
				repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				defaultBackground();
				repaint();
			}
		});
	}

	/**
	* Instantiates the GUI elements to display Game data
	*/
	@Override
	public void initLayout() {
		add(name);
		add(year);
		add(type);
		add(genre);
		add(rating);
		add(numP);
	}

	/**
	* Refreshes the GUI elements with their new data
	*/
	@Override
	public void refreshGUI() {
//TODO: make legit
		super.refreshGUI(); //at end to trigger repaint when done
	}

	/**
	* Sets the background color for this panel based on the row number
	*/
	public void defaultBackground() {
		setBackground(row % 2 == 0 ? cWhite : cGray);
	}

	public int launch() {
		System.out.println("This type of game is not yet supported.");
		return 0; //TODO: get and return the PID so we can track run time?
	}
}

