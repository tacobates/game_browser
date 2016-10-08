import java.awt.BorderLayout;
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
	private ImageIcon img = new ImageIcon();

	private JButton back;
	private JButton play;
	private JButton screenNext;
	private JButton screenPrev;

	private JPanel screen;
	private JPanel pTop;
	private JPanel pMiddle;
	private JPanel pBottom;

	private String config;


	/********* Methods *********/

	/**
	* Creates an empty RenderBrowse
	* @param Browser b: reference to the parent display frame
	*/
	public RenderDetail(Browser b) {
		super(b);
		name.setFont(name.getFont().deriveFont(20.0f));
	}

	/**
	* Setup the sub-panes
	*/
	protected void setupPanels() {
		//TODO: setup screen with an image
		screen = new JPanel();
		pTop = new JPanel();
		pMiddle = new JPanel();
//TODO: what layout for pMiddle?
		pBottom = new JPanel(new BorderLayout());
//TODO: if no image, use the default 0.jpg for "no screenshot"
	}

	/**
	* Setup the buttons for the Details pane
	*/
	protected void setupButtons() {
		back = new JButton("Back to Browse");
		play = new JButton("Play Game");
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
		setupPanels();
		setupButtons();

		pTop.add(back);
		pTop.add(name);
		pTop.add(play);

		pMiddle.add(descrip);
		pMiddle.add(year);
		pMiddle.add(type);
		pMiddle.add(genre);
		pMiddle.add(rating);
		pMiddle.add(numP);

		pBottom.add(screen, BorderLayout.NORTH);
		pBottom.add(screenPrev, BorderLayout.SOUTH);
		pBottom.add(screenNext, BorderLayout.SOUTH);

		add(pTop, BorderLayout.NORTH);
		add(pMiddle, BorderLayout.CENTER);
		add(pBottom, BorderLayout.SOUTH);
//TODO: size and place things WAY better
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

		super.refreshGUI(); //at end to trigger repaint when done
	}

	/**
	* Changes the screenshot to +1 or -1
	*/
	public void screen(int increment) {
		//TODO: add & mod the result, refresh img & screen
	}

	public int launch() {
		System.out.println("This type of game is not yet supported.");
		return 0; //TODO: get and return the PID so we can track run time?
//TODO: put in RenderGame and just delegate to Game.launch()
	}
}

