package classificador;

public class CoordsSpeller {

	private int x;
	private int y;
	private String letter;
	
	public CoordsSpeller(int x, int y, String letter) {
		this.x = x;
		this.y = y;
		this.letter = letter;
	}
	
	public String getLetter() {
		return letter;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
