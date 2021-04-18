package Semaforos;


/**JavaFile******************************************************************

 FileName    [Program that prints the dialogue between Luke Skywalker and
 Darth Vader]

 Synopsis [Contains three classes, that should be executed as threads ]

 Author      [Iria Estevez-Ayres <ayres@it.uc3m.es>]

 Copyright   [Copyright (c) 2019 Carlos III University of Madrid
 All rights reserved.

 Permission is hereby granted, without written agreement and without license
 or royalty fees, to use, copy, modify, and distribute this software and its
 documentation for any purpose, provided that the above copyright notice and
 the following two paragraphs appear in all copies of this software.

 IN NO EVENT SHALL THE CARLOS III UNIVERSITY OF MADRID BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING
 OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE CARLOS III
 UNIVERSITY OF MADRID HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 THE CARLOS III UNIVERSITY OF MADRID SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS ON AN
 "AS IS" BASIS, AND CARLOS III UNIVERSITY OF MADRID HAS NO OBLIGATION TO
 PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.]

 ******************************************************************************/
import java.lang.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/*
 * Arquitectura de Sistemas II (2020-21)
 *
 * Ejercicio en clase:
 *
 * Sincroniza a Luke y Darth Vader para que el dialogo tenga
 * sentido.
 *
 * El código de sincronización debe estar dentro de la clase Lock
 *
 */

class Luke extends Thread {

    private Semaphore semDV, semLS;

    Luke(Semaphore semDV, Semaphore semLS) {
        this.semDV = semDV;
        this.semLS = semLS;
    }

    public void run() {

        try {
            semLS.acquire();
        } catch (Exception e) {
        }

        System.out.println("(Luke): He told me enough! He told me YOU killed him!");

        semDV.release();
        try {
            semLS.acquire();
        } catch (Exception e) {
        }

        System.out.println("(Luke): No. No. That's not true. That's impossible!");

        semDV.release();
        try {
            semLS.acquire();
        } catch (Exception e) {
        }

        System.out.println("(Luke): No! No!");
    }
}

class DarthVader extends Thread {
    private Semaphore semLS, semDV;

    DarthVader(Semaphore semDV, Semaphore semLS) {
        this.semDV = semDV;
        this.semLS = semLS;
    }

    public void run() {
        System.out.println(
                "(DV): If you only knew the power of the Dark Side. Obi-Wan never told you what happened to your father.");

        semLS.release();
        try {
            semDV.acquire();
        } catch (Exception e) {
        }

        System.out.println("(DV): No. I am your father.");

        semLS.release();
        try {
            semDV.acquire();
        } catch (Exception e) {
        }

        System.out.println("(DV): Search your feelings, you KNOW it to be true!");

        semLS.release();
    }
}

public class SemaforosStarWars {

    public static void main(String[] args) {
        Semaphore semDV = new Semaphore(0);
        Semaphore semLS = new Semaphore(0);
        Luke luke = new Luke(semDV, semLS);
        DarthVader daddy = new DarthVader(semDV, semLS);
        daddy.start();
        luke.start();
    }
}

