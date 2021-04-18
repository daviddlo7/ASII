package Semaforos;

import java.util.concurrent.Semaphore;

public class BadPhilosopher extends Thread {

    private Semaphore palillo1;
    private Semaphore palillo2;
    private int ident;

    public BadPhilosopher(int id, Semaphore palillo1, Semaphore palillo2) {
        this.ident = id;
        this.palillo1 = palillo1;
        this.palillo2 = palillo2;
    }

    public void comer() {
        System.out.println("[ " + ident + " ] Intento palillo 1");
        try {
            palillo1.acquire();
        } catch (Exception e) {
        }
        ;

        // Fuerzo el deadlock para que se vea
        try {
            Thread.sleep(this.ident); // duerme un num. de segundos dependiendo
            // de su identificador
        } catch (Exception e) {
        }
        ;
        System.out.println("[ " + ident + " ] Intento palillo 2");
        try {
            palillo2.acquire();
        } catch (Exception e) {
        }
        ;

        System.out.println("[ " + ident + " ] comiendo");
    }

    public void termino_de_comer() {
        palillo2.release();
        palillo1.release();
        System.out.println("[ " + ident + " ] termino");
    }

    public void run() {
        this.comer();
        this.termino_de_comer();
    }

    public static void main(String[] args) {

        BadPhilosopher f1, f2, f3, f4;
        Semaphore palilloA, palilloB, palilloC, palilloD;
        palilloA = new Semaphore(1);
        palilloB = new Semaphore(1);
        palilloC = new Semaphore(1);
        palilloD = new Semaphore(1);
        /*
         * El problema radica en el orden en el que agarran los palillos.
         */
        f1 = new BadPhilosopher(1, palilloA, palilloB);
        f2 = new BadPhilosopher(2, palilloB, palilloC);
        f3 = new BadPhilosopher(3, palilloC, palilloD);
        f4 = new BadPhilosopher(4, palilloA, palilloD);
        f4.start();
        f1.start();
        f3.start();
        f2.start();
    }
}

