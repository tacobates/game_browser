import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import javax.swing.*;

/**
* Renders a game for the Browser pane
*/
public class RenderBrowse extends RenderGame {
	/********* Constants *********/
//TYPE_{BASH,X11,DOS,NES,SNES,N64,GC,WII,WU,NX,GB,GBA,DS,DS3,PSX}
//String[] TYPES = {ordinal list of names for TYPE_*}

	/********* Variables *********/
	private Color cBlue = new Color(132,193,255);
	private Color cGray = new Color(230,230,230);
	private Color cWhite = new Color(245,245,245);


	/********* Methods *********/

	/**
	* Creates an empty RenderBrowse
	* @param Browser b: reference to the parent display frame
	*/
	public RenderBrowse(Browser b) {
		super(b);
		doResize();
		defaultBackground();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (game.getName() == null)
					return; //Do nothing for empty games
				browser.setDetailGame(game); //TODO: something like this
				browser.card(2);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if (game.getName() == null)
					return; //Do nothing for empty games
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
		setLayout(new GridLayout(1,2));
		name.setFont(name.getFont().deriveFont(14.0f));
		yearP.setFont(name.getFont().deriveFont(14.0f));

		icon = new JLabel();
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(icon);
		p1.add(name);
		p1.add(yearP);

		JPanel p2 = new JPanel(new GridLayout(1,2));
		p2.add(type);
		p2.add(genre);
		p2.add(numP);
		p2.add(rating);

		add(p1);
		add(p2);
	}

	/**
	* Refreshes the GUI elements with their new data
	*/
	@Override
	public void refreshGUI() {
		String p = meta.getDirRoot() + meta.DIR_ICON + "/";
		String p2 = p + Integer.toString(game.getID()) + ".png";
		File f = new File(p2);
		if (game.getName() == null)
			icon.setIcon(new ImageIcon()); //Blank icon
		else if (f.exists())
			icon.setIcon(new ImageIcon(p2));
		else //No icon
			icon.setIcon(new ImageIcon(p + "0.png"));

		super.refreshGUI(); //at end to trigger repaint when done
	}

	/**
	*
	*/
	public void doResize() {
		Dimension d = browser.getContentPane().getSize();
		d.setSize(d.width - meta.PAD_W2, meta.ICON_H + meta.ICON_PAD);
		setSize(d);
		setPreferredSize(d);
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
//TODO: put in RenderGame and just delegate to Game.launch()
	}
}

