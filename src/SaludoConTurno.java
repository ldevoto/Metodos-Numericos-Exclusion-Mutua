
/**
 * En este ejercicio se utiliza el metodo de turnos para la exclusion mutua.
 * Este tipo de metodo cumple con:
 * 	-la condicion de exclusion propiamente dicha. Es decir, que solo un thread pueda entrar en la seccion critica a la vez
 * 	-que no haya inanicion (De hecho si o si deben ejecutar en orden uno despues del otro por lo que eso garantiza que todos los threads sean atendidos en algun momento)
 * 	-que no haya deadlock (no lo hay simplemente)
 * Pero no cumple con:
 * 	-la contencion al terminar. Cuando uno de los threads termina de ejecutar, el otro quedara a la espera de la habilitacion
 * que este debia hacerle y quedara esperando por siempre. De hecho esta situacion puede mostrarse si se reduce la cantidad de 
 * iteraciones de uno de los threads(ej ITERACIONES1), el otro quedara esperando por siempre y el programa nunca terminara. 
 * @author Ldevoto
 *
 */
public class SaludoConTurno extends Thread{
	static volatile int turno = 1;
	final int ITERACIONES1 = 100;
	final int ITERACIONES2 = 100;
	final int DEMORA1 = 9999;
	final int DEMORA2 = 9999;
	int miTurno; //Se le asigna el turno por el que preguntar a cada thread para que ejecuten codigo distinto
	int otroTurno;
	
	SaludoConTurno(int miTurno){
		this.miTurno = miTurno;
		this.otroTurno = 3 - this.miTurno;
	}
	
	public void run() {
		System.out.println("Comienza ejecucion del thread " + miTurno);
		int iteraciones = miTurno == 1? ITERACIONES1 : ITERACIONES2;
		int demora = miTurno == 1? DEMORA1 : DEMORA2;
		//Repetira el saludo una cantidad ITERACIONES de veces
		for (int i = 0; i < iteraciones; i++) {
			System.out.println("Iteracion " + i + " del thread " + miTurno + ". Esperando su turno..");
			//Seccion No Critica. Hacemos que demore un poco
			for(int j = 0; j < demora; j++) {}
		
			//Espera la habilitacion para acceder a la seccion critica
			while (turno != miTurno) {}
			
			//Accede a la Seccion Critica
			System.out.println("Ingresa el thread " + miTurno + " a la seccion critica");
			seccionCritica();
			System.out.println("Sale el thread " + miTurno + " de la seccion critica\n");
			//Cambia el turno
			System.out.println("Thread " + miTurno + " pasando el turno al thread " + otroTurno + "!");
			turno = otroTurno;
			//Fin de Seccion Critica
		}
	}
	
	private void seccionCritica() {
		System.out.println("Hola Mundo desde thread " + miTurno + " Seccion Critica");
	}
	
	public static void main(String[] args) {
		SaludoConTurno saludo1 = new SaludoConTurno(1);
		SaludoConTurno saludo2 = new SaludoConTurno(2);
		System.out.println("Comienzo del Programa\n---------------------");
		saludo1.start();
		saludo2.start();
		try {
			saludo1.join();
			saludo2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Fin del Programa\n----------------");
	}
}
