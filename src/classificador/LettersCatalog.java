package classificador;

import java.util.ArrayList;

public class LettersCatalog {

	private ArrayList<CoordsSpeller> letters = new ArrayList<CoordsSpeller>();
	char[] alphabet = "abcdefghijklmnopqrstuvwxyz123456789_".toCharArray();
	char[] numbers = "".toCharArray();

	public LettersCatalog() {
		createArray();
	}

	public void createArray() {
		int b = 0;
		for (int i = 7; i < 13; i++) {
			for (int a = 1; a < 7; a++) {
				letters.add(new CoordsSpeller(i, a, String.valueOf(alphabet[b])));
				b++;
			}
		}
	}
	
	public String getLetter(int x, int y) {
		for (CoordsSpeller c : letters) {
			if (c.getX() == x && c.getY() == y) {
				return c.getLetter();
			}
		}
		return null;
	}
}
