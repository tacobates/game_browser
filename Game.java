import java.io.*;

/**
* Class that represents a Game
*/
public class Game {
	/********* Constants *********/
	public static final String DEF_DIR    = "/usr/share/games";
	public static final String DEF_ICON  = "/_icon";
	public static final String DEF_SCREEN = "/_screen";
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
	public static final int TYPE_PSX  =  12;
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
	private int type;
	private int numPlayers;
	private int year;
	private String conf; //Configuration file
	private String cmd; //Special command required to launch?
	private String description = "No Description";
	private String dir;
	private String file;
	private String icon;
	private String name;
	private String screenshot;
//TODO: allow for multiple screenshots

	/********* Getters *********/
	public int    getNumPlayers()  { return numPlayers; }
	public int    getType()        { return type; }
	public int    getYear()        { return year; }
	public String getTypeName()    { return TYPES[type]; }
	public String getConf()        { return conf; }
	public String getCmd()         { return cmd; }
	public String getDescription() { return description; }
	public String getDir()         { return dir; }
	public String getFile()        { return file; }
	public String getIcon()        { return icon; }
	public String getName()        { return name; }
	public String getScreenshot()  { return screenshot; }

	/********* Setters *********/
	public void setNumPlayers(int i)     { numPlayers = i; }
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


	/********* Methods *********/

	/**
	* Creates a game, and tries to fetch configuration info
	*/
	public Game(String conf) throws Exception {
		//Fetch Config File
		this.conf = conf;
		try(BufferedReader br = new BufferedReader(new FileReader(conf))){
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			conf = sb.toString();
System.out.println(conf);
		}

		//Parse Config & Populate Class Variables
		; //TODO
//TODO: Throw Error here if file is invalid (output it elegantly in Browser)
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


	public int launch() {
		System.out.println("This type of game is not yet supported.");
		return 0; //TODO: get and return the PID so we can track run time?
	}
}

