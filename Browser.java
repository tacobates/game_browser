import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
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
	private Dimension dSize3; //dSize2 - another header (pControl)

	JComboBox<String> cFave;
	JComboBox<String> cGenre;
	JComboBox<String> cInst;
	JComboBox<String> cNumP;
	JComboBox<String> cRate;
	JComboBox<String> cSort;
	JComboBox<String> cType;
	JComboBox<String> cYear1;
	JComboBox<String> cYear2;

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
	private JPanel wrapBrowse;

	private JScrollPane browseScroll;
	private JScrollPane detailScroll;

	private JTextField tContains;
	private JTextField tPage;

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
		setTitle(meta.TITLE1);

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
		dSize3 = new Dimension(w - meta.PAD_W, h - meta.PAD_H - meta.PAD_H);
		cards.setSize(dSize2);
		cards.setPreferredSize(dSize2);
		browseScroll.setSize(dSize3);
		browseScroll.setPreferredSize(dSize3);

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

		//Details Pane
		pDetail = new RenderDetail(this);

		//Scroll Panes for each Card
		browseScroll = new JScrollPane(pBrowse);
		detailScroll = new JScrollPane(pDetail);
		detailScroll.setBorder(BorderFactory.createEmptyBorder());

		//So controls don't scroll off page
		wrapBrowse = new JPanel(new BorderLayout());
		wrapBrowse.add(pControl, BorderLayout.NORTH);
		wrapBrowse.add(browseScroll, BorderLayout.CENTER);

		//Add the Cards & place in Content Pane
		cards.add("1", wrapBrowse);
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
			case 3:
				setTitle(meta.TITLE3);
				break;
			case 4:
				setTitle(meta.TITLE4);
				break;
			case 5:
				setTitle(meta.TITLE5);
				break;
			default:
				setTitle(meta.TITLE1);
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
		int max = meta.getMaxPageNum();
		if (n < 0)
			n = max;
		else if (n > max)
			n = 0;
		
		currPage = n;
		loadBrowser();
		tPage.setText(meta.PAGE + Integer.toString(n + 1));
	}
	private void pageUp()   { page(currPage + 1); }
	private void pageDown() { page(currPage - 1); }
//TODO: make key listeners that are actually bound to pageUp & pageDown???

	/**
	* Filters the list of games based on the criteria in the GUI elements
	*/
	private void filter() {
		String sort = (String)cSort.getSelectedItem();
		String search = tContains.getText();
		String fave = (String)cFave.getSelectedItem();
		String inst = (String)cInst.getSelectedItem();
		String rate = (String)cRate.getSelectedItem();
		String nump = (String)cNumP.getSelectedItem();
		String type = (String)cType.getSelectedItem();
		String genre = (String)cGenre.getSelectedItem();
		String y1 = (String)cYear1.getSelectedItem();
		String y2 = (String)cYear2.getSelectedItem();

		meta.filter(sort, search, fave, inst, rate, nump, type, genre, y1, y2);

		page(0);
		card(1);
	}


//TODO: cluster init* methods
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

		tPage = new JTextField(meta.PAGE + "1", 5); //5 columns wide

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
		pBtns2.add(tPage);
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
		tPage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){ //Happens on enter
				//Only get the numbers
				String s = e.getActionCommand().replaceAll("\\D","");
				if (s.length() > 0)
					page(Integer.parseInt(s) - 1); //Zero based
			}
		});
	}

	/**
	* Sets up the Search panel
	*/
	private void initSearch() {
		pSearch = new JPanel(new BorderLayout());
		JLabel home = makeIcon("/img/home.gif");
		JButton bApply = new JButton("Apply Filters");

		cSort = new JComboBox<String>();
		cSort.addItem(meta.SORT_NAME0);
cSort.addItem(meta.SORT_NAME1); //TODO: Delete. Just for POC (not useful)
		cSort.addItem(meta.SORT_YEAR0);
		cSort.addItem(meta.SORT_YEAR1);
		cSort.addItem(meta.SORT_RATE0);
		cSort.addItem(meta.SORT_RATE1);

		tContains = new JTextField("");

		cFave = new JComboBox<String>();
		cFave.addItem(meta.FILT_ANY);
		cFave.addItem(meta.FILT_FAVE1);

		cInst = new JComboBox<String>();
		cInst.addItem(meta.FILT_ANY);
		cInst.addItem(meta.FILT_INST1);

		cRate = new JComboBox<String>();
		cRate.addItem(meta.FILT_ANY);
		cRate.addItem(meta.FILT_RATE45);
		cRate.addItem(meta.FILT_RATE4);
		cRate.addItem(meta.FILT_RATE35);
		cRate.addItem(meta.FILT_RATE3);
		cRate.addItem(meta.FILT_RATE25);
		cRate.addItem(meta.FILT_RATE2);

		cNumP = new JComboBox<String>();
		cNumP.addItem(meta.FILT_ANY);
		for(Map.Entry<Integer,Boolean> entry : meta.mapPlayers.entrySet())
			cNumP.addItem(Integer.toString(entry.getKey()) + " or more");

		cType = new JComboBox<String>();
		cType.addItem(meta.FILT_ANY);
		for(Map.Entry<String,String> entry : meta.mapTypes.entrySet())
			cType.addItem(entry.getValue());

		cGenre = new JComboBox<String>();
		cGenre.addItem(meta.FILT_ANY);
		for(Map.Entry<String,String> entry : meta.mapGenres.entrySet())
			cGenre.addItem(entry.getValue());

		cYear1 = new JComboBox<String>();
		cYear1.addItem(meta.FILT_ANY);
		for(Map.Entry<Integer,Boolean> entry : meta.mapYears.entrySet())
			cYear1.addItem(Integer.toString(entry.getKey()));

		cYear2 = new JComboBox<String>();
		cYear2.addItem(meta.FILT_ANY);
		for(Map.Entry<Integer,Boolean> entry : meta.mapYears.entrySet())
			cYear2.addItem(Integer.toString(entry.getKey()));

		JPanel top = new JPanel(new BorderLayout());
		top.add(home, BorderLayout.WEST);
		top.add(bApply, BorderLayout.EAST);

//TODO: get a better layout for mid
		JPanel mid = new JPanel(new GridLayout(10,3, 1,1)); //3rd col for help?
		mid.add(new JLabel(meta.LABF_SORT, JLabel.RIGHT));
		mid.add(cSort);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_CONT, JLabel.RIGHT));
		mid.add(tContains);
		mid.add(new JLabel("(leave blank to ignore)"));

		mid.add(new JLabel(meta.LABF_FAVE, JLabel.RIGHT));
		mid.add(cFave);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_INST, JLabel.RIGHT));
		mid.add(cInst);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_RATE, JLabel.RIGHT));
		mid.add(cRate);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_NUMP, JLabel.RIGHT));
		mid.add(cNumP);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_TYPE, JLabel.RIGHT));
		mid.add(cType);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_GENR, JLabel.RIGHT));
		mid.add(cGenre);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_YEAR1, JLabel.RIGHT));
		mid.add(cYear1);
		mid.add(new JLabel(""));

		mid.add(new JLabel(meta.LABF_YEAR2, JLabel.RIGHT));
		mid.add(cYear2);
		mid.add(new JLabel(""));

		pSearch.add(top, BorderLayout.NORTH);
		pSearch.add(mid, BorderLayout.CENTER);

		bApply.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filter();
			}
		});
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
	* @param w: [opt] width of icon in pixels (assume 24x24 for Java Icons)
	* @param h: [opt] height of icon in pixels
	*/
	public JLabel makeIcon(String src) { return makeIcon(src,24,24); }
	public JLabel makeIcon(String src, int w) { return makeIcon(src,w,w); }
	public JLabel makeIcon(String src, int w, int h) {
		URL u = this.getClass().getResource(src);
		ImageIcon i = new ImageIcon(u);
		JLabel rtn = new JLabel(i);
		rtn.setPreferredSize(new Dimension(w, h));
		return rtn;
	}

	/**
	* Makes a JLabel with makeIcon() for our star rating icons
	* @param double r: the rating of the item (probably a Game)
	* @param String c: [opt] the color prefix ("o" for Orange or "b" for Blue)
	* @param int size: [opt] height in Pizels of the desired image (32 or 24)
	*/
	public ImageIcon makeStar(double r){return makeStar(r,"o",24);}
	public ImageIcon makeStar(double r, String c){return makeStar(r,c,24);}
	public ImageIcon makeStar(double r, String c, int size) {
		String px = Integer.toString(size);
		String rounded = String.format("%.1f", r);
		String src = "/img/stars/" + px + "/" + c + rounded + ".png";
		URL u = this.getClass().getResource(src);
		ImageIcon rtn = new ImageIcon(u);
		return rtn;
	}
}

