import java.util.HashMap;
import java.util.Map;

/**
 * En este ejercicio se utiliza el metodo de Dekker para la exclusion mutua.
 * Este tipo de metodo cumple con:
 * 	-la condicion de exclusion propiamente dicha
 * 	-que no haya inanicion. Este item es polemico ya que dependera de la forma de administrar los procesos del SO y de como se sincronicen o no 
 * el acceso a las secciones criticcas. En el infinito casi podriamos asegurar que por lo menos cada thread ingresara una vez a su seccion critica
 * 	-que no haya deadlock. En ningun caso habra deadlock. Esta pensado para que ninguno de los dos threads necesiten recursos cruzados
 * 	-la contencion al terminar. Cuando uno de los threads termina, el otro puede seguir ejecutando sin problema. Esto puede verse
 * si se modifica la variable de ITERACIONES1 o 2 para que no sean igual. El programa va a terminar sin problemas.
 * 
 * Un comentario agregado es que podemos simular la necesidad de ingresar mas veces de un thread que el otro incrementando el valor de DEMORA1 o 2.
 * Con esto hacemos que el thread espere mas y el otro ingrese mas cantidad de veces a la seccion critica dejando evidenciar que no es necesario tener
 * un orden de ejecucion o entrada. 
 * @author Ldevoto
 *
 */
public class SaludoConDekker extends Thread{
	static volatile int turno = 1;
	static volatile Map<Integer, Boolean> want = new HashMap<>();
	final int ITERACIONES1 = 10;
	final int ITERACIONES2 = 10;
	final int DEMORA1 = 9999;
	final int DEMORA2 = 9999;
	int miTurno; //Se le asigna el turno por el que preguntar a cada thread para que ejecuten codigo distinto
	int otroTurno;
	
	SaludoConDekker(int miTurno){
		this.miTurno = miTurno;
		this.otroTurno = 3 - this.miTurno;
		want.put(1, false);
		want.put(2, false);
	}
	
	public void run() {
		System.out.println("Comienza ejecucion del thread " + miTurno);
		int iteraciones = miTurno == 1? ITERACIONES1 : ITERACIONES2;
		int demora = miTurno == 1? DEMORA1 : DEMORA2;
		//Repetira el saludo una cantidad x de veces
		for (int i = 0; i < iteraciones; i++) {
			System.out.println("Iteracion " + i + " del thread " + miTurno);
			//Seccion No Critica. Hacemos que demore un poco
			for(int j = 0; j < demora; j++) {}
		
			//Espera la habilitacion para acceder a la seccion critica. Algoritmo auspiciado por Dekker!
			want.put(miTurno, true);
			while (want.get(otroTurno)) {
				if (turno == otroTurno) {
					want.put(miTurno, false);
					while(turno != miTurno){};
					want.put(miTurno, true);
				}
			}
			
			//Accede a la Seccion Critica
			System.out.println("Ingresa el thread " + miTurno + " a la seccion critica");
			seccionCritica();
			System.out.println("Sale el thread " + miTurno + " de la seccion critica\n");
			//Fin de Seccion Critica
			//Cambia el turno
			System.out.println("Thread " + miTurno + " habilitando el turno al thread " + otroTurno + " si es que lo necesita!");
			turno = otroTurno;
			want.put(miTurno, false);
		}
	}
	
	private void seccionCritica() {
		System.out.println("Hola Mundo desde thread " + miTurno + " Seccion Critica");
	}
	
	public static void main(String[] args) {
		SaludoConDekker saludo1 = new SaludoConDekker(1);
		SaludoConDekker saludo2 = new SaludoConDekker(2);
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
