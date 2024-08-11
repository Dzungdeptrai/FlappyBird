public interface Game {
	String getTitulo();

	int getChieurong();

	int getChieudai();

	void tique(java.util.Set<String> teclas, double dt);

	void tecla(String tecla);

	void desenhar(Dohoa dohoa);
}