//JavaFX example here: www.youtube.com/watch?v=FLkOX4Eez6o
//doesn't work on ARM

/**
* Initializes and displays a Browser object
*/
public class Main {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				Browser gb = new Browser();
				gb.setVisible(true);
			}
		});
	}
}
