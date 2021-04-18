package Monitores;


class Adult extends Thread {
	Guarde guarderia;
	int ident;
	public Adult(Guarde guarderia, int ident){
		this.guarderia=guarderia;
		this.ident=ident;
	}

	public void run(){
		guarderia.inAdulto();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {
		}
		guarderia.outAdulto();
	}
}

class Child extends Thread {
	Guarde guarderia;
	int ident;
	public Child(Guarde guarderia, int ident){
		this.guarderia=guarderia;
		this.ident=ident;
	}

	public void run(){
		guarderia.inNiño();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException ignored) {
		}
		guarderia.outNiño();
	}
}



class Guarde {
	private int nAdultos, nNiños;

	synchronized void inNiño() {
		while (nNiños == nAdultos * 3) {
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
		}
		nNiños++;
		System.out.println("Entra niño. Hay " + nNiños);
	}

	synchronized void inAdulto() {
		nAdultos++;
		System.out.println("Entra adulto. Hay " + nAdultos);
		this.notifyAll();
	}

	synchronized void outNiño() {
		nNiños--;
		System.out.println("Sale niño. Hay " + nNiños);
		this.notifyAll();
	}

	synchronized void outAdulto() {
		while (nNiños > 3 * (nAdultos - 1)) {
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
		}
		nAdultos--;
		System.out.println("Sale adulto. Hay " + nAdultos);
	}

}

public class Guarderia {
	public static void main(String[] args) {
		Guarde guarderia=new Guarde();

		for (int i = 0; i < 16; i++) {
			Child niño=new Child(guarderia,i);
			niño.start();
		}
		for (int i = 0; i < 3; i++) {
			Adult adult=new Adult(guarderia,i);
			adult.start();
		}
	}

}
