package Hilos_Cerrojos;


/**
 * JavaFile******************************************************************
 * <p>
 * FileName    [Program that simulates a BankAccount]
 * <p>
 * Synopsis [Scrooge McDuck and Donald share an account. Scrooge deposits
 * money, Donald withdraws money]
 * <p>
 * Author      [Iria Estevez-Ayres <ayres@it.uc3m.es>]
 * <p>
 * Copyright   [Copyright (c) 2019 Carlos III University of Madrid
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
 * Arquitectura de Sistemas II.
 * a) Qué hace el código
 * b) Haz que tanto Donald como Scrooge llamen a sus funciones 10 veces
 *   ¿Qué ocurre ahora?
 */
class Account {

    private float balance = 0;
    private Random rnd = new Random();

    public void depositMoney(float amount) {

        float tmp;

        synchronized (this) {
            tmp = balance;
            System.out.println("(Adding money): the initial balance is: " + tmp);
            tmp += amount;
            balance = tmp;
            System.out.println("(Adding money): the final balance is: " + balance);
            this.notify();
        }

    }

    public void withdrawMoney(float amount) {

        float tmp;

        synchronized (this) {

            while (balance <= 0) {

                try {
                    this.wait();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            tmp = balance;
            System.out.println("(Withdrawing money): the initial balance is: " + tmp);
            tmp -= amount;
            balance = tmp;
            System.out.println("(Withdrawing money): the final balance is: " + balance);
            this.notify();
        }
    }

}

class Scrooge extends Thread {

    private Account account;

    Scrooge(Account account) {
        this.account = account;
    }

    public void run() {
        account.depositMoney(1200);
        try {
            sleep(6000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        account.depositMoney(1200);
    }
}

class Donald extends Thread {
    private Account account;
    Donald(Account account) {
        this.account = account;
    }

    public void run() {
        account.withdrawMoney(400);
        account.withdrawMoney(400);
    }
}

public class Bank {

    public static void main(String[] args) {
        Account account = new Account();
        Donald donald = new Donald(account);
        Donald donald2 = new Donald(account);
        Scrooge tiogilito = new Scrooge(account);
        donald.start();
        donald2.start();
        tiogilito.start();
    }
}
