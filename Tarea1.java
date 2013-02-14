import java.io.*; // para leer de la entrada estandar


public class Tarea1 {
	public static void main(String[] args) {
		// lee línea por línea y guarda una lista usando cada línea
		// como el dato de un nodo
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
			if (l.inserta(new Nodo(linea)) != true) {
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
				//System.out.println(n.dato + " < " + p.dato);
				// no es el primer elemento
				if (p.prev != null) p.prev.sig = n;
				n.prev = p.prev;
				p.prev = n;
				n.sig = p;
				if (p == pri) pri = n; // actual era el primero
				return true;

			// actual es el último nodo, inserta después de él
			} else if (p.sig == null) {
				//System.out.println("last: " + n.dato + " < " + p.dato);
				p.sig = n;
				n.prev = p;
				n.sig = null;
				return true;
			}
		}
		return false;
	}
	// remueve nodo de la lista...
	public void rm(Nodo n) { 
	}
	// imprime lista
	public void imprime() {
		for (Nodo p = pri; p != null; p = p.sig) {
			System.out.println(p.dato);
		}
	}
}
