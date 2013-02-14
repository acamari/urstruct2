// Ejecuta comandos desde la entrada estándar (consola), actualmente reconoce
// tres tipos de comandos, además este programa siempre imprime el estado final
// de la lista ordenada:
//
// Comandos:
//
// Insertar en la lista ordenada, para insertar, escríbase cualquier dato que
// no esté precedido por un guión (-) y presiónese enter, ejemplo:
//	$ java Ex1 
//	Benjamin,5678,Calle dos,b@mail.org
//	Alberto,1234,Calle sin nombre,ma@il.org
//	^D
//	Alberto,1234,Calle sin nombre,ma@il.org
//	Benjamin,5678,Calle dos,b@mail.org
//	$
//
// Remover de la lista ordenada, escríbase cualquier dato precedido de un
// guión, y será removido de la lista ordenada:
//	$ java Ex1 
//	Carlos,9999,Calle de carlos,c@mail.org
//	Alberto,1234,Calle sin nombre,ma@il.org
//	Benjamin,5678,Calle dos,b@mail.org
//	-Alberto
//	^D
//	Benjamin,5678,Calle dos,b@mail.org
//	Carlos,9999,Calle de carlos,c@mail.org
//	$
//
//
// Modificar un elemento de la lista ordenada, esto se hace precediendo el dato
// a modificar con una tilde (~), y escribiendo el dato a reemplazar precedido
// por otra ~:
//	$ java Ex1 
//	Carlos,9999,Calle de carlos,c@mail.org
//	Alberto,1234,Calle sin nombre,ma@il.org
//	Benjamin,5678,Calle dos,b@mail.org
//	~Alberto~Xavier
//	^D
//	Benjamin,5678,Calle dos,b@mail.org
//	Carlos,9999,Calle de carlos,c@mail.org
//	Xavier,1234,Calle sin nombre,ma@il.org
//	$
import java.io.*; 
import java.util.regex.*;

public class Ex1 {
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
				int r; // removidos
				linea = linea.replaceFirst("-", "");
				for (r = 0; l.remueve(new Nodo(linea)) != null; r ++);
				if (r <= 0) {
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
			} else {
				Pattern p = Pattern.compile("^([^,]*),([^,]*),([^,]*),([^,]*)$");
				Matcher	m = p.matcher(linea);
				String nom, tel, dir, mail;

				if (!m.matches()) {
					System.err.println("comando invalido: " + linea);
				}
				nom = m.group(1);
				tel = m.group(2);
				dir = m.group(3);
				mail = m.group(4);
				if (l.inserta(new Nodo(nom, tel, dir, mail)) != true) {
					System.err.println("error: insertando " + linea);
				}
			}
		}
		l.imprime();
	}

}

class Nodo {
	public String		nom	= null;
	public String		tel	= null;
	public String		dir	= null;
	public String		mail	= null;

	private Nodo		prev	= null;
	private Nodo		sig	= null;
	
	public Nodo(Nodo arg) { //duplicador
		this.nom = arg.nom;
		this.tel = arg.tel;
		this.dir = arg.dir;
		this.mail = arg.mail;

		this.prev = arg.prev;
		this.sig = arg.sig;
	}
	public Nodo(String a) { // útil en búsquedas
		this.nom = a;
	}
	public Nodo(String a, String b, String c, String d) {
		this.nom = a;
		this.tel = b;
		this.dir = c;
		this.mail = d;
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
	//private Nodo	ult	= null; // último

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
			if (p.nom.compareTo(n.nom) > 0) {
				//System.err.println(n.nom + " < " + p.nom);
				// no es el primer elemento
				if (p.obtenprev() != null) p.obtenprev().modsig(n);
				n.modprev(p.obtenprev());
				p.modprev(n);
				n.modsig(p);
				if (p == pri) pri = n; // actual era el primero
				return true;

			// actual es el último nodo, inserta después de él
			} else if (p.obtensig() == null) {
				//System.err.println("last: p: " + p + " nom: " + p.nom + " prev: " + p.obtenprev() + " sig: " + p.obtensig());
				//System.err.println("last: n: " + n + " nom: " + n.nom + " prev: " + n.obtenprev() + " sig: " + n.obtensig());
				p.modsig(n);
				n.modprev(p);
				n.modsig(null);
				return true;
			}
		}
		return false;
	}
	// remueve de la lista el primer nodo cuyo nom sea lógicamente igual a
	// n.nom, regresa el objeto borrado, null en caso de no encontrar alguno
	public Nodo remueve(Nodo n) { 
		Nodo el;
		for (Nodo p = pri; p != null;) {
			if (!p.nom.equals(n.nom)) {
				p = p.obtensig();
				continue;
			}
			if (p.obtenprev() == null && p.obtensig() == null) { // único elem
				el = p;
				pri = p = null;
			} else if (p.obtenprev() == null) { // primer elem
				el = p;
				p.obtensig().modprev(null);
				pri = p.obtensig();
				p.modsig(null);
				p = pri;
			} else if (p.obtensig() == null) { // último elem
				el = p;
				p.obtenprev().modsig(null);
				p.modprev(null);
				p = null;
			} else { // cualquier elemento
				el = p;
				Nodo tmp = p.obtensig();
				p.obtenprev().modsig(p.obtensig());
				p.obtensig().modprev(p.obtenprev());
				p.modprev(null);
				p.modsig(null);
				p = tmp;
			}
			return el;
		}
		return null;
	}
	// modifica nodos con nom igual a v.data modificándolos para que su
	// nom sea igual a n.data
	public int mod(Nodo v, Nodo n) {
		int m;
		Nodo r;
		if (v == null || n == null) return 0;
		for (m = 0; (r = remueve(v)) != null; m++) {
			r.nom = n.nom;
			inserta(new Nodo(r));
		}

		return m;
	}

	public void imprime() {
		imprime(0);
	}
	// imprime lista
	public void imprime(int debug) {
		for (Nodo p = pri; p != null; p = p.obtensig()) {
			if (debug > 0) {
				System.err.println(p + " nom: " + p.nom + ": prev: " +
				    p.obtenprev() + " sig: " + p.obtensig());
			} else {
				System.out.println(p.nom + " tel: <" + p.tel + "> dir: <" + p.dir + "> mail: <" + p.mail + ">");
			}
		}
	}
}
