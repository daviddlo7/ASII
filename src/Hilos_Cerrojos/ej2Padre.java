package Hilos_Cerrojos;


/**JavaFile******************************************************************

 FileName    [Program example with/without Threads]

 Synopsis [Contains three classes, that should be executed as threads ]

 Author      [Iria Estevez-Ayres <ayres@it.uc3m.es>]

 Copyright   [Copyright (c) 2020 Carlos III University of Madrid
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

/*
 * Arquitectura de Sistemas II (2020-21)
 * Ejemplo transparencias 1
 */
class Hijo2 implements Runnable {
    private int id;

    public Hijo2(int id) {
        this.id = id;
    }

    public void run() {
        if (this.id == 0)
            System.out.println("Hola Luke");
        else
            System.out.println("Skywalker");
    }
}

public class ej2Padre {
    public static void main(String[] arg) {
        Hijo2 h1, h2;
        Thread t1, t2;
        h1 = new Hijo2(0);
        h2 = new Hijo2(1);
        t1 = new Thread(h1);
        t2 = new Thread(h2);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
        }
        System.out.println("Soy tu padre");
    }
}

