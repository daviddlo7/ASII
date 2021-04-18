package Monitores;

import java.lang.*;
import java.util.*;

class Account {

	private float balance = 0;

	public synchronized void depositMoney(float amount) {
		float tmp;
		tmp = balance;
		System.out.println("(Adding money): the initial balance is: "
				+ tmp);
		tmp += amount;
		balance = tmp;
		System.out.println("(Adding money): the final balance is: "
				+ balance);
		this.notify();
		notify();
	}

	public synchronized void withdrawMoney(float amount) {
		float tmp;
		while (balance <= 0) {
			try {
				this.wait();
			} catch (Exception e) {
			}
		}

		tmp = balance;
		System.out.println("(Withdrawing money): the initial balance is: "
				+ tmp);
		tmp -= amount;
		balance = tmp;
		System.out.println("(Withdrawing money): the final balance is: "
				+ balance);
	}
}


class Scrooge extends Thread {

	private Account account;
	Scrooge(Account account) {
		this.account = account;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			account.depositMoney(300);
		}
	}
}

class Donald extends Thread {

	private Account account;

	Donald(Account account) {
		this.account = account;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			account.withdrawMoney(100);
		}

	}
}

public class BankMonitor {

	public static void main(String[] args) {
		Account account = new Account();
		Account account2 = new Account();

		Donald donald1 = new Donald(account);
		Donald donald2 = new Donald(account);
		Donald donald3 = new Donald(account);
		Scrooge tiogilito = new Scrooge(account);

		Donald donald4 = new Donald(account2);
		Donald donald5 = new Donald(account2);
		Donald donald6 = new Donald(account2);
		Scrooge tiogilito2 = new Scrooge(account2);
		//donald1.start();
		//donald2.start();
		//donald3.start();
		tiogilito.start();

		//donald4.start();
		//donald5.start();
		//donald6.start();
		tiogilito2.start();

	}
}
