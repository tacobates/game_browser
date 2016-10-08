import java.awt.Dimension;
import java.awt.Graphics;

import java.io.*;
import java.net.URL;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;

/**
* A JPanel tweaked for GIF display (since regular JPanels lazy-load too slow)
*/
public class PanelGif extends JPanel {
	/********* Variables *********/
	private Dimension size;
	private ImageIcon gif;
	private JLabel label;

	/********* Methods *********/

	/**
	* Constructor initializes GUI
	* @param String src: Local path to image
	* @param int w: desired width in pixels
	* @param int h: desired height in pixels
	*/
	public PanelGif(String src, int w, int h) {
		//Pre-load GIF because it lazy loads too slowly
		URL ugif = this.getClass().getResource(src);
		gif = new ImageIcon(ugif);
		label = new JLabel(gif);
		size = new Dimension(w, h);
		label.setSize(size);
		label.setMinimumSize(size);
		label.setMaximumSize(size);
		label.setPreferredSize(size);

//		setLayout(new FlowLayout()); //only include wrapper panel cards
		setPreferredSize(size);
		setSize(size);
	}

	/**
	* Force immidiate render
	*/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		gif.paintIcon(this, g, 0, 0);
	}
}

