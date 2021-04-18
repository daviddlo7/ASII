package PracticaSemaforos;

import java.lang.*;
import java.util.*;

import java.util.concurrent.Semaphore;

class Santa extends Thread {

    private Semaphore santaSleep;
    private Semaphore resolverDuda;

    public Santa(Semaphore santaSleep, Semaphore resolverDuda) {
        this.santaSleep = santaSleep;
        this.resolverDuda = resolverDuda;
    }

    public void run() {
        do {
            System.out.println("(Santa) Espero en el adquire\n");
            try {
                santaSleep.acquire();
            } catch (Exception e) {
            }
            ;

            /*
             * try { contadorElfos.acquire(); } catch (Exception e) { } ;
             */
            if (Reindeer.nElfos == 9) {

                System.out.println("(Santa) Me voy de a repartir regalos con los renos\n");
                try {
                    sleep(5000);
                } catch (Exception e) {
                }
                System.out.println("(Santa) Ya he vuelto de viaje 5 segundos\n");

                Reindeer.nElfos = 0;

                // contadorElfos.release();
            } else {
                /*
                 * try { contadorDuendes.acquire(); } catch (Exception e) { } ;
                 */

                System.out.println("(Santa) Me voy de a resolver dudas con los duendes\n");
                try {
                    sleep(5000);
                } catch (Exception e) {
                }

                resolverDuda.release(3);

                System.out.println("(Santa) Ya he vueltoooo\n");

                // contadorElfos.release();

            }

        } while (true);

    }
}

class Elve extends Thread {

    private Semaphore santaSleep;
    private Semaphore contadorDuendes;
    private Semaphore duendesLleno;
    private Semaphore resolverDuda;
    public static int nDuendes = 0;

    public Elve(Semaphore santaSleep, Semaphore contadorDuendes, Semaphore duendesLleno, Semaphore resolverDuda) {
        this.santaSleep = santaSleep;
        this.contadorDuendes = contadorDuendes;
        this.duendesLleno = duendesLleno;
        this.resolverDuda = resolverDuda;
    }

    public void run() {
        try {
            duendesLleno.acquire();
        } catch (Exception e) {
        }

        try {
            contadorDuendes.acquire();
        } catch (Exception e) {
        }
        nDuendes++;
        System.out.println("Soy el duende " + nDuendes + "\n");
        contadorDuendes.release();

        if (nDuendes % 3 == 0) {
            System.out.println("Hemos llegado a 3 duendes, release Santa\n");
            santaSleep.release();
        }

        try {
            resolverDuda.acquire();
        } catch (Exception e) {
        }
        System.out.println("Me ha resuelto la duda\n");
        if (nDuendes % 3 == 0) {
            duendesLleno.release(3);
        }
    }

}

class Reindeer extends Thread {

    private Semaphore santaSleep;
    private Semaphore contadorElfos;
    public static int nElfos = 0;

    public Reindeer(Semaphore santaSleep, Semaphore contadorElfos) {
        this.santaSleep = santaSleep;
        this.contadorElfos = contadorElfos;
    }

    public void run() {

        try {
            contadorElfos.acquire();
        } catch (Exception e) {
        }
        ;

        nElfos++;
        System.out.println("Soy el reno " + nElfos + "\n");
        contadorElfos.release();

        if (nElfos == 9) {
            santaSleep.release();

        }
    }

}

public class NorthPole {

    public static void main(String[] args) {

        // You can (and should) create new constructors for each class
        // So, you will also need to modify this main
        Semaphore santaSleep = new Semaphore(0);
        Semaphore contadorElfos = new Semaphore(1);
        Semaphore contadorDuendes = new Semaphore(1);
        Semaphore duendesLleno = new Semaphore(3);
        Semaphore resolverDuda = new Semaphore(0);

        Santa claus = new Santa(santaSleep, resolverDuda);
        Reindeer rudolf;
        Elve legolas;

        claus.start();

        for (int i = 0; i < 9; i++) {
            rudolf = new Reindeer(santaSleep, contadorElfos);
            try {
                Thread.sleep(250);
            } catch (Exception e) {
                // TODO: handle exception
            }
            rudolf.start();
        }

        for (int i = 0; i < 20; i++) {
            legolas = new Elve(santaSleep, contadorDuendes, duendesLleno, resolverDuda);
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                // TODO: handle exception
            }
            legolas.start();
        }

    }
}