import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;

import java.io.*;
import java.net.URL;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;

/**
* Class for Game Browsing
* As a JFrame, this class acts as the main display window
*/
public class Browser extends JFrame {
	/********* Constants *********/
	public static final String _CONF_DIR = "/usr/share/games/_conf";
//TODO: replace above with reference to Meta.getConfDir()

	/********* Variables *********/
	private Dimension dSize;  //Size of Window as a whole
	private Dimension dSize2; //Size of Body (minus header, borders)

	private JLabel lLoad;

	private JPanel cards;
	private JPanel pBrowse;
	private JPanel pLoad;

	private JScrollPane browseScroll;
	private JScrollPane detailScroll;

	private List<Game> games;

	private Meta meta;

	private PanelGif lGif;

	private RenderDetail pDetail;

	private RenderBrowse[] rows;

	/********* Methods *********/

	/**
	* Constructor initializes GUI
	*/
	public Browser() {
		meta = Meta.getInstance(); //Holds Game Data & Conf

		//Pre-load GIF because it lazy loads too slowly
		lGif = new PanelGif("/img/loading.gif", meta.GIF_W, meta.GIF_H);

		initGUI();
	}

	/**
	* Create & show GUI
	*/
	private void initGUI() {
		dSize = new Dimension(meta.DEF_W, meta.DEF_H);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout()); //only include wrapper panel cards
		setLocationRelativeTo(null);
		setLocation(new Point(0,0));
		setResizable(true);
		setSize(dSize);
		setPreferredSize(dSize);
		setTitle(meta.TITLE);

		initPanels();
		initResizeListener();
		doResize();
	}

	/**
	* Resizes elements based on dSize
	*/
	private void doResize() {
		int h = dSize.height;
		int w = dSize.width;
//System.out.println("Resizing " + w + " x " + h);
		dSize2 = new Dimension(w - meta.PAD_W, h - meta.PAD_H);
		browseScroll.setSize(dSize2);
		browseScroll.setPreferredSize(dSize2);
		cards.setSize(dSize2);
		cards.setPreferredSize(dSize2);

		revalidate(); //Redraw the new sizes immediately
	}

	/**
	* Listens for window resize to resize other elements
	*/
	private void initResizeListener() {
		addComponentListener(new ComponentAdapter() {  
			public void componentResized(ComponentEvent evt) {
				dSize = getSize();
				doResize();
			}
		});
	}

	/**
	* Initializes panels for App
	*/
	private void initPanels() {
		//Card layout to switch between "browse" & "details"
		cards = new JPanel(new CardLayout(meta.CARD_GAP_H, meta.CARD_GAP_V));

		//Loading Screen
		pLoad = new JPanel(new BorderLayout());
		lLoad = new JLabel("Looking for games...", JLabel.CENTER);
		lLoad.setFont(lLoad.getFont().deriveFont(36.0f));
		lLoad.setBorder(BorderFactory.createEmptyBorder(35,0,35,0));
		pLoad.add(lLoad, BorderLayout.NORTH);
		pLoad.add(lGif, BorderLayout.CENTER);

		//Add & Display Loading screen immediately
		cards.add("0", pLoad);
		getContentPane().add(cards);
		pack();
		setVisible(true);

		//Browser Pane
		initBrowser();

		//Details Pane
		pDetail = new RenderDetail(this);

		//Scroll Panes for each Card
		browseScroll = new JScrollPane(pBrowse);
		detailScroll = new JScrollPane(pDetail);

		//Add the Cards & place in Content Pane
		cards.add("1", browseScroll);
		cards.add("2", detailScroll);

		card(1);

		loadBrowser();
//TODO: dispose of GIF and card0 to free memory
	}

	/**
	* Sets the Game for the Detail Pane to display
	* @param Game g: the game the user has asked to see details for
	*/
	protected void setDetailGame(Game g) {
		pDetail.setGame(g);
	}

	/**
	* Switches the card currently displayed in this frame
	* @param int i: the numeric ID for the card
	*/
	protected void card(int i) {
		switch(i) {
			case 2:
				setTitle(meta.TITLE2);
				break;
			default:
				setTitle(meta.TITLE);
				break;
		}
		CardLayout c = (CardLayout)(cards.getLayout());
		c.show(cards, Integer.toString(i));
	}

	/**
	* Sets up the RenderGame elements for use & reuse
	*/
	private void initBrowser() {
		rows = new RenderBrowse[meta.PAGE_SIZE];
		for (int i = 0; i < meta.PAGE_SIZE; ++i)
			rows[i] = new RenderBrowse(this);

		pBrowse = new JPanel(new GridLayout(meta.PAGE_SIZE, 1));
		for (int i = 0; i < rows.length; ++i)
			pBrowse.add(rows[i]);
	}

	/**
	* Scrapes directory to get Game data
	*/
	private void loadBrowser() {
//TODO: apply filters/sorts to meta
		int page = 0; //first page (TODO: where to pass in other pages?)
		ArrayList<Game> games = meta.getGames(page);
		for (int i = 0; i < games.size(); ++i)
			rows[i].setGame(games.get(i));
//TODO: repaint???
	}



	/**
	* Scrapes directory to get Game data
	*/
/*
	private void fetchGameConf() {
		games = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream =
			Files.newDirectoryStream(Paths.get(_CONF_DIR))) {
			for (Path path : directoryStream) {
				String s = path.toString();
				if (s.endsWith(".conf") && !s.startsWith(".")) {
					games.add(new Game(path.toString()));
				}
			}
		} catch (IOException ex) {
			System.out.println("Unable to parse file");
			//TODO: get the file???
		} catch (Exception ex) {
			System.out.println("Invalid conf file");
			//TODO: get the file???
		}

for (int i = 0; i < games.size(); ++i) {
System.out.println(games.get(i));
System.out.println("===================");
}

//TODO: pBrowse.setLayout(new GridLayout(X, 1)); //where X is # games
	}
*/


}

