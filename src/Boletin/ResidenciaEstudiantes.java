package Boletin;

class Habitacion{
	private int estudiantes;
	private boolean director=false;


	synchronized void inEstudiante(){
		while(director){
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
		}
		estudiantes++;
		System.out.println("Hay "+estudiantes +" estudiantes");
		notifyAll();
	}
	synchronized void outEstudiante(){
		estudiantes--;
		System.out.println("Hay "+estudiantes +" estudiantes");
		notifyAll();
	}
	synchronized void inDirector(){
		while(estudiantes <15){
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
		}
		director=true;
		System.out.println("Director ha entrado");
	}
	synchronized void outDirector(){
		while(estudiantes!=0){
			try {
				this.wait();
			} catch (InterruptedException ignored) {
			}
			director=false;
			System.out.println("Director ha salido");
			notifyAll();
		}
	}
}

class Estudiante extends Thread{
	Habitacion habitacion;
	private static int n;
	public Estudiante(Habitacion habitacion){
		this.habitacion=habitacion;
	}

	public void run(){
		habitacion.inEstudiante();
		n++;
		System.out.println("E"+n+ ": he entrado");
		try {
			Thread.sleep(50000);
		} catch (InterruptedException ignored) {
		}
		habitacion.outEstudiante();
		System.out.println("E"+n+ ": he salido");
	}
}

class Director extends Thread{
	Habitacion habitacion;
	public Director(Habitacion habitacion){
		this.habitacion=habitacion;
	}

	public void run(){
		System.out.println("D: intento entrar");
		habitacion.inDirector();
		System.out.println("D: he entrado");
		habitacion.outDirector();
		System.out.println("D: he salido");
	}
}

public class ResidenciaEstudiantes {
	public static void main(String[] args) {
		Habitacion habitacion= new Habitacion();
		Estudiante estudiante;
		Director director;

		for (int i = 0; i < 18; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignored) {
			}
			if(i==5){
				director=new Director(habitacion);
				director.start();
			}
			estudiante=new Estudiante(habitacion);
			estudiante.start();
		}
	}
}
