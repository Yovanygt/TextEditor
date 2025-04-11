package texteditor;

public class WordNode {
    String word;           // La palabra almacenada en este nodo
    int position;          // Posición inicial de la palabra en el texto
    WordNode left;         // Hijo izquierdo en el árbol binario de búsqueda (menor alfabéticamente)
    WordNode right;        // Hijo derecho en el árbol binario de búsqueda (mayor alfabéticamente)

    // Constructor: inicializa un nodo con una palabra y su posición
    public WordNode(String word, int position) {
        this.word = word;         // Asigna la palabra
        this.position = position; // Asigna la posición
        this.left = null;         // Inicializa el hijo izquierdo como nulo
        this.right = null;        // Inicializa el hijo derecho como nulo
    }
}