package PracticaSinscronizacion;
//Los hilos, que son los aviones

class AvionesM extends Thread {
	private int id;
	private boolean estado = false; //Volando=true
	AeropuertoM aeropuerto;

	public AvionesM(int id, AeropuertoM aeropuerto) {
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
			Thread.sleep(50); //De 0 a 50ms en la pista
		} catch (Exception ignored) {
		}
		aeropuerto.inHangar(pistaAsignada);
		System.out.println("[" + id + "] En el hangar");
		try {
			Thread.sleep(500); //Espero 0 a 500ms en volver a querer salir
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
			Thread.sleep(50); //De 0 a 50ms en la pista
		} catch (Exception ignored) {
		}
		aeropuerto.entroEspacioAereoDespegando(pistaAsignada);
		System.out.println("[" + id + "] En el espacio aereo");
		try {
			Thread.sleep(10); //De 0 a 10ms en el espacio aereo
		} catch (Exception ignored) {
		}
		aeropuerto.salgoEspacioAereo();
		System.out.println("[" + id + "] Me voy");
	}
}

class AeropuertoM {
	public static final boolean VACIA = false;
	public static final boolean LLENA = true;
	public static final boolean PISTA_A = true;
	public static final boolean PISTA_B = false;


	private int N, M;//N=Numero espacio aéreo, M=Numero de la cola por pista
	private int nEspacioAereo, nPistaA, nPistaB, nHangar; //nPista significa el numero de aviones asignados a esa pista
	private boolean pistaA = VACIA, pistaB = VACIA;//significa que pista a true está ocupada, false esta vacía. Lo mismo con la B

	public AeropuertoM(int huecoEA, int huecoPista) {
		this.N = huecoEA;
		this.M = huecoPista;
	}

	public synchronized boolean entroEspacioAereoAterrizando() {
		while ((nPistaA == M && nPistaB == M) || nEspacioAereo == N) {
			try {
				this.wait();
			} catch (Exception ignored) {
			}
		}//Puedes entrar en el espacio aéreo y tener pista asignada
		nEspacioAereo++;
		System.out.println("Espacio Aereo: "+nEspacioAereo);
		if (nPistaA > nPistaB) {
			nPistaB++;
			return PISTA_B;
		} else {
			nPistaA++;
			return PISTA_A;
		}
	}

	public synchronized void aterrizo(boolean pista) {
		if (pista == PISTA_A) {
			while (pistaA == LLENA) {
				try {
					this.wait();
				} catch (Exception ignored) {
				}
			}
			pistaA = LLENA;
			nPistaA--;
		} else {
			while (pistaB == LLENA) {
				try {
					this.wait();
				} catch (Exception ignored) {
				}
			}
			pistaB = LLENA;
			nPistaB--;
		}
		nEspacioAereo--;
		notifyAll();
	}

	public synchronized void inHangar(boolean pista) {
		if (pista == PISTA_A) {
			pistaA = VACIA;
		} else {
			pistaB = VACIA;
		}
		nHangar++;
		notifyAll();
	}

	public synchronized boolean quieroDespegar() {

		while ((nPistaA == M && nPistaB == M) || nEspacioAereo == N) {
			try{
				this.wait();
			} catch (Exception ignored) {
			}
		}//Te puedo asignar una pista
		if (nPistaA > nPistaB) {
			nPistaB++;
			return PISTA_B;
		} else {
			nPistaA++;
			return PISTA_A;
		}
	}

	public synchronized void despegar(boolean pista) {
		if (pista == PISTA_A) {
			while (pistaA == LLENA) {
				try {
					this.wait();
				} catch (Exception ignored) {
				}
			}
			pistaA = LLENA;
		} else {
			while (pistaB == LLENA) {
				try {
					this.wait();
				} catch (Exception ignored) {
				}
			}
			pistaB = LLENA;
		}
		notifyAll();
	}

	public synchronized void entroEspacioAereoDespegando(boolean pista) {
		if (pista == PISTA_A) {
			nPistaA--;
			pistaA=VACIA;
		} else {
			nPistaB--;
			pistaB=VACIA;
		}
		nEspacioAereo++;
		notifyAll();
	}

	public synchronized void salgoEspacioAereo() {
		nEspacioAereo--;
		notifyAll();
	}

}

public class AeropuertoMonitor {
	public static void main(String[] args) {
		int M = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		System.out.println("M=" + M + ", N=" + N);

		AeropuertoM aeropuerto = new AeropuertoM(M, N);
		AvionesM avion;

		for (int i = 0; i < 20; i++) {
			avion = new AvionesM(i, aeropuerto);
			avion.start();
		}
	}
}