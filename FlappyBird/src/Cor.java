import java.util.HashMap;

/**
 * Corlor in RGB
 */
public class Cor {
	public int r;
	public int g;
	public int b;

	private static HashMap<Integer, Cor> cores = new HashMap<Integer, Cor>();

	/*
	 * Cria uma cor dados os componentes entre 0 e 255
	 */
	private Cor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/*
	 * Cria uma cor dados os componentes entre 0 e 1
	 */
	public static Cor rgb(double r, double g, double b) {
		return Cor.rgb((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	public static Cor rgb(int r, int g, int b) {
		int indice = (r << 16) | (g << 8) | b;
		if (!cores.containsKey(indice)) {
			cores.put(indice, new Cor(r, g, b));
		}
		return cores.get(indice);
	}

	public static Cor White = Cor.rgb(1.0, 1.0, 1.0);
	public static Cor Blue = Cor.rgb(0.0, 0.0, 1.0);
	public static Cor Red = Cor.rgb(1.0, 0.0, 0.0);
	public static Cor Green = Cor.rgb(0.0, 1.0, 0.0);
}
