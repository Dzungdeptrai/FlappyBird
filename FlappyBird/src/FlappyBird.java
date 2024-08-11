import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class FlappyBird implements Game {

	public chim bird;
	public Random gerador = new Random();
	public int record = 0;
	public ScoreNumber scorenumber;

	public int game_state = 0; // [0->Start Screen] [1->Get Ready] [2->Game] [3->Game Over]

	public double scenario_offset = 0;
	public double ground_offset = 0;
	public ArrayList<Cot> Cots = new ArrayList<Cot>();
	public Timer pipetimer;
	public Hitbox groundbox;

	public Timer auxtimer;

	private Bam addCot() {
		return new Bam() {
			public void executa() {
				Cots.add(new Cot(getChieurong(), gerador.nextInt(getChieudai() - 112 - Cot.HOLESIZE)));
			}
		};
	}

	private Bam proxCena() {
		return new Bam() {
			public void executa() {
				game_state += 1;
				game_state = game_state % 4;
			}
		};
	}

	public FlappyBird() {
		bird = new chim(50, getChieudai() / 4);
		pipetimer = new Timer(2, true, addCot());
		scorenumber = new ScoreNumber(0);
		groundbox = new Hitbox(0, getChieudai() - 112, getChieurong(), getChieudai());

	}

	public String getTitulo() {
		return "Flappy Bird";
	}

	public String getAuthor() {
		return "";
	}

	public int getChieurong() {
		return 384;
	}

	public int getChieudai() {
		return 512;
	}

	public void gameOver() {
		Cots = new ArrayList<Cot>();
		bird = new chim(50, getChieudai() / 4);
		proxCena().executa();
	}

	public void tique(Set<String> keys, double dt) {
		scenario_offset += dt * 25;
		scenario_offset = scenario_offset % 288;
		ground_offset += dt * 100;
		ground_offset = ground_offset % 308;

		switch (game_state) {
		case 0: // Main Screen
			break;
		case 1: // Get Ready
			auxtimer.tique(dt);
			bird.updateSprite(dt);
			break;
		case 2: // Game Screen
			pipetimer.tique(dt);
			bird.update(dt);
			bird.updateSprite(dt);
			if (groundbox.intersecao(bird.box) != 0) {
				gameOver();
				return;
			}
			if (bird.y < -5) {
				gameOver();
				return;
			}
			for (Cot Cot : Cots) {
				Cot.tique(dt);
				if (Cot.boxcima.intersecao(bird.box) != 0 || Cot.boxbaixo.intersecao(bird.box) != 0) {
					if (scorenumber.getScore() > ScoreNumber.record) {
						ScoreNumber.record = scorenumber.getScore();
					}
					gameOver();
					return;
				}
				if (!Cot.counted && Cot.x < bird.x) {
					Cot.counted = true;
					scorenumber.modifyScore(1);
					;
				}
			}
			if (Cots.size() > 0 && Cots.get(0).x < -70) {
				Cots.remove(0);
			}

			break;
		case 3: // Game Over Screen
			break;
		}
	}

	public void tecla(String c) {
		switch (game_state) {
		case 0:
			if (c.equals(" ")) {
				auxtimer = new Timer(1.6, false, proxCena());
				proxCena().executa();
			}
			break;
		case 1:
			break;
		case 2:
			if (c.equals(" ")) {
				bird.flap();
			}
			break;
		case 3:
			if (c.equals(" ")) {
				scorenumber.setScore(0);
				proxCena().executa();
			}
			break;
		}
	}

	public void desenhar(Dohoa t) {
		// Draw background no matter what
		t.imagem("flappy.png", 0, 0, 288, 512, 0, (int) -scenario_offset, 0);
		t.imagem("flappy.png", 0, 0, 288, 512, 0, (int) (288 - scenario_offset), 0);
		t.imagem("flappy.png", 0, 0, 288, 512, 0, (int) ((288 * 2) - scenario_offset), 0);

		for (Cot Cot : Cots) {
			Cot.drawItself(t);
		}

		// draw ground
		t.imagem("flappy.png", 292, 0, 308, 112, 0, -ground_offset, getChieudai() - 112);
		t.imagem("flappy.png", 292, 0, 308, 112, 0, 308 - ground_offset, getChieudai() - 112);
		t.imagem("flappy.png", 292, 0, 308, 112, 0, (308 * 2) - ground_offset, getChieudai() - 112);

		switch (game_state) {
		case 0:
			t.imagem("flappy.png", 292, 346, 192, 44, 0, getChieurong() / 2 - 192 / 2, 100);
			t.imagem("flappy.png", 352, 306, 70, 36, 0, getChieurong() / 2 - 70 / 2, 175);
			t.texto("Ấn Space để bắt đầu", 40, getChieudai() / 2 - 16, 32, Cor.Red);
			break;
		case 1:
			bird.drawItself(t);
			t.imagem("flappy.png", 292, 442, 174, 44, 0, getChieurong() / 2 - 174 / 2, getChieudai() / 3);
			scorenumber.drawScore(t, 5, 5);
			break;
		case 2:
			scorenumber.drawScore(t, 5, 5);
			bird.drawItself(t);
			break;
		case 3:
			t.imagem("flappy.png", 292, 398, 188, 38, 0, getChieurong() / 2 - 188 / 2, 100);
			t.imagem("flappy.png", 292, 116, 226, 116, 0, getChieurong() / 2 - 226 / 2, getChieudai() / 2 - 116 / 2);
			scorenumber.drawScore(t, getChieurong() / 2 + 50, getChieudai() / 2 - 25);
			scorenumber.drawRecord(t, getChieurong() / 2 + 55, getChieudai() / 2 + 16);
			t.texto("Ấn Space để quay về menu chính", 20, getChieudai() / 2 + 100, 22, Cor.Red);
			break;
		}
	}

	public static void main(String[] args) {
		roda();
	}

	private static void roda() {
		new Hethong(new FlappyBird());
	}

}
