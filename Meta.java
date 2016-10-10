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
	public static final int I_FILE         = 8;
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
	public static final String FILT_ANY    = "Any";
	public static final String FILT_FAVE1  = "Only Favorited";
	public static final String FILT_INST1  = "Only Installed";
	public static final String FILT_RATE2  = "> 2.0";
	public static final String FILT_RATE25 = "> 2.5";
	public static final String FILT_RATE3  = "> 3.0";
	public static final String FILT_RATE35 = "> 3.5";
	public static final String FILT_RATE4  = "> 4.0";
	public static final String FILT_RATE45 = "> 4.5";
	public static final String HTML_A      = "<html><body>";
	public static final String HTML_Z      = "</body></html>";
	public static final String LABF_CONT   = "Contains:";
	public static final String LABF_FAVE   = "Favorited:";
	public static final String LABF_GENR   = "Genre:";
	public static final String LABF_INST   = "Installed:";
	public static final String LABF_NUMP   = "Players:";
	public static final String LABF_RATE   = "Rating:";
	public static final String LABF_SORT   = "Sort by:";
	public static final String LABF_TYPE   = "Type:";
	public static final String LABF_YEAR1  = "Newer than:";
	public static final String LABF_YEAR2  = "Older than:";
	public static final String LOG_ACC     = "/log/access.log";
	public static final String LOG_ERR     = "/log/error.log";
	public static final String LOG_INST    = "/log/install.log";
	public static final String PAGE        = "Page ";
	public static final String SEP         = "/";
	public static final String SORT_NAME0  = "Name";
	public static final String SORT_NAME1  = "Name Z-A";
	public static final String SORT_RATE0  = "Popular";
	public static final String SORT_RATE1  = "Unpopular";
	public static final String SORT_YEAR0  = "Oldest";
	public static final String SORT_YEAR1  = "Newest";
	public static final String TITLE1      = "Game Browser";
	public static final String TITLE2      = "Game Details";
	public static final String TITLE3      = "Search & Filter";
	public static final String TITLE4      = "Sync Latest Data";
	public static final String TITLE5      = "About";

//TODO: get DIRs from user entered data, but assume these (even DIR)
//TODO: allow for multiple directories to be entered

	/********* Variables *********/
	private static ArrayList<Integer> gids;    //Filtered list of Game IDs
	private static ArrayList<Integer> gidsAll; //Has ALL Game IDs

	private static HashMap<Integer, Game> mapGames; //Stores all our Games
	protected static TreeMap<String,  String> mapGenres;
	protected static TreeMap<Integer, Boolean> mapPlayers;
	protected static TreeMap<String,  String> mapTypes;
	protected static TreeMap<Integer, Boolean> mapYears;
	private static TreeMap<String,  Integer> sortedName;
	private static TreeMap<String,  Integer> sortedName2;//Desc
	private static TreeMap<String,  Integer> sortedRate; //Rate Asc + Name +ID
	private static TreeMap<String,  Integer> sortedRate2;//Desc
	private static TreeMap<String,  Integer> sortedYear; //Year + Name + ID
	private static TreeMap<String,  Integer> sortedYear2;//Desc

	protected static String dirRoot = "/usr/local/game_meta";
	protected static String dirBash = "/usr/games/";
	protected static String dirX11  = "/usr/games/";
	protected static String dirDos  = "/usr/games/dos";
	protected static String dirNes  = "/usr/games/rom/nes";
	protected static String dirSnes = "/usr/games/rom/snes";
	protected static String dirN64  = "/usr/games/rom/n64";
	protected static String dirGC   = "/usr/games/rom/gc";
	protected static String dirWii  = "/usr/games/rom/wii";
	protected static String dirWU   = "/usr/games/rom/wu";
	protected static String dirNX   = "/usr/games/rom/nx";
	protected static String dirGB   = "/usr/games/rom/gb";
	protected static String dirGBA  = "/usr/games/rom/gba";
	protected static String dirDS   = "/usr/games/rom/ds";
	protected static String dirDS3  = "/usr/games/rom/ds3";
	protected static String dirPSX  = "/usr/games/rom/psx";
	protected static String user    = "no_user";

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
		sortedName2 = new TreeMap<String, Integer>(Collections.reverseOrder());
		sortedRate = new TreeMap<String, Integer>();
		sortedRate2 = new TreeMap<String, Integer>(Collections.reverseOrder());
		sortedYear = new TreeMap<String, Integer>();
		sortedYear2 = new TreeMap<String, Integer>(Collections.reverseOrder());
		mapGenres = new TreeMap<String, String>();
		mapPlayers = new TreeMap<Integer, Boolean>();
		mapTypes = new TreeMap<String, String>();
		mapYears = new TreeMap<Integer, Boolean>();
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
					sortedName2.put(n, id);
					sortedRate.put(r+n, id);
					sortedRate2.put(r+n, id);
					sortedYear.put(y+n, id);
					sortedYear2.put(y+n, id);
					gidsAll.add(id);
					mapGenres.put(g.getGenre().toLowerCase(), g.getGenre());
					mapPlayers.put(g.getNumPlayers(), true);
					mapTypes.put(g.getTypeName().toLowerCase(), g.getTypeName());
					mapYears.put(g.getYear(), true);
				}
				line = br.readLine();
			}

//TODO: do below by calling filter() with right params
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
	* [8] - (String) Filename for game 
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
		if (x.length > I_FILE)
			g.setFile(x[I_FILE]);
		return g;
	}

//TODO: filter/sort method that returns # games that apply


	/**
	* Filter & Sort based on the parameter criteria
	* @param sort: corresponds to SORT_* & LABF_SORT
	* @param search: corresponds to LABF_CONT
	* @param fave: corresponds to FILT_FAVE1 & LABF_FAVE
	* @param inst: corresponds to FILT_INST1 & LABF_INST
	* @param rate: corresponds to FILT_RATE* & LABF_RATE
	* @param nump: corresponds to LABF_NUMP
	* @param type: corresponds to LABF_TYPE
	* @param genre: corresponds to LABF_GENRE
	* @param y1: corresponds to LABF_YEAR1
	* @param y2: corresponds to LABF_YEAR2
	*/
	public void filter(String sort, String search, String fave, String inst,
		String rate, String nump, String type, String genre, String y1,
		String y2) {

		Set<Map.Entry<String,Integer>> entries;
		switch (sort) {
			case SORT_NAME0: entries = sortedName.entrySet(); break;
			case SORT_NAME1: entries = sortedName2.entrySet(); break;
			case SORT_RATE0: entries = sortedRate2.entrySet(); break;
			case SORT_RATE1: entries = sortedRate.entrySet(); break;
			case SORT_YEAR0: entries = sortedYear.entrySet(); break;
			case SORT_YEAR1: entries = sortedYear2.entrySet(); break;
			default: return; //We cannot handle unexpected sorting
		}

		//Wipe out the currently filtered list
		gids = new ArrayList<Integer>();

		//Use that sorted list to loop through & check criteria
		for(Map.Entry<String,Integer> entry : entries) {
			int id = entry.getValue();
			Game g = mapGames.get(id);

			//TODO: fave & inst (not in data yet)

			//Check rating
			double minRate = 0.0;
			switch (rate) {
				case FILT_RATE45: minRate = 4.5; break;
				case FILT_RATE4:  minRate = 4.0; break;
				case FILT_RATE35: minRate = 3.5; break;
				case FILT_RATE3:  minRate = 3.0; break;
				case FILT_RATE25: minRate = 2.5; break;
				default: break;
			}
			if (g.getRating() < minRate)
				continue; //Failed Match - Skip it

			//Check Num Players
			nump = nump.replaceAll("\\D", "");
			if (nump.length() > 0) {
				if (g.getNumPlayers() < Integer.parseInt(nump))
					continue; //Failed Match - Skip it
			}

			//Check Type
			if (FILT_ANY != type) {
				if (type.compareToIgnoreCase(g.getTypeName()) != 0)
					continue; //Failed Match - Skip it
			}

			//Check Genre
			if (FILT_ANY != genre) {
				if (genre.compareToIgnoreCase(g.getGenre()) != 0)
					continue; //Failed Match - Skip it
			}

			//Check Year Start
			if (FILT_ANY != y1) {
				int y = Integer.parseInt(y1);
				if (g.getYear() < y)
					continue; //Failed Match - Skip it
			}

			//Check Year End
			if (FILT_ANY != y2) {
				int y = Integer.parseInt(y2);
				if (g.getYear() > y)
					continue; //Failed Match - Skip it
			}

			//Check Search String (slowest, but may be skipped by other criteria)
			if (search.length() > 0) {
				if (-1 == g.getName().toLowerCase().indexOf(search.toLowerCase()))
					continue; //Failed Match - Skip it
			}

			//If we haven't hit a "continue" yet, then we've matched all criteria
			gids.add(id);
		}
	}

	/**
	* Based on PAGE_SIZE & filtered gids, how many pages are there
	*/
	public int getMaxPageNum() {
		return (gids.size() / PAGE_SIZE); //Integer division ~ floor()
	}

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

