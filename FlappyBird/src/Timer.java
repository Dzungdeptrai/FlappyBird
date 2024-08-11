public class Timer {
	double tempo;
	double limite;
	Bam b;
	boolean repete;
	boolean fim;

	public Timer(double limite, boolean repete, Bam Bam) {
		this.limite = limite;
		this.b = Bam;
		this.repete = repete;
	}

	public void tique(double dt) {
		if (fim)
			return;
		tempo += dt;
		if (tempo > limite) {
			b.executa();
			if (repete) {
				tempo -= limite;
			} else {
				fim = true;
			}
		}
	}
}