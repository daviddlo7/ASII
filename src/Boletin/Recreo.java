package Boletin;

import java.util.concurrent.Semaphore;

class Nino extends Thread {
	private static final boolean I = false;
	private static final boolean P = true;

	private static int nInfantil, nPrimaria, ninos;
	private final Semaphore mutexPrimaria;
	private final Semaphore mutexInfantil;
	private final Semaphore salir;
	private final Semaphore seleccionadosInfantil;
	private final Semaphore seleccionadosPrimaria;
	private final Semaphore mutexNinos;
	private final boolean tipo;
	private int numero;

	public Nino(boolean b, Semaphore mutexPrimaria, Semaphore mutexInfantil, Semaphore salir, Semaphore seleccionadosInfantil, Semaphore seleccionadosPrimaria, Semaphore mutexNinos) {
		this.tipo = b;
		this.mutexPrimaria = mutexPrimaria;
		this.mutexInfantil = mutexInfantil;
		this.salir = salir;
		this.seleccionadosInfantil = seleccionadosInfantil;
		this.seleccionadosPrimaria = seleccionadosPrimaria;
		this.mutexNinos = mutexNinos;
	}

	public void darseMano(String type) {
		System.out.println(type + numero + " Nos damos la mano");

		try {
			mutexNinos.acquire();
		} catch (InterruptedException ignored) {
		}
		ninos--;
		if (ninos == 0) {
			seleccionadosInfantil.release();
			seleccionadosInfantil.release();
			seleccionadosPrimaria.release();
		}
		mutexNinos.release();
	}

	public void seleccionado(String type) {

		System.out.println(type + numero + " seleccionado");

		try {
			mutexNinos.acquire();
		} catch (InterruptedException ignored) {
		}
		ninos++;
		if (ninos == 3) {
			mutexNinos.release();

			System.out.println(type + numero + " Somos 3!!");

			salir.release();
			salir.release();

			darseMano(type);


		} else {
			mutexNinos.release();
			System.out.println(type + numero + " No somos 3. Espero");

			try {
				salir.acquire();
			} catch (InterruptedException ignored) {
			}
			darseMano(type);

		}

	}

	public void run(){ //mutexInfantil.release();
		String type;
		if(tipo==I){
			type="INF";
			try {
				mutexInfantil.acquire();
			} catch (InterruptedException ignored) {
			}
			nInfantil++;
			numero=nInfantil-1;
			mutexInfantil.release();

			System.out.println(type+numero+ " en el punto de encuentro");

			try {
				seleccionadosInfantil.acquire();
			} catch (InterruptedException ignored) {
			}
			seleccionado(type);

		}
		if(tipo==P){
			type="PRI";
			try {
				mutexPrimaria.acquire();
			} catch (InterruptedException ignored) {
			}
			nPrimaria++;
			numero=nPrimaria-1;
			mutexPrimaria.release();

			System.out.println(type+numero+ " en el punto de encuentro");

			try {
				seleccionadosPrimaria.acquire();
			} catch (InterruptedException ignored) {
			}

			seleccionado(type);

		}
	}
}

public class Recreo {
	public static void main(String[] args) {
		Semaphore mutexPrimaria, mutexInfantil,mutexNinos;
		Semaphore salir,seleccionadosInfantil,seleccionadosPrimaria;
		Nino nino;

		mutexPrimaria=new Semaphore(1);
		mutexInfantil = new Semaphore(1);
		salir=new Semaphore(0);
		seleccionadosInfantil=new Semaphore(2);
		seleccionadosPrimaria=new Semaphore(1);
		mutexNinos=new Semaphore(1);

		for (int i = 0; i < 9; i++) {
			if(i==0||i==1||i==3||i==4|| i==6||i==7){
				nino=new Nino(false,mutexPrimaria,mutexInfantil,salir, seleccionadosInfantil, seleccionadosPrimaria,mutexNinos);
			}else{
				nino = new Nino(true, mutexPrimaria, mutexInfantil, salir, seleccionadosInfantil,seleccionadosPrimaria,mutexNinos);
			}
			nino.start();
		}
	}
}
