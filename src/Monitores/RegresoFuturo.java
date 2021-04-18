package Monitores;


class CocheVolador extends Thread {
	private final GasoRecicladora gasolinera;
	private final int ident;

	public CocheVolador(GasoRecicladora gasolinera, int ident) {
		this.gasolinera = gasolinera;
		this.ident = ident;
	}

	public void run() {
		System.out.println("Soy " + ident + " e intento entrar en el Pasillo");
		gasolinera.entroPasillo();
		System.out.println("Soy " + ident + " e intento salir del Pasillo");
		gasolinera.salgoPasillo();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {
		}
		System.out.println("Soy " + ident + " e intento salir de la gaso");
		gasolinera.salgo();
		System.out.println("Soy " + ident + " e intento salir del Pasillo");
		gasolinera.salgoPasillo();
	}
}

class GasoRecicladora {
	int nPasillo;
	int plazasDisponibles = 2;
	int sentidoPasillo = -1;

	synchronized void entroPasillo() {
		while ((plazasDisponibles == 0 || sentidoPasillo == 0)) {
			try {
				this.wait();
			} catch (InterruptedException ignore) {
			}
		}
		sentidoPasillo = 1;
		plazasDisponibles--;
		nPasillo++;
	}

	synchronized void salgoPasillo() {
		nPasillo--;
		if (nPasillo == 0) {
			notifyAll();
			sentidoPasillo = -1;
		}
	}

	synchronized void salgo() {
		while (sentidoPasillo == 1) {
			try {
				this.wait();
			} catch (InterruptedException ignore) {
			}
		}
		nPasillo++;
		sentidoPasillo = 0;
		plazasDisponibles++;
	}
}

public class RegresoFuturo {
	public static void main(String[] args) {

		CocheVolador coche;
		GasoRecicladora gasolinera = new GasoRecicladora();
		for (int i = 0; i < 4; i++) {
			coche = new CocheVolador(gasolinera, i);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ignored) {
			}
			coche.start();
		}
	}

}
