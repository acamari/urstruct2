// Ejecuta comandos desde la entrada estándar (consola), actualmente reconoce
// tres tipos de comandos, además este programa siempre imprime el estado final
// de la lista ordenada:
//
// Comandos:
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
//
//
// Modificar un elemento de la lista ordenada, esto se hace precediendo el dato
// a modificar con una tilde (~), y escribiendo el dato a reemplazar precedido
// por otra ~:
//	$ java Tarea1 
//	c
//	a
//	b
//	~a~z
//	^D
//	b
//	c
//	z
//	$
import java.io.*; 
import java.util.regex.*;

public class Tarea1 {
	public static void main(String[] args) {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String linea = null;
		ListaDoble l = new ListaDoble();
		for (;;) {
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
				if (l.remueve(new Nodo(linea)) <= 0) {
					System.err.println("no se encontro: " + linea);
				}
			// modificar
			} else if (linea.matches("^~.*$")) {
				Pattern p = Pattern.compile("^~([^~]*)~(.*)$");
				Matcher	m = p.matcher(linea);
				String viejo, nuevo;
				if (!m.matches()) {
					System.err.println("comando invalido: " + linea);
				}
				viejo = m.group(1);
				nuevo = m.group(2);
				if (l.mod(new Nodo(viejo), new Nodo(nuevo)) <= 0) {
					System.err.println("error en comando: " + linea);
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
	private Nodo		prev	= null;
	private Nodo		sig	= null;
	
	public Nodo(Nodo arg) { //duplicador
		this.dato = arg.dato;
		this.prev = arg.prev;
		this.sig = arg.sig;
	}

	public Nodo(String arg) {
		this.dato = arg;
	}
	public Nodo obtenprev() {
		return this.prev;
	}
	public Nodo obtensig() {
		return this.sig;
	}
	public Nodo modprev(Nodo arg) {
		return this.prev = arg;
	}
	public Nodo modsig(Nodo arg) {
		return this.sig = arg;
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
		for (p = pri; p != null; p = p.obtensig()) {
			// actual es lógicamente mayor al nodo a insertar
			// entonces, inserta antes de él
			if (p.dato.compareTo(n.dato) > 0) {
				//System.err.println(n.dato + " < " + p.dato);
				// no es el primer elemento
				if (p.obtenprev() != null) p.obtenprev().modsig(n);
				n.modprev(p.obtenprev());
				p.modprev(n);
				n.modsig(p);
				if (p == pri) pri = n; // actual era el primero
				return true;

			// actual es el último nodo, inserta después de él
			} else if (p.obtensig() == null) {
				//System.err.println("last: p: " + p + " dato: " + p.dato + " prev: " + p.obtenprev() + " sig: " + p.obtensig());
				//System.err.println("last: n: " + n + " dato: " + n.dato + " prev: " + n.obtenprev() + " sig: " + n.obtensig());
				p.modsig(n);
				n.modprev(p);
				n.modsig(null);
				return true;
			}
		}
		return false;
	}
	// remueve de la lista todos los nodos cuyo dato sea lógicamente igual a
	// n.dato, regresa número de elementos eliminados
	public int remueve(Nodo n) { 
		int el = 0; // eliminados
		for (Nodo p = pri; p != null;) {
			if (!p.dato.equals(n.dato)) {
				p = p.obtensig();
				continue;
			}
			if (p.obtenprev() == null && p.obtensig() == null) { // único elem
				pri = p = null;
			} else if (p.obtenprev() == null) { // primer elem
				p.obtensig().modprev(null);
				pri = p.obtensig();
				p.modsig(null);
				p = pri;
			} else if (p.obtensig() == null) { // último elem
				p.obtenprev().modsig(null);
				p.modprev(null);
				p = null;
			} else { // cualquier elemento
				Nodo tmp = p.obtensig();
				p.obtenprev().modsig(p.obtensig());
				p.obtensig().modprev(p.obtenprev());
				p.modprev(null);
				p.modsig(null);
				p = tmp;
			}
			++el;
		}
		return el;
	}
	// modifica nodos con dato igual a v.data modificándolos para que su
	// dato sea igual a n.data
	public int mod(Nodo v, Nodo n) {
		int m = 0;
		if (v == null || n == null) return 0;
		if ((m = remueve(v)) <= 0) return 0;
		for (int i = 0; i < m; i++) inserta(new Nodo(n));
		return m;
	}

	public void imprime() {
		imprime(0);
	}
	// imprime lista
	public void imprime(int debug) {
		for (Nodo p = pri; p != null; p = p.obtensig()) {
			if (debug > 0) {
				System.err.println(p + " dato: " + p.dato + ": prev: " +
				    p.obtenprev() + " sig: " + p.obtensig());
			} else {
				System.out.println(p.dato);
			}
		}
	}
}
