import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	public static final int SCREEN_H   = 600;
	public static final int SCREEN_W   = 800;
	public static final String TITLE   = "Game Browser";
	public static final String TITLE2  = "Game Details";

	/********* Variables *********/
	private JButton bTemp1;
	private JButton bTemp2;
	private JButton bTemp3;

	private JLabel lLoad;

	private JPanel cards;
	private JPanel pBrowse;
	private JPanel pControls; //TODO: set up some controls???
	private JPanel pDetail;
	private JPanel pLoad;

	private JScrollPane browseScroll;
	private JScrollPane detailScroll;

	private List<Game> games;

	private Meta meta;

	/********* Methods *********/

	/**
	* Constructor initializes GUI
	*/
	public Browser() {
		meta = Meta.getInstance();
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

		//Pack contents to display
		pack();
getContentPane().setSize(SCREEN_W, SCREEN_H);

		fetchGameConf();

		//TODO: redraw display??? or redraw on card switch
		//pack();
	}

	/**
	* Initializes our display panels
	* Shows the "loading" card by default
	*/
	private void makePanels() {
		//Card layout to switch between "browse" & "details"
		cards = new JPanel(new CardLayout(CARD_GAP_H, CARD_GAP_V));
		cards.setSize(SCREEN_W, SCREEN_H);

//Temp Buttons
pControls = new JPanel(new GridLayout(1,3, 6,1)); //TODO: make 6/1 gap a CONST
addButtons();

		//Loading Screen
		pLoad = new JPanel(new BorderLayout());
		lLoad = new JLabel("Looking for games...");
		pLoad.add(lLoad, BorderLayout.CENTER);
pLoad.add(pControls, BorderLayout.NORTH);

		//Empty browse & detail panels to be populated later
		pBrowse = new JPanel();
		pDetail = new JPanel();

pBrowse.add(bTemp1);
pDetail.add(bTemp2);
		//Scroll Panes for each Card
		browseScroll = new JScrollPane(pBrowse);
		detailScroll = new JScrollPane(pDetail);

		//Add the Cards & place in Content Pane
		cards.add("0", pLoad);
		cards.add("1", browseScroll);
		cards.add("2", detailScroll);
		getContentPane().add(cards);
	}

/**TODO: make legit*/
private void addButtons() {
bTemp1 = new JButton("Load C");
bTemp2 = new JButton("Browse C");
bTemp3 = new JButton("Detail C");
//TODO: set button actions
pControls.add(bTemp1);
pControls.add(bTemp2);
pControls.add(bTemp3);
bTemp1.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) { card(0); }
});
bTemp2.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) { card(1); }
});
bTemp3.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) { card(2); }
});
}

	/**TODO: make legit*/
	private void card(int i) {
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
	* Scrapes directory to get Game data
	*/
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


}

