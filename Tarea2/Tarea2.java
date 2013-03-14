import java.io.*; 
import java.math.*;

public class Tarea2 {
	public static void main(String[] args) {
		Localidad mty = new Localidad("Monterrey", "Mx/Nl", new Coord(0.0, 1.1), true);
		Localidad sal = new Localidad("Saltillo", "Mx/Coah", new Coord(0.1, -1.1), true);
		mty.entrada[0] = new Via(0, 20, 0, sal, mty);
		mty.salida[0] = new Via(0, 20.1, 0, mty, sal);
		sal.salida[0] = new Via(0, 20, 0, sal, mty);
		sal.entrada[0] = new Via(0, 20.1, 0, mty, sal);
		System.out.println("fin.");
	}
}

// Coordenadas, en radianes
class Coord {
	public	double		lat; // latitud
	public	double		lon; // longitud
	public Coord(double a, double b) {
		mod(a, b);
	}
	// modificar: para actualizar valores del objeto
	public void mod(double a, double b) {
		this.lat = a;
		this.lon = b;
	}
}

// Arco dirigidos entre nodos, une dos localidades, entiéndase carreteras,
// siempre se va de a a b
class Via {
	public	double		costo; // costo, ==0 si es gratis
	public	double		dis; // distancia, en kilómetros.
	public	double		riesgo; // en alguna unidad arbitraria, ==0 si
					// no «hay riesgo», podría usarse el
					// número de robos por mes como
					// indicador...
	public	Localidad	orig; // localidad origen de esta vía
	public	Localidad	dest; // localidad destino de esta vía

	public Via(double a, double b, double c, Localidad orig, Localidad dest) {
		mod(a, b, c, orig, dest);
	}
	// modificar: para actualizar valores del objeto
	public void mod(double a, double b, double c, Localidad orig, Localidad dest) {
		this.costo = a;
		this.dis = b;
		this.riesgo = c;
		this.orig = orig;
		this.dest = dest;
	}
}


// localidad más genérica 
class Localidad {
	public	String		nombre	= null; // «Nombre común»
	public	String		ubi	= null; // dirección política, ubicación
	public	Coord 		c	= null; // coordenadas
	public	boolean		haygps	= false; // si hay cobertura GPS
	public	Via[]		salida; // Conjunto de vías de salida de esta
					  // localidad
	public	Via[]		entrada; // Conjunto de vías de entrada de esta
					  // localidad
	public Localidad(String a, String b, Coord c, boolean d) {
		this.nombre = a;
		this.ubi = b;
		this.c = c;
		this.haygps = d;
		this.salida = new Via[32];
		this.entrada = new Via[32];
	}

}

// una localidad con características adicionales
class Base extends Localidad {
	public	int		bodegas; // número de bodegas presente
	public	int		talleres; // ídem
	public	int		dormitorios; // ídem
	public Base(String a, String b, Coord c, boolean d, int e, int f, int g) {
		super(a, b, c, d);
		this.bodegas = e;
		this.talleres = f;
		this.dormitorios = g;
	}
}
