package Monitores;

import java.lang.*;

class Client extends Thread {
	private final int ident;
	private final BarberShop barberShop;

	public Client(int ident, BarberShop barberShop) {
		this.ident = ident;
		this.barberShop = barberShop;
	}

	public void run() {
		if (!barberShop.inSala1()) {
			return;
		}

		System.out.println("Soy el cliente " + ident + " e intento entrar en la Sala1");
		barberShop.inSala2();

		System.out.println("Soy el cliente " + ident + " e intento entrar en la Sala2");
		barberShop.inBarber();
		System.out.println("Soy el cliente " + ident + " y entro al barbero");
		try {
			Thread.sleep(2000);
		} catch (Exception ignored) {
		}
		barberShop.outBarber();
		System.out.println("Soy el cliente " + ident + " y salgo del barbero");
	}
}

class BarberShop {
	private int numSala1, numSala2;
	private boolean barber = false;

	public synchronized boolean inSala1() {
		if (numSala1 == 3)
			return false;
		numSala1++;
		return true;
	}

	public synchronized void inSala2() {
		while (numSala2 == 15) {
			try {
				this.wait();
			} catch (Exception ignored) {
			}
		}
		numSala2++;
		numSala1--;
	}

	public synchronized void inBarber() {
		while (barber) {
			try {
				this.wait();
			} catch (Exception ignored) {
			}
		}
		barber = true;
		numSala2--;
		notifyAll();
	}

	public synchronized void outBarber() {
		barber = false;
		this.notifyAll();
	}
}

public class Barber {
	public static void main(String[] args) {

		Client cliente;
		BarberShop barberShop = new BarberShop();

		for (int i = 0; i < 20; i++) {
			cliente = new Client(i, barberShop);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ignored) {
			}
			cliente.start();
		}

	}
}