import java.io.*;

/**
* Stores Meta Data from community & users
*/
public class Meta {
	/********* Constants *********/
	public static final String DIR_CONF   = "/conf";
	public static final String DIR_ICON   = "/img/icon";
	public static final String DIR_LOG    = "/log";
	public static final String DIR_SCREEN = "/img/screen";
	public static final String DIR_USER   = "/user";

//TODO: get DIRs from user entered data, but assume these (even DIR)
//TODO: allow for multiple directories to be entered

	/********* Variables *********/
	private String dirRoot = "/usr/local/game_browser";
	private String dirBash = "/usr/share/games/bash";
	private String dirX11  = "/usr/share/games/x11";
	private String dirDos  = "/usr/share/games/dos";
	private String dirNes  = "/usr/share/games/rom/nes";
	private String dirSnes = "/usr/share/games/rom/snes";
	private String dirN64  = "/usr/share/games/rom/n64";
	private String dirGC   = "/usr/share/games/rom/gc";
	private String dirWii  = "/usr/share/games/rom/wii";
	private String dirWU   = "/usr/share/games/rom/wu";
	private String dirNX   = "/usr/share/games/rom/nx";
	private String dirGB   = "/usr/share/games/rom/gb";
	private String dirGBA  = "/usr/share/games/rom/gba";
	private String dirPSX  = "/usr/share/games/rom/psx";

	/********* Getters *********/
	public int    getType()        { return type; }
	public String getTypeName()    { return TYPES[type]; }

	/********* Setters *********/
	public void setType(int i)           { type = i; }
	public void getConf(String s)        { conf = s; }


	/********* Methods *********/

//TODO: Singleton???

	/**
	* Creates a Meta data object to house user & community data
	*/
	public Meta() throws IOException {
//TODO: look for community info, but deal with it if not synced
return;



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


}

