package Hilos_Cerrojos;

import java.util.concurrent.Semaphore;

class Hijo4 extends Thread{

	private final Semaphore mutex;
	private final Semaphore avisar;
	private final Semaphore espacio;
	private int ident;
	public static int numero;

	public Hijo4(int ident, Semaphore mutex, Semaphore avisar, Semaphore espacio){
		this.ident = ident;
		this.mutex=mutex;
		this.avisar=avisar;
		this.espacio=espacio;
	}

	public void run(){


	}

}

class montior{

	private int hola;

	synchronized void inPersona(){



	}


}

public class prueab {
	public static void main(String[] args) {


		Semaphore mutex=new Semaphore(1);
		Semaphore avisar=new Semaphore(0);
		Semaphore espacio=new Semaphore(4);

		Hijo4 hijo=new Hijo4(1,mutex,avisar,espacio);
		hijo.start();

	}

}
