import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
* Renders a game for the detail pane
*/
public class RenderDetail extends RenderGame {
	/********* Constants *********/
	public static final String HTML_A = "<html><body>";
	public static final String HTML_Z = "</body></html>";

	/********* Variables *********/
	private int screenI = 1;

	private Color cBack   = new Color(205,205,205);

	private ImageIcon img;

	private JButton back;
	private JButton play;
	private JButton screenNext;
	private JButton screenPrev;

	private JLabel lGenre;
	private JLabel lNumP;
	private JLabel lRate;
	private JLabel lScreen;
	private JLabel lType;

	private JPanel screen;
	private JPanel pTop;
	private JPanel pBody;
	private JPanel pMiddle;
	private JPanel pMeta;
	private JPanel pBottom;
	private JPanel pBtns;

	private String config;


	/********* Methods *********/

	/**
	* Creates an empty RenderBrowse
	* @param Browser b: reference to the parent display frame
	*/
	public RenderDetail(Browser b) {
		super(b);
		name.setFont(name.getFont().deriveFont(22.0f));
		name.setHorizontalAlignment(SwingConstants.CENTER);
		descrip.setOpaque(true);
		descrip.setBackground(cBack);
		descrip.setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
	}

	/**
	* Setup the JLabels
	*/
	protected void initLabels() {
		lGenre = new JLabel("Genre:",   SwingConstants.RIGHT);
		lNumP  = new JLabel("Players:", SwingConstants.RIGHT);
		lRate  = new JLabel("Rated:",   SwingConstants.RIGHT);
		lType  = new JLabel("Type:",    SwingConstants.RIGHT);
		lScreen = new JLabel();
	}

	/**
	* Setup the sub-panes
	*/
	protected void initPanels() {
		screen = new JPanel();
		pTop = new JPanel(new BorderLayout());
		pBody = new JPanel(new BorderLayout());
		pMiddle = new JPanel(new BorderLayout());
		pMeta = new JPanel(new GridLayout(4,2,10,0));
		pBottom = new JPanel(new BorderLayout());
		pBtns = new JPanel();
//TODO: if no image, use the default 0.jpg for "no screenshot"

		pMiddle.setBorder(BorderFactory.createEmptyBorder(10,0,10,0)); //margins
		pMeta.setBorder(BorderFactory.createEmptyBorder(0,0,0,10)); //margin R
	}

	/**
	* Setup the buttons for the Details pane
	*/
	protected void initButtons() {
		back = new JButton("Back");
		play = new JButton("Play");
		screenNext = new JButton("->");
		screenPrev = new JButton("<-");
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { browser.card(1); }
		});
		play.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//TODO: game.launch() ???
			}
		});
		screenNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { screen(1); }
		});
		screenPrev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { screen(-1); }
		});
	}

	/**
	* Instantiates the GUI elements to display Game data
	*/
	@Override
	public void initLayout() {
		setLayout(new BorderLayout());
		initPanels();
		initLabels();
		initButtons();

		pTop.add(back, BorderLayout.WEST);
		pTop.add(name, BorderLayout.CENTER);
		pTop.add(play, BorderLayout.EAST);

		pMeta.add(lType);
		pMeta.add(type);
		pMeta.add(lGenre);
		pMeta.add(genre);
		pMeta.add(lNumP);
		pMeta.add(numP);
		pMeta.add(lRate);
		pMeta.add(rating);

		pMiddle.add(pMeta, BorderLayout.WEST);
		pMiddle.add(descrip, BorderLayout.CENTER);

		pBtns.add(screenPrev);
		pBtns.add(screenNext);

		screen.add(lScreen);

		pBottom.add(screen, BorderLayout.CENTER);
		pBottom.add(pBtns, BorderLayout.SOUTH);

		pBody.add(pMiddle, BorderLayout.NORTH);
		pBody.add(pBottom, BorderLayout.CENTER);

		add(pTop, BorderLayout.NORTH);
		add(pBody, BorderLayout.CENTER);
	}

	/**
	* Refreshes the GUI elements with their new data
	*/
	@Override
	public void refreshGUI() {
		//TODO: fetch descrip & config from file
String d = "TODO: fetch real description from file<br>Make sure it can handle line breaks & special chars !@#$%^&*()_+=-`~.<br>Especially multiple line breaks!!!";
		//d = d.replaceAll("<br/?>", "\n"); //don't escape, JLabel can do html
		game.setDescription(HTML_A + d + HTML_Z);
		descrip.setText(d);

		screen(0);

		super.refreshGUI(); //at end to trigger repaint when done
	}

	/**
	* Changes the screenshot to +1 or -1
	*/
	public void screen(int increment) {
		int max = game.getNumScreens();
		if (0 == max) {
//TODO: go parse files for num screens
max = 1;
			game.setNumScreens(max);
		}

		screenI += increment;
		if (screenI > max)
			screenI = 1;
		else if (screenI < 1)
			screenI = max;

		//TODO: make URL from meta DIRs
		String p = meta.getDirRoot() + meta.DIR_SCREEN + "/";
		p += Integer.toString(game.getID()) + "/";
		p += Integer.toString(screenI) + ".jpg";
System.out.println("Set Image: " + p);
		img = new ImageIcon(p);
		lScreen.setIcon(img);

		browser.revalidate();
		browser.repaint();
	}

	public int launch() {
		System.out.println("This type of game is not yet supported.");
		return 0; //TODO: get and return the PID so we can track run time?
//TODO: put in RenderGame and just delegate to Game.launch()
	}
}

