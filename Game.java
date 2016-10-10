import java.io.*;

/**
* Class that represents a Game
*/
public class Game {
	/********* Constants *********/
	public static final int TYPE_BASH =   0;
	public static final int TYPE_X11  =   1;
	public static final int TYPE_DOS  =   2;
	public static final int TYPE_NES  =   3;
	public static final int TYPE_SNES =   4;
	public static final int TYPE_N64  =   5;
	public static final int TYPE_GC   =   6; //Game Cube
	public static final int TYPE_WII  =   7;
	public static final int TYPE_WU   =   8; //Wii U
	public static final int TYPE_NX   =   9; //Nintendo NX
	public static final int TYPE_GB   =  10; //Gameboy
	public static final int TYPE_GBA  =  11; //Gameboy Advanced
	public static final int TYPE_DS   =  12; //Nintendo DS
	public static final int TYPE_DS3  =  13; //Nintendo 3-DS
	public static final int TYPE_PSX  =  14;
	public static final String[] TYPES = {
		"Terminal",
		"X11",
		"DOS",
		"NES",
		"SNES",
		"N64",
		"GC",
		"Wii",
		"WiiU",
		"NX",
		"GB",
		"GBA",
		"PSX"
	};

	/********* Variables *********/
	private boolean support = false;
	private double rating   = 0;
	private double userRate = 0;
	private int id = 0;
	private int type;
	private int numPlayers;
	private int numScreens;
	private int year;
	protected Meta meta;
	private String conf; //Configuration file
	private String cmd; //Special command required to launch?
	private String description = "No Description";
	protected String dir;
	private String file;
	private String genre;
//TODO: make Icon an actual GUI element? or do in decorator?
	private String icon;
	private String name;
	private String screenshot;
//TODO: allow for multiple screenshots

	/********* Getters *********/
	public boolean getSupport()     { return support; }
	public double  getRating()      { return rating; }
	public double  getUserRate()    { return userRate; }
	public int     getID()          { return id; }
	public int     getNumPlayers()  { return numPlayers; }
	public int     getNumScreens()  { return numScreens; }
	public int     getType()        { return type; }
	public int     getYear()        { return year; }
	public String  getTypeName()    { return TYPES[type]; }
	public String  getConf()        { return conf; }
	public String  getCmd()         { return cmd; }
	public String  getDescription() { return description; }
	public String  getDir()         { return dir; }
	public String  getFile()        { return file; }
	public String  getGenre()       { return genre; }
	public String  getIcon()        { return icon; }
	public String  getName()        { return name; }
	public String  getScreenshot()  { return screenshot; }

	/********* Setters *********/
	public void setSupport(boolean b)    { support = b; }
	public void setRating(double d)      { rating = d; }
	public void setUserRate(double d)    { userRate = d; }
	public void setID(int i)             { id = i; }
	public void setNumPlayers(int i)     { numPlayers = i; }
	public void setNumScreens(int i)     { numScreens = i; }
	public void setType(int i)           { type = i; }
	public void setYear(int i)           { year = i; }
	public void setConf(String s)        { conf = s; }
	public void setCmd(String s)         { cmd = s; }
	public void setDescription(String s) { description = s; }
	public void setDir(String s)         { dir = s; }
	public void setFile(String s)        { file = s; }
	public void setIcon(String s)        { icon = s; }
	public void setName(String s)        { name = s; }
	public void setScreenshot(String s)  { screenshot = s; }
	public void setGenre(String s)       {
		//TODO: do as a hashMap
		s = s.toLowerCase();
		switch (s) {
			case "adve": genre = "Adventure";     break;
			case "arca": genre = "Arcade";        break;
			case "batt": genre = "Battle";        break;
			case "boar": genre = "Board Game";    break;
			case "card": genre = "Card Game";     break;
			case "educ": genre = "Education";     break;
			case "esca": genre = "Escape";        break;
			case "fps":  genre = "FPS";           break;
			case "plat": genre = "Platformer";    break;
			case "puzz": genre = "Puzzle";        break;
			case "race": genre = "Racing";        break;
			case "rpg":  genre = "RPG";           break;
			case "shoo": genre = "Shooter";       break;
			case "simu": genre = "Simulation";    break;
			case "stea": genre = "Stealth";       break;
			case "text": genre = "Text Based";    break;
			case "word": genre = "Word Puzzle";   break;
			default:     genre = "Unspecified";   break;
		}
	}


	/********* Methods *********/

	/**
	* Creates an empty game for us to populate
	*/
	public Game() {
		meta = Meta.getInstance();
		setDir(meta.dirX11);
	}

	/**
	* Outputs the meta data for a Game
	*/
	public String toString() {
		return
			"Name: " + name + "\n" +
			"type: " + getTypeName() + "(" + type + ")\n" +
			"CMD: " + cmd + "\n" +
			"Conf: " + conf + "\n" +
			"Desc: " + description + "\n" +
			"Dir: " + dir + "\n" +
			"File: " + file + "\n" +
			"Icon: " + icon + "\n" +
			"Screen: " + screenshot;
	}

//TODO: decorator pattern for output???

//TODO: allow for sorting by name, type, file, rating, etc...
//TODO: allow for filtering by type, rating, etc...


	public long launch() {
		System.out.println("This type of game is not yet supported.");
		return -1; //TODO: get and return the PID so we can track run time?
	}
}

