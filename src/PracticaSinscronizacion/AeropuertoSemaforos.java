package PracticaSinscronizacion;

import java.util.concurrent.Semaphore;

class AvionesSem extends Thread {
	private int id;
	AeropuertoSem aeropuerto;
	public static int mevoy;

	public AvionesSem(int id, AeropuertoSem aeropuerto) {
		this.id = id;
		this.aeropuerto = aeropuerto;
	}


	public void run() { //Estoy volando y quiero aterrizar, y luego vuelvo a volar y fin
		System.out.println("[" + id + "] Antes del espacio aéreo");

		boolean pistaAsignada = aeropuerto.entroEspacioAereoAterrizando();//se puede quedar esperando si hay muchos aviones en el espacio aereo
		if (pistaAsignada) {
			System.out.println("[" + id + "] En el espacio aereo. Pista asignada: A");
		} else {
			System.out.println("[" + id + "] En el espacio aereo. Pista asignada: B");
		}

		aeropuerto.aterrizo(pistaAsignada);//se puede quedar esperando si hay muchos aviones en las pistas
		try {
			Thread.sleep((long) (Math.random()*50)); //De 0 a 50ms en la pista
		} catch (Exception ignored) {
		}
		aeropuerto.inHangar(pistaAsignada);
		System.out.println("[" + id + "] En el hangar");
		try {
			Thread.sleep((long) (Math.random()*500)); //Espero 0 a 500ms en volver a querer salir
		} catch (Exception ignored) {
		}
		System.out.println("[" + id + "] Quiero despegar");
		pistaAsignada = aeropuerto.quieroDespegar();//obligatorio ponerle hueco en el espacio aéreo
		if (pistaAsignada) {
			System.out.println("[" + id + "] En el hangar. Pista asignada: A");
		} else {
			System.out.println("[" + id + "] En el hangar. Pista asignada: B");
		}
		aeropuerto.despegar(pistaAsignada);
		try {
			Thread.sleep((long) (Math.random()*50)); //De 0 a 50ms en la pista
		} catch (Exception ignored) {
		}
		aeropuerto.entroEspacioAereoDespegando(pistaAsignada);
		System.out.println("[" + id + "] En el espacio aereo");
		try {
			Thread.sleep((long) (Math.random()*10)); //De 0 a 10ms en el espacio aereo
		} catch (Exception ignored) {
		}
		aeropuerto.salgoEspacioAereo();
		System.out.println("[" + id + "] Me voy");
		mevoy++;
		System.out.println("Me voy: " + mevoy);
	}
}

class AeropuertoSem {
	public static final boolean VACIA = false;
	public static final boolean LLENA = true;
	public static final boolean PISTA_A = true;
	public static final boolean PISTA_B = false;

	private Semaphore mutex = new Semaphore(1);
	private Semaphore esperoAvionEA = new Semaphore(0);
	private Semaphore esperoAvionPistaA = new Semaphore(0);
	private Semaphore esperoAvionPistaB = new Semaphore(0);

	private int N, M;//N=Numero espacio aéreo, M=Numero de la cola por pista
	private int nEspacioAereo, nPistaA, nPistaB; //nPista significa el numero de aviones asignados a esa pista
	private boolean pistaA = VACIA, pistaB = VACIA;//significa que pista a true está ocupada, false esta vacía. Lo mismo con la B

	public AeropuertoSem(int huecoEA, int huecoPista) {
		this.N = huecoEA;
		this.M = huecoPista;
	}

	public boolean entroEspacioAereoAterrizando() {
		try{ mutex.acquire(); } catch (Exception e) {}
		while ((nPistaA == M && nPistaB == M) || nEspacioAereo == N) {
			mutex.release();
			try{ esperoAvionEA.acquire(); } catch (Exception e) {}
			try{ mutex.acquire(); } catch (Exception e) {}
		}//Puedes entrar en el espacio aéreo y tener pista asignada
		nEspacioAereo++;
		System.out.println("Espacio Aereo: "+nEspacioAereo);
		if (nPistaA > nPistaB) {
			nPistaB++;
			mutex.release();
			return PISTA_B;
		} else {
			nPistaA++;
			mutex.release();
			return PISTA_A;
		}

	}

	public void aterrizo(boolean pista) {
		try{ mutex.acquire(); } catch (Exception e) {}
		if (pista == PISTA_A) {
			while (pistaA == LLENA) {
				mutex.release();
				try {
					esperoAvionPistaA.acquire();
				} catch (Exception ignored) {
				}
				try{ mutex.acquire(); } catch (Exception e) {}
			}
			pistaA = LLENA;
			nPistaA--;
		} else {
			while (pistaB == LLENA) {
				mutex.release();
				try {
					esperoAvionPistaB.acquire();
				} catch (Exception ignored) {
				}
				try{ mutex.acquire(); } catch (Exception e) {}
			}
			pistaB = LLENA;
			nPistaB--;
		}
		nEspacioAereo--;
		mutex.release();
		esperoAvionEA.release();
	}

	public void inHangar(boolean pista) {
		try{ mutex.acquire(); } catch (Exception e) {}
		if (pista == PISTA_A) {
			pistaA = VACIA;
			esperoAvionPistaA.release();
		} else {
			pistaB = VACIA;
			esperoAvionPistaB.release();
		}
		mutex.release();

	}

	public boolean quieroDespegar() {
		try{ mutex.acquire(); } catch (Exception e) {}
		while ((nPistaA == M && nPistaB == M)|| nEspacioAereo==N) {
			System.out.println("Entro en el acquire");
			mutex.release();
			try{
				esperoAvionEA.acquire();
			} catch (Exception ignored) {
			}
			System.out.println("Salgo del acquire");
			try{ mutex.acquire(); } catch (Exception e) {}
		}//Te puedo asignar una pista
		if (nPistaA > nPistaB) {
			nPistaB++;
			mutex.release();

			return PISTA_B;
		} else {
			nPistaA++;
			mutex.release();

			return PISTA_A;
		}
	}

	public void despegar(boolean pista) {
		try{ mutex.acquire(); } catch (Exception e) {}
		if (pista == PISTA_A) {
			while (pistaA == LLENA) {
				mutex.release();
				try {
					esperoAvionPistaA.acquire();
				} catch (Exception ignored) {
				}
				try{ mutex.acquire(); } catch (Exception e) {}
			}
			pistaA = LLENA;

		} else {
			while (pistaB == LLENA) {
				mutex.release();
				try {
					esperoAvionPistaB.acquire();
				} catch (Exception ignored) {

				}try{ mutex.acquire(); } catch (Exception e) {}
			}
			pistaB = LLENA;

		}
		mutex.release();

	}

	public void entroEspacioAereoDespegando(boolean pista) {
		try{ mutex.acquire(); } catch (Exception e) {}
		if (pista == PISTA_A) {
			nPistaA--;
			pistaA=VACIA;
			esperoAvionPistaA.release();
		} else {
			nPistaB--;
			pistaB=VACIA;
			esperoAvionPistaB.release();
		}
		nEspacioAereo++;
		mutex.release();

	}

	public void salgoEspacioAereo() {
		try{ mutex.acquire(); } catch (Exception e) {}
		nEspacioAereo--;
		mutex.release();
		esperoAvionEA.release();
	}

}

public class AeropuertoSemaforos {
	public static void main(String[] args) {
		int M = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		System.out.println("M=" + M + ", N=" + N);

		AeropuertoSem aeropuerto = new AeropuertoSem(M, N);
		AvionesSem avion;

		for (int i = 0; i < 20; i++) {
			avion = new AvionesSem(i, aeropuerto);
			avion.start();
		}
	}
}