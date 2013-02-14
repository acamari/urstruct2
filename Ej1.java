public class Ej1 {
	public static void main (String args[]) {
		Node <Integer>		i;
		i = new Node <Integer> (999);
		System.out.println("i: " + i);

	}
}

class Node <argtype> {
	public argtype		tmp;
	public Node		arr[];
	public Node(int n) {
		arr = new Node [n];
	}
}
