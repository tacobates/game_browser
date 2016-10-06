import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.io.*;
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

	public static final int CARD_GAP_H = 10;
	public static final int CARD_GAP_V = 10;
	public static final int PAGE_SIZE  = 7; //TODO: set to 20
	public static final int SCREEN_H   = 600;
	public static final int SCREEN_W   = 800;
	public static final String TITLE   = "Game Browser";
	public static final String TITLE2  = "Game Details";

	/********* Variables *********/
	private JButton bTemp1;
	private JButton bTemp2;
	private JButton bTemp3;

	private JLabel lLoad;
	private JLabel lGif;

	private JPanel cards;
	private JPanel pBrowse;
	private JPanel pControls; //TODO: set up some controls???
	private JPanel pDetail;
	private JPanel pLoad;

	private JScrollPane browseScroll;
	private JScrollPane detailScroll;

	private List<Game> games;

	private Meta meta;

	private RenderGame[] rows;

	/********* Methods *********/

	/**
	* Constructor initializes GUI
	*/
	public Browser() {
		meta = Meta.getInstance(); //Holds Game Data & Conf
		initGUI();
	}

	/**
	* Create & show GUI
	*/
	private void initGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout()); //only include wrapper panel cards
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(SCREEN_W, SCREEN_H));
		setSize(SCREEN_W, SCREEN_H);
		setTitle(TITLE);

		makePanels();
		//Nothing after this, since makePanels does an invoke later
	}

	/**
	* Initializes & shows the "loading" card, & triggers panel creation
	*/
	private void makePanels() {
		//Card layout to switch between "browse" & "details"
		cards = new JPanel(new CardLayout(CARD_GAP_H, CARD_GAP_V));
		cards.setSize(SCREEN_W, SCREEN_H);

//Temp Buttons
//pControls = new JPanel(new GridLayout(1,3, 6,1)); //TODO: make 6/1 gap a CONST
makeButtons();

		//Loading Screen
		pLoad = new JPanel(new BorderLayout());
String tempFile = "/home/pi/dev/game_browser/img/loading.gif";
		lGif = new JLabel(new ImageIcon(tempFile));
		lLoad = new JLabel("Looking for games...", JLabel.CENTER);
		lLoad.setFont(lLoad.getFont().deriveFont(36.0f));
		lLoad.setBorder(BorderFactory.createEmptyBorder(35,0,35,0));
		pLoad.add(lLoad, BorderLayout.NORTH);
		pLoad.add(lGif, BorderLayout.CENTER);
		pLoad.pack();

		//Add & Display Loading screen immediately
		cards.add("0", pLoad);
		getContentPane().add(cards);
		pack();
		setVisible(true);

		SwingUtilities.invokeLater(new Runnable(){//do swing work on EDT
			public void run(){
				//  d.dispose();
				finishPanels();
				loadBrowser();
			}
		});
	}

	/**
	* Finishes panel setup after initial loading screen has been displayed
	*/
	public void finishPanels() {
try{Thread.sleep(2000);} catch(InterruptedException e) {}
		//Browser Pane
		initBrowser();
//TODO: Where to put the paging controls ??? (GridLayout => GridBag?)
		pBrowse = new JPanel(new GridLayout(PAGE_SIZE, 1));
		for (int i = 0; i < rows.length; ++i)
			pBrowse.add(rows[i]);

		//Details Pane
		pDetail = new JPanel();
pDetail.add(bTemp2);
pDetail.add(bTemp1);
//TODO: populate details

		//Scroll Panes for each Card
		browseScroll = new JScrollPane(pBrowse);
		detailScroll = new JScrollPane(pDetail);

		//Add the Cards & place in Content Pane
		cards.add("1", browseScroll);
		cards.add("2", detailScroll);

		card(1);
//TODO: dispose of GIF and card0 to free memory
	}

/**TODO: make legit*/
private void makeButtons() {
	bTemp1 = new JButton("Load C");
	bTemp2 = new JButton("Browse C");
	bTemp3 = new JButton("Detail C");
bTemp1.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){card(0);}});
	bTemp2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) { card(1); }
	});
	bTemp3.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) { card(2); }
	});
}

	/**TODO: make legit*/
	protected void card(int i) {
		switch(i) {
			case 2:
				setTitle(TITLE2);
				break;
			default:
				setTitle(TITLE);
				break;
		}
		CardLayout c = (CardLayout)(cards.getLayout());
		c.show(cards, Integer.toString(i));
	}

	/**
	* Sets up the RenderGame elements for use & reuse
	*/
	private void initBrowser() {
		rows = new RenderGame[PAGE_SIZE];
		for (int i = 0; i < PAGE_SIZE; ++i)
			rows[i] = new RenderBrowse(this);
	}

	/**
	* Scrapes directory to get Game data
	*/
	private void loadBrowser() {
//TODO: apply filters/sorts to meta
		int page = 0; //first page (TODO: where to pass in other pages?)
		ArrayList<Game> games = meta.getGames(PAGE_SIZE, page);
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

