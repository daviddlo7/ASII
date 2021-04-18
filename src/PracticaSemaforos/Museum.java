package PracticaSemaforos;


import java.lang.*;
import java.util.*;
import java.util.concurrent.Semaphore;

class Person extends Thread {
    private int me;
    private Semaphore s45Sem, s20aSem, s40Sem, s20bSem, s30Sem;
    private static int numIn45, numIn20a, numIn40, numIn20b, numIn30;

    private Semaphore mutex45, mutex20a, mutex40, mutex20b, mutex30;
    private Random rnd = new Random();

    Person(int me, Semaphore s45Sem, Semaphore s20aSem, Semaphore s40Sem, Semaphore s20bSem, Semaphore s30Sem,
           Semaphore mutex45, Semaphore mutex20a, Semaphore mutex40, Semaphore mutex20b, Semaphore mutex30) {
        this.s45Sem = s45Sem;
        this.s20aSem = s20aSem;
        this.s40Sem = s40Sem;
        this.s20bSem = s20bSem;
        this.s30Sem = s30Sem;
        this.mutex45 = mutex45;
        this.mutex20a = mutex20a;
        this.mutex40 = mutex40;
        this.mutex20b = mutex20b;
        this.mutex30 = mutex30;
        this.me = me;
    }

    private void room45in(int time) {

        try {
            mutex45.acquire();
        } catch (Exception e) {
        }
        numIn45++;
        mutex45.release();

        if (time == 2) {
            try {
                mutex20a.acquire();
            } catch (Exception e) {
            }
            numIn20a--;
            mutex20a.release();
        }

        try {
            mutex45.acquire();
        } catch (Exception e) {
        }
        System.out.println("[ " + me + " ] I am inside room 45 " + time + " time. There is " + numIn45 + " people");
        mutex45.release();

    }

    private void room20ain() {

        try {
            mutex20a.acquire();
        } catch (Exception e) {
        }
        numIn20a++;
        mutex20a.release();

        try {
            mutex45.acquire();
        } catch (Exception e) {
        }
        numIn45--;
        mutex45.release();

        try {
            mutex20a.acquire();
        } catch (Exception e) {
        }
        System.out.println("[ " + me + " ] I am inside room 20a. There is " + numIn20a + " people");
        mutex20a.release();
    }

    private void room40in(int time) {

        try {
            mutex40.acquire();
        } catch (Exception e) {
        }
        numIn40++;
        mutex40.release();

        if (time == 1) {
            try {
                mutex45.acquire();
            } catch (Exception e) {
            }
            numIn45--;
            mutex45.release();
        }

        if (time == 2) {
            try {
                mutex20b.acquire();
            } catch (Exception e) {
            }
            numIn20b--;
            mutex20b.release();
        }
        try {
            mutex40.acquire();
        } catch (Exception e) {
        }
        System.out.println("[ " + me + " ] I am inside room 40 " + time + " time. There is " + numIn40 + " people");
        mutex40.release();
    }

    private void room20bin() {
        try {
            mutex20b.acquire();
        } catch (Exception e) {
        }
        numIn20b++;
        mutex20b.release();
        try {
            mutex40.acquire();
        } catch (Exception e) {
        }
        numIn40--;
        mutex40.release();

        try {
            mutex20b.acquire();
        } catch (Exception e) {
        }
        System.out.println("[ " + me + " ] I am inside room 20b. There is " + numIn20b + " people");
        mutex20b.release();
    }

    private void room30in() {
        try {
            mutex30.acquire();
        } catch (Exception e) {
        }
        numIn30++;
        mutex30.release();
        try {
            mutex40.acquire();
        } catch (Exception e) {
        }
        numIn40--;
        mutex40.release();

        try {
            mutex30.acquire();
        } catch (Exception e) {
        }
        System.out.println("[ " + me + " ] I am inside room 30. There is " + numIn30 + " people");
        mutex30.release();
    }

    private void room30out() {
        try {
            mutex30.acquire();
        } catch (Exception e) {
        }
        numIn30--;
        mutex30.release();
    }

    public void run() {
        System.out.println("[ " + me + " ] Trying to enter");

        try {
            s45Sem.acquire();
        } catch (Exception e) {
        }

        room45in(1);
        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (Exception e) {
        }

        try {
            s20aSem.acquire();
        } catch (Exception e) {
        }

        room20ain();
        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (Exception e) {
        }

        s20aSem.release();

        room45in(2);
        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (Exception e) {
        }

        try {
            s40Sem.acquire();
        } catch (Exception e) {
        }
        s45Sem.release();

        room40in(1);
        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (Exception e) {
        }

        try {
            s20bSem.acquire();
        } catch (Exception e) {
        }
        room20bin();
        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (Exception e) {
        }

        s20bSem.release();

        room40in(2);
        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (Exception e) {
        }

        s40Sem.release();
        try {
            s30Sem.acquire();
        } catch (Exception e) {
        }

        room30in();

        try {
            Thread.sleep(rnd.nextInt(1000));
        } catch (Exception e) {
        }

        room30out();
        s30Sem.release();

        System.out.println("[ " + me + " ] Outside the museum");
    }
}

public class Museum {

    public static void main(String[] args) {

        Person p;
        Semaphore s45Sem, s20aSem, s40Sem, s20bSem, s30Sem;
        Semaphore mutex45, mutex20a, mutex40, mutex20b, mutex30;

        s45Sem = new Semaphore(45);
        s20aSem = new Semaphore(20);
        s40Sem = new Semaphore(40);
        s20bSem = new Semaphore(20);
        s30Sem = new Semaphore(30);

        mutex45 = new Semaphore(1);
        mutex20a = new Semaphore(1);
        mutex40 = new Semaphore(1);
        mutex20b = new Semaphore(1);
        mutex30 = new Semaphore(1);

        for (int i = 0; i < 100; i++) {
            p = new Person(i, s45Sem, s20aSem, s40Sem, s20bSem, s30Sem, mutex45, mutex20a, mutex40, mutex20b, mutex30);
            p.start();
        }
    }
}
