import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private int currPage = 0;

	private Dimension dSize;  //Size of Window as a whole
	private Dimension dSize2; //Size of Body (minus header, borders)

	private JLabel iAbout;
	private JLabel iHome;
	private JLabel iSearch;
	private JLabel iSync;
	private JLabel iNext;
	private JLabel iPrev;
	private JLabel lLoad;

	private JPanel cards;
	private JPanel pAbout;
	private JPanel pBtns1;
	private JPanel pBtns2;
	private JPanel pBrowse;
	private JPanel pControl;
	private JPanel pLoad;
	private JPanel pSearch;
	private JPanel pSync;

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

		initBrowser();  //Browser Pane
		initControls(); //Control Pane
		initSearch();   //Search Pane
		initSync();     //Sync Pane
		initAbout();    //About Pane

		JPanel wrapBrowse = new JPanel(new BorderLayout());
		wrapBrowse.add(pControl, BorderLayout.NORTH);
		wrapBrowse.add(pBrowse, BorderLayout.CENTER);

		//Details Pane
		pDetail = new RenderDetail(this);

//TODO: display pControls ABOVE browseScroll
//TODO: add padding to shrink browseScroll
		//Scroll Panes for each Card
		browseScroll = new JScrollPane(wrapBrowse);
		detailScroll = new JScrollPane(pDetail);
		detailScroll.setBorder(BorderFactory.createEmptyBorder());

		//Add the Cards & place in Content Pane
		cards.add("1", browseScroll);
		cards.add("2", detailScroll);
		cards.add("3", pSearch);
		cards.add("4", pSync);
		cards.add("5", pAbout);

		card(1);
		revalidate(); //Try to force Gif to display

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
	* Go to specified page number (if possible)
	* @param int n: the page number to go to, if in bounds
	*/
	private void page(int n) {
int max = 3; //TODO: get real max
		if (n < 0)
			n = max;
		else if (n > max)
			n = 0;
		
		currPage = n;
		loadBrowser();
System.out.println("Go to page: " + Integer.toString(currPage));
		//TODO: alter textbox to have value of currPage
	}
	private void pageUp()   { page(currPage + 1); }
	private void pageDown() { page(currPage - 1); }
//TODO: make key listeners that are actually bound to pageUp & pageDown???

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
	* Sets up the Control panel for Searching & Filtering, Paging, and About
	*/
	private void initControls() {
		pControl = new JPanel(new BorderLayout());
		pBtns1 = new JPanel(new GridLayout(1,4,7,0));
		pBtns2 = new JPanel(new GridLayout(1,3,3,0));

		iAbout = makeIcon("/img/info.gif");
		iHome = makeIcon("/img/home.gif");
		iNext = makeIcon("/img/forward.gif");
		iPrev = makeIcon("/img/back.gif");
		iSearch = makeIcon("/img/search.gif");
		iSync = makeIcon("/img/refresh.gif");

		pBtns1.add(iHome);
		pBtns1.add(iSearch);
		pBtns1.add(iSync);
		pBtns1.add(iAbout);

		pBtns2.add(iPrev);
		//pBtns2.add(); //TODO: add text box for typing
		pBtns2.add(iNext);

		pControl.add(pBtns1, BorderLayout.WEST);
		pControl.add(pBtns2, BorderLayout.EAST);

		//Action Listeners for all items in pControl
		iHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO: clear search/filter criteria
				card(1);
			}
		});
		iSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { card(3); }
		});
		iSync.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
System.out.println("TODO: JDialog to confirm, then card(4) to watch progress");
				card(4);
			}
		});
		iAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { card(5); }
		});
		iPrev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { pageDown(); }
		});
		iNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { pageUp(); }
		});
	}

	/**
	* Sets up the Search panel
	*/
	private void initSearch() {
		JLabel home = makeIcon("/img/home.gif");
		pSearch = new JPanel();

JLabel temp = new JLabel("TODO: actually make a Search Pane");

		pSearch.add(home);
		pSearch.add(temp);

		home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { card(1); }
		});
	}

	/**
	* Sets up the Sync panel
	*/
	private void initSync() {
		JLabel home = makeIcon("/img/home.gif");
		pSync = new JPanel();

JLabel temp = new JLabel("TODO: actually make a Sync Pane");

		pSync.add(home);
		pSync.add(temp);

		home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { card(1); }
		});
	}

	/**
	* Sets up the About panel
	*/
	private void initAbout() {
		JLabel home = makeIcon("/img/home.gif");
		pAbout = new JPanel();

JLabel temp = new JLabel("TODO: actually make an About Pane");

		pAbout.add(home);
		pAbout.add(temp);

		home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { card(1); }
		});
	}

	/**
	* Scrapes directory to get Game data
	*/
	private void loadBrowser() {
//TODO: apply filters/sorts to meta
		ArrayList<Game> games = meta.getGames(currPage);
		for (int i = 0; i < games.size(); ++i)
			rows[i].setGame(games.get(i));
//TODO: repaint???
	}

	/**
	* Makes a JLabel for the spcified icon
	* @param src: local path to icon
	*/
	private JLabel makeIcon(String src) {
		URL u = this.getClass().getResource(src);
		ImageIcon i = new ImageIcon(u);
		JLabel rtn = new JLabel(i);
		rtn.setPreferredSize(new Dimension(24, 24));
		return rtn;
	}
}

