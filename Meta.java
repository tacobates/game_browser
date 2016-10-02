import java.io.*;

/**
* Stores Meta Data from community & users
*/
public final class Meta {
	private static Meta instance = null; //Singleton

	/********* Constants *********/
	public static final String DIR_CONF   = "/conf";
	public static final String DIR_ICON   = "/img/icon";
	public static final String DIR_LOG    = "/log";
	public static final String DIR_SCREEN = "/img/screen";
	public static final String DIR_USER   = "/user";

//TODO: get DIRs from user entered data, but assume these (even DIR)
//TODO: allow for multiple directories to be entered

	/********* Variables *********/
	private static String dirRoot = "/usr/local/game_browser";
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
	public int    getType()        { return 0; }
	public String getTypeName()    { return "Zero"; }

	/********* Setters *********/ //TODO: !!!
	public void setType(int i)           { int type = i; }
	public void getConf(String s)        { String conf = s; }


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
System.out.println(toStringStatic());
		return instance;
	}

	/**
	* Reads the config file to populate our member variables
	*/
	private static void parseConfig() {
		user = System.getProperty("user.name");
return;

//TODO: look for community info, but deal with it if not synced

//TODO: catch IOException

/*
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
*/

		//Parse Config & Populate Class Variables
		//TODO
//TODO: Throw Error here if file is invalid (output it elegantly in Browser)
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

