package Semaforos;

import java.util.concurrent.Semaphore;

class Personal extends Thread {
	private int ident;
	private static int nEntrada, nVestuario,nPista;
	Semaphore mutexEntrada, mutexPista, mutexVestuario, entrada, vestuarios, pista;
	public Personal(int i, Semaphore mutexEntrada, Semaphore mutexPista, Semaphore mutexVestuario, Semaphore entrada, Semaphore vestuarios, Semaphore pista) {
		this.ident=i;
		this.mutexEntrada=mutexEntrada;
		this.mutexPista=mutexPista;
		this.mutexVestuario=mutexVestuario;
		this.entrada=entrada;
		this.vestuarios=vestuarios;
		this.pista=pista;
	}

	public void run() {
		try {
			entrada.acquire();
		} catch (InterruptedException ignored) {
		}
		try {
			mutexEntrada.acquire();
		} catch (InterruptedException ignored) {
		}
		nEntrada++;
		mutexEntrada.release();
		try {
			vestuarios.acquire();
		} catch (InterruptedException ignored) {
		}

	}
}

class Espectador extends Thread{
	private int ident;
	private static int espectadores;
	Semaphore mutexGradas,gradas;
	public Espectador(int i, Semaphore mutexGradas, Semaphore gradas) {
		this.ident=i;
		this.mutexGradas=mutexGradas;
		this.gradas=gradas;
	}
	public void run(){
		try {
			gradas.acquire();
		} catch (InterruptedException ignored) {
		}
		try {
			mutexGradas.acquire();
		} catch (InterruptedException ignored) {
		}
		espectadores++;
		mutexGradas.release();
		gradas.release();
	}
}


public class Pabellon {
	public static void main(String[] args) {
		Personal p;
		Espectador e;

		Semaphore mutexEntrada,mutexVestuario,mutexPista,mutexGradas;
		Semaphore entrada,vestuarios,pista,gradas;

		mutexEntrada = new Semaphore(1);
		mutexPista = new Semaphore(1);
		mutexVestuario = new Semaphore(1);
		mutexGradas = new Semaphore(1);

		entrada = new Semaphore(20);
		vestuarios = new Semaphore(10);
		pista = new Semaphore(25);
		gradas = new Semaphore(150);

		for (int i = 0; i < 30; i++) {
			p=new Personal(i,mutexEntrada,mutexPista,mutexVestuario,entrada,vestuarios,pista);
			p.start();
		}
		for (int i = 0; i < 170; i++) {
			e=new Espectador(i,mutexGradas,gradas);
			e.start();
		}
	}
}
