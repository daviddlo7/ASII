package Monitores;

class Court {

	private int players, referees;
	boolean playing = false;

	public synchronized void inPlayer() {
		while (players == 10 || playing) {
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
		}
		players++;
	}

	public synchronized void inReferee() {
		while (referees == 1 || playing) {
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
		}
		referees++;
	}

	public synchronized void outPlayer() {
		players--;
		notifyAll();
	}

	public synchronized void outReferee() {
		referees--;
		notifyAll();
	}

	public synchronized void playGame() {
		if (players == 10 && referees == 1) {
			playing = true;
			notifyAll();
			System.out.println("Empieza el partido");
		} else {
			while (!playing) {
				try {
					this.wait();
				} catch (InterruptedException ignored) {
				}
			}
		}
	}
}

class Player extends Thread {

	Court basketballCourt;
	int ident;

	public Player(Court court, int ident) {
		this.basketballCourt = court;
		this.ident = ident;
	}

	public void run() {
		basketballCourt.inPlayer();
		System.out.println("Soy el jugador " + ident + " y entro en la pista");
		basketballCourt.playGame();
		System.out.println("Soy el jugador " + ident + " voy a jugar");
		try {
			Thread.sleep(7000);
		} catch (InterruptedException ignored) {
		}
		basketballCourt.outPlayer();
		System.out.println("Soy el jugador " + ident + " y salgo de la pista");
	}
}

class Referee extends Thread {
	Court basketballCourt;
	int ident;

	public Referee(Court basketballCourt, int ident) {
		this.ident = ident;
		this.basketballCourt = basketballCourt;
	}

	public void run() {
		basketballCourt.inReferee();
		System.out.println("Soy el arbitro " + ident + " y entro en la pista");
		basketballCourt.playGame();
		System.out.println("Soy el arbitro " + ident + " voy a arbitrar");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {
		}
		basketballCourt.outReferee();
		System.out.println("Soy el arbitro " + ident + " y salgo de la pista");
	}
}

public class Basketball {
	public static void main(String[] args) {

		Court basketballCourt = new Court();

		for (int i = 0; i < 20; i++) {
			Player player = new Player(basketballCourt, i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignored) {
			}
			player.start();
		}
		for (int i = 0; i < 3; i++) {
			Referee referee = new Referee(basketballCourt, i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignored) {
			}
			referee.start();
		}
	}
}
