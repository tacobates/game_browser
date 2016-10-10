import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

	private JPanel p1; //Items displayed in the row
	private JPanel p2; //Items displayed in the row

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
				mySetBackground(cBlue);
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
		setLayout(new BorderLayout());
		name.setFont(name.getFont().deriveFont(14.0f));
		yearP.setFont(name.getFont().deriveFont(14.0f));

		icon = new JLabel();
		p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(icon);
		p1.add(name);
		p1.add(yearP);

		p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15,0));
		p2.add(typeG);
		p2.add(numPlayer);
		p2.setBorder(BorderFactory.createEmptyBorder(16,0,0,0)); //margin top

		stars.setBorder(BorderFactory.createEmptyBorder(-8,0,0,18)); //margin R

		JPanel p2wrap = new JPanel(new BorderLayout(0,0));
		p2wrap.add(p2, BorderLayout.NORTH);
		p2wrap.add(stars, BorderLayout.EAST);

		add(p1, BorderLayout.CENTER);
		add(p2wrap, BorderLayout.EAST);
	}

	/**
	* Refreshes the GUI elements with their new data
	*/
	@Override
	public void refreshGUI() {
		EZFile ez = EZFile.getInstance();
		String p = meta.getDirRoot() + meta.DIR_ICON + "/";
		String p2 = p + Integer.toString(game.getID()) + ".png";
		if (game.getName() == null)
			icon.setIcon(new ImageIcon()); //Blank icon
		else if (ez.pathExists(p2))
			icon.setIcon(new ImageIcon(p2));
		else //No icon
			icon.setIcon(browser.makeIcon("/img/no_icon.png"));

		numPlayer.setHorizontalAlignment(SwingConstants.RIGHT);
		stars.setHorizontalAlignment(SwingConstants.CENTER);

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
		mySetBackground(row % 2 == 0 ? cWhite : cGray);
	}

	/**
	* Sets the background for all the sub-components in my layout
	*/
	public void mySetBackground(Color c) {
		for (int i = 0; i < getComponentCount(); i++)
			getComponent(i).setBackground(c);
		p2.setBackground(c);
	}
}

