import java.io.*;
import java.util.*;

/**
* Stores Meta Data from community & users
*/
public final class Meta {
	private static Meta instance = null; //Singleton

	/********* Constants *********/
	public static final String SEP         = "/";
	public static final String DIR_CONF    = "/detail";
	public static final String DIR_ICON    = "/icon";
	public static final String DIR_LOG     = "/log";
	public static final String DIR_SCREEN  = "/screen";
	public static final String DIR_USER    = "/user";
	public static final String FILE_ALL    = "/all.tsv";
	public static final String LOG_ACC     = "/log/access.log";
	public static final String LOG_ERR     = "/log/error.log";
	public static final String LOG_INST    = "/log/install.log";

//TODO: get DIRs from user entered data, but assume these (even DIR)
//TODO: allow for multiple directories to be entered

	/********* Variables *********/
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
System.out.println(toStringStatic());
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
//TODO: look for community info, but deal with it if not synced
		EZFile ez = EZFile.getInstance();
		ArrayList<String[]> all = ez.readTSV(dirRoot + FILE_ALL, true);
//String[] t = all.get(0);
//System.out.println(t[0] + ", " + t[1] +", "+ t[2] +", "+ t[3] +", "+
//	t[4] +", "+ t[5] +", "+ t[6] +", "+ t[7]);

		//TODO: use "all" to create sorted sets, but do it after initial load
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

