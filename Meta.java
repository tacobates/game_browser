import java.io.*;
import java.util.*;

/**
* Stores Meta Data from community & users
*/
public final class Meta {
	private static Meta instance = null; //Singleton

	/********* Constants *********/
	public static final int CARD_GAP_H     = 2;
	public static final int CARD_GAP_V     = 0;
	public static final int DEF_H          = 600;
	public static final int DEF_W          = 700;
	public static final int GIF_H          = 200;
	public static final int GIF_W          = 480;
	public static final int I_SUPPORT      = 0;
	public static final int I_ID           = 1;
	public static final int I_TYPE         = 2;
	public static final int I_RATE         = 3;
	public static final int I_NUMP         = 4;
	public static final int I_YEAR         = 5;
	public static final int I_GENRE        = 6;
	public static final int I_NAME         = 7;
	public static final int ICON_H         = 64;
	public static final int ICON_PAD       = 10;
	public static final int ICON_W         = 64;
	public static final int PAD_H          = 35; //size of header
	public static final int PAD_W          = 6;  //size of border
	public static final int PAD_W2         = 25; //size of scroll bar
	public static final int PAGE_SIZE      = 12;
	public static final String DIR_CONF    = "/detail";
	public static final String DIR_ICON    = "/icon";
	public static final String DIR_LOG     = "/log";
	public static final String DIR_SCREEN  = "/screen";
	public static final String DIR_USER    = "/user";
	public static final String FILE_ALL    = "/all.tsv";
	public static final String HTML_A      = "<html><body>";
	public static final String HTML_Z      = "</body></html>";
	public static final String LOG_ACC     = "/log/access.log";
	public static final String LOG_ERR     = "/log/error.log";
	public static final String LOG_INST    = "/log/install.log";
	public static final String PAGE        = "Page ";
	public static final String SEP         = "/";
	public static final String TITLE       = "Game Browser";
	public static final String TITLE2      = "Game Details";

//TODO: get DIRs from user entered data, but assume these (even DIR)
//TODO: allow for multiple directories to be entered

	/********* Variables *********/
	private static ArrayList<Integer> gids;    //Filtered list of Game IDs
	private static ArrayList<Integer> gidsAll; //Has ALL Game IDs

	private static HashMap<Integer, Game> mapGames; //Stores all our Games
	private static TreeMap<String, Integer> sortedName;
	private static TreeMap<String, Integer> sortedRate; //Rate Desc + Name +ID
	private static TreeMap<String, Integer> sortedYear; //Year + Name + ID

	private static String dirRoot = "/usr/local/game_meta";
	private static String dirBash = "/usr/share/games/bash";
	private static String dirX11  = "/usr/share/games/x11";
	private static String dirDos  = "/usr/share/games/dos";
	private static String dirNes  = "/usr/share/games/rom/nes";
	private static String dirSnes = "/usr/share/games/rom/snes";
	private static String dirN64  = "/usr/share/games/rom/n64";
	private static String dirGC   = "/usr/share/games/rom/gc";
	private static String dirWii  = "/usr/share/games/rom/wii";
	private static String dirWU   = "/usr/share/games/rom/wu";
	private static String dirNX   = "/usr/share/games/rom/nx";
	private static String dirGB   = "/usr/share/games/rom/gb";
	private static String dirGBA  = "/usr/share/games/rom/gba";
	private static String dirPSX  = "/usr/share/games/rom/psx";
	private static String user    = "no_user";

	/********* Getters *********/ //TODO: !!!
	public String getDirRoot()   { return dirRoot; }
	public String getDirConf()   { return dirRoot + DIR_CONF; }
	public String getDirIcon()   { return dirRoot + DIR_ICON; }
	public String getDirLog()    { return dirRoot + DIR_LOG; }
	public String getDirScreen() { return dirRoot + DIR_SCREEN; }
	public String getDirUser()   { return dirRoot + DIR_USER; }

	/********* Setters *********/ //TODO: !!!
	public void setDirRoot(String s)   { dirRoot = s; }


	/********* Methods *********/

	/**
	* Singleton empty constructor
	*/
	protected Meta() {
		//Do nothing
	}

	/**
	* Gets the Singleton instance
	*/
	public static Meta getInstance() {
		if (instance == null) {
			instance = new Meta();
			parseConfig();
		}
		return instance;
	}

	/**
	* Reads the config file to populate our member variables
	*/
	private static void parseConfig() {
		//TODO: set dirRoot & dir* if config was saved

		//Get current user
		user = System.getProperty("user.name");

		//Get Basic Game Data (everything sortable & filterable)
		int i = 1;
		int skip = 1; //Rows to skip (like a header row)
		gids = new ArrayList();
		gidsAll = new ArrayList();
		mapGames = new HashMap<Integer, Game>();
		sortedName = new TreeMap<String, Integer>();
		sortedRate = new TreeMap<String, Integer>();
		sortedYear = new TreeMap<String, Integer>();
		String path = dirRoot + FILE_ALL;
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line = br.readLine();
			while (line != null) {
				if (line.length() > 1 && i++ > skip) { //Skip lines with no data
					Game g = createGame(line.split("\\t"));
					int id = g.getID();
					String n = g.getName().toLowerCase();
					String y = Integer.toString(g.getYear());
					String r = String.format("%.2f", g.getRating());
					mapGames.put(id, g);
					sortedName.put(n, id);
					sortedRate.put(r+n, id);
					sortedYear.put(y+n, id);
					gidsAll.add(id);
				}
				line = br.readLine();
			}

//TODO: move to function for resorting
//TODO: sort by whatever was sorted last time
			//Default gids to Name Sorted list
			for(Map.Entry<String,Integer> entry : sortedName.entrySet())
				gids.add(entry.getValue());
		} catch (Exception e) {
		}

		//TODO: use "all" to create sorted sets, but do it after initial load
	}

	/**
	* Creates a game from a TSV row that's been split into an array
	* [0] - (String) Installed 1 or ""
	* [1] - (int) ID of game
	* [2] - (String) Type of game (Bash, X11, Nes, DOS, etc...)
	* [3] - (double) Rating
	* [4] - (int) max # players
	* [5] - (int) Year released (may be blank)
	* [6] - (String) Genre of game (first 4 chars)
	* [7] - (String) Name of Game 
	*/
	public static Game createGame(String[] x) {
		Game g = null;
		String type = x[I_TYPE].toLowerCase();
		switch (type) {
			case "bash": case "x11":
				g = new GameX11(); break;
			case "dos":  g = new GameDos();  break;
			case "ds3":  g = new GameDS3();  break;
			case "ds":   g = new GameDS();   break;
			case "gba":  g = new GameGba();  break;
			case "gb":   g = new GameGb();   break;
			case "gc":   g = new GameGC();   break;
			case "n64":  g = new GameN64();  break;
			case "nes":  g = new GameNes();  break;
			case "nx":   g = new GameNX();   break;
			case "psx":  g = new GamePsx();  break;
			case "snes": g = new GameSnes(); break;
			case "wii":  g = new GameWii();  break;
			case "wu":   g = new GameWU();   break;
			default:     g = new Game();     break;
		}
		g.setName(x[I_NAME]);
		g.setSupport(0 == "1".compareTo(x[I_SUPPORT]));
		g.setID(Integer.parseInt(x[I_ID]));
		g.setNumPlayers(Integer.parseInt(x[I_NUMP]));
		g.setRating(Double.parseDouble(x[I_RATE]));
		g.setYear(Integer.parseInt(x[I_YEAR]));
		g.setGenre(x[I_GENRE]);
		return g;
	}

//TODO: filter/sort method that returns # games that apply

	/**
	* Get Game IDs (TODO: with applied filters / sorting)
	* @param int page: Index of the page you want (0 based)
	*/
	public ArrayList<Game> getGames(int page) {
		//TODO: apply filters/sort (get from pre-sorted)
		int max = gids.size();
		ArrayList<Game> rtn = new ArrayList();
		int start = page * PAGE_SIZE;
		for (int i = start; i < start + PAGE_SIZE; ++i) {
			if (i < gids.size())
				rtn.add(mapGames.get(gids.get(i)));
			else
				rtn.add(new Game());
		}
		return rtn;
	}

	/**
	* Non-static toString override taps into static output()
	*/
	public String toString() {
		return getInstance().toStringStatic();
	}

	/**
	* Static output method
	*/
	public static String toStringStatic() {
		return "User: " + user + "\n" +
			"";
	}
}

