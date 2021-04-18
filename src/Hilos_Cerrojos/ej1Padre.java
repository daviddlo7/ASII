package Hilos_Cerrojos;


/**
 * JavaFile******************************************************************
 * <p>
 * FileName    [Program example with/without Threads]
 * <p>
 * Synopsis [Contains three classes, that should be executed as threads ]
 * <p>
 * Author      [Iria Estevez-Ayres <ayres@it.uc3m.es>]
 * <p>
 * Copyright   [Copyright (c) 2020 Carlos III University of Madrid
 * All rights reserved.
 * <p>
 * Permission is hereby granted, without written agreement and without license
 * or royalty fees, to use, copy, modify, and distribute this software and its
 * documentation for any purpose, provided that the above copyright notice and
 * the following two paragraphs appear in all copies of this software.
 * <p>
 * IN NO EVENT SHALL THE CARLOS III UNIVERSITY OF MADRID BE LIABLE TO ANY PARTY
 * FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING
 * OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE CARLOS III
 * UNIVERSITY OF MADRID HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * THE CARLOS III UNIVERSITY OF MADRID SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS ON AN
 * "AS IS" BASIS, AND CARLOS III UNIVERSITY OF MADRID HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.]
 ******************************************************************************/

import java.lang.*;
import java.util.*;

/*
 * Arquitectura de Sistemas II (2020-21)
 * Ejemplo transparencias 1
 */
class Hijo extends Thread {
	private int id;

	public Hijo(int id) {
		this.id = id;
	}

	public void run() {
		if (this.id == 0)
			System.out.println("Hola Luke");
		else
			System.out.println("Skywalker");
	}
}

public class ej1Padre {
	public static void main(String[] arg) {
		Hijo h1, h2;
		h1 = new Hijo(0);
		h2 = new Hijo(1);
		h2.start();
		h1.start();
		try {

			h1.join();
			h2.join();
		} catch (Exception e) {
		}

		System.out.println("Soy tu padre");
	}
}

