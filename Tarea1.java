// Ejecuta comandos desde la entrada estándar (consola), actualmente reconoce
// dos tipos de comandos, este programa siempre imprime el estado final de la
// lista ordenada
//
// Insertar en la lista ordenada, para insertar, escríbase cualquier dato que
// no esté precedido por un guión (-) y presiónese enter, ejemplo:
//	$ java Tarea1 
//	mundo
//	hola
//	^D
//	hola
//	mundo
//	$
//
// Remover de la lista ordenada, escríbase cualquier dato precedido de un
// guión, y será removido de la lista ordenada:
//	$ java Tarea1 
//	c
//	a
//	b
//	-a
//	^D
//	b
//	c
//	$
import java.io.*; 


public class Tarea1 {
	public static void main(String[] args) {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String linea = null;
		ListaDoble l = new ListaDoble();
		while (true) {
			try { 
				linea = stdin.readLine(); 
			} catch (IOException e) {
				System.err.println("error: " + e);
			}
			if (linea == null) break; // fin de lectura
			linea = linea.replace("\n", ""); // borra saltos de línea

			// borrar
			if (linea.matches("^-.*$")) {
				linea = linea.replaceFirst("-", "");
				if (l.rm(new Nodo(linea)) <= 0) {
					System.err.println("no se encontro: " + linea);
				}
			// insertar
			} else if (l.inserta(new Nodo(linea)) != true) {
				System.err.println("error: insertando " + linea);
			}

		}
		l.imprime();
	}

}

class Nodo {
	public String		dato	= null;
	public Nodo		prev	= null;
	public Nodo		sig	= null;

	public Nodo(String arg) {
		this.dato = arg;
		return;
	}
}

class ListaDoble {
	private Nodo	pri	= null; // primero
	//private Nodo	act	= null; // actual
	private Nodo	ult	= null; // último

	public ListaDoble() { // sin arg
		return;
	}
	// inserta nodo en la lista doblemente enlazada, manteniendo orden
	public boolean inserta(Nodo n) { 
		Nodo p;

		if (pri == null) { // lista vacía
			pri = n;
			return true;
		}
		for (p = pri; p != null; p = p.sig) {
			// actual es lógicamente mayor al nodo a insertar
			// entonces, inserta antes de él
			if (p.dato.compareTo(n.dato) > 0) {
				//System.err.println(n.dato + " < " + p.dato);
				// no es el primer elemento
				if (p.prev != null) p.prev.sig = n;
				n.prev = p.prev;
				p.prev = n;
				n.sig = p;
				if (p == pri) pri = n; // actual era el primero
				return true;

			// actual es el último nodo, inserta después de él
			} else if (p.sig == null) {
				//System.err.println("last: " + n.dato + " < " + p.dato);
				p.sig = n;
				n.prev = p;
				n.sig = null;
				return true;
			}
		}
		return false;
	}
	// remueve de la lista todos los nodos cuyo dato sea lógicamente igual a
	// n.dato, regresa número de elementos eliminados
	public int rm(Nodo n) { 
		int el = 0; // eliminados
		//System.err.println("borrando: " + n.dato);
		for (Nodo p = pri; p != null; p = p.sig) {
			if (!p.dato.equals(n.dato)) continue;
			if (p.prev == null && p.sig == null) { // único elem
				pri = null;
			} else if (p.prev == null) { // primer elem
				p.sig.prev = null;
				pri = p.sig;
				p.sig = null;
			}
			++el;
		}
		return el;
	}
	// imprime lista
	public void imprime() {
		for (Nodo p = pri; p != null; p = p.sig) {
			System.out.println(p.dato);
		}
	}
}
