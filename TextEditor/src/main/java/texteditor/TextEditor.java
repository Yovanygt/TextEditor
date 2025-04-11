package texteditor;

import java.util.Stack;

public class TextEditor {
    private String currentText;           // Almacena el texto actual del editor
    private Stack<String> undoStack;      // Pila para guardar los estados anteriores (Deshacer)
    private Stack<String> redoStack;      // Pila para guardar los estados deshechos (Rehacer)
    private WordNode root;                // Raíz del árbol binario de búsqueda (ABB) para búsqueda rápida

    // Constructor: inicializa el editor con valores vacíos
    public TextEditor() {
        this.currentText = "";            // El texto empieza vacío
        this.undoStack = new Stack<>();   // Crea una pila vacía para deshacer
        this.redoStack = new Stack<>();   // Crea una pila vacía para rehacer
        this.root = null;                 // El ABB empieza vacío
    }

    // Actualiza el texto actual y reconstruye el ABB
    public void updateText(String newText) {
        if (!newText.equals(currentText)) { // Solo actúa si el texto ha cambiado
            undoStack.push(currentText);    // Guarda el estado anterior en la pila de deshacer
            redoStack.clear();              // Limpia la pila de rehacer al hacer un cambio nuevo
            currentText = newText;          // Actualiza el texto actual
            buildSearchTree();              // Reconstruye el ABB con el nuevo texto
        }
    }

    // Deshace el último cambio y actualiza el ABB
    public String undo() {
        if (!undoStack.isEmpty()) {         // Verifica si hay algo que deshacer
            redoStack.push(currentText);    // Guarda el texto actual en la pila de rehacer
            currentText = undoStack.pop();  // Restaura el texto anterior
            buildSearchTree();              // Reconstruye el ABB con el texto restaurado
            return currentText;             // Devuelve el texto restaurado
        }
        return currentText;                 // Si no hay nada que deshacer, devuelve el texto actual
    }

    // Rehace el último cambio deshecho y actualiza el ABB
    public String redo() {
        if (!redoStack.isEmpty()) {         // Verifica si hay algo que rehacer
            undoStack.push(currentText);    // Guarda el texto actual en la pila de deshacer
            currentText = redoStack.pop();  // Restaura el texto siguiente
            buildSearchTree();              // Reconstruye el ABB con el texto restaurado
            return currentText;             // Devuelve el texto restaurado
        }
        return currentText;                 // Si no hay nada que rehacer, devuelve el texto actual
    }

    // Devuelve el texto actual
    public String getCurrentText() {
        return currentText;                 // Retorna el texto actual del editor
    }

    // Construye el ABB a partir del texto actual
    private void buildSearchTree() {
        root = null;                        // Reinicia el árbol
        String[] words = currentText.split("\\s+"); // Divide el texto en palabras usando espacios
        int position = 0;                   // Posición inicial en el texto

        for (String word : words) {         // Recorre cada palabra
            if (!word.isEmpty()) {          // Ignora palabras vacías
                root = insertWord(root, word, position); // Inserta la palabra original
                position += word.length() + 1; // Actualiza la posición (longitud de la palabra + espacio)
            }
        }
    }

    // Inserta una palabra en el ABB de forma ordenada
    private WordNode insertWord(WordNode node, String word, int position) {
        if (node == null) {                 // Si el nodo es nulo, crea uno nuevo
            return new WordNode(word, position);
        }
        int comparison = word.toLowerCase().compareTo(node.word.toLowerCase()); // Compara en minúsculas
        if (comparison < 0) {               // Si la palabra es menor, va al subárbol izquierdo
            node.left = insertWord(node.left, word, position);
        } else if (comparison > 0) {        // Si es mayor, va al subárbol derecho
            node.right = insertWord(node.right, word, position);
        }
        return node;                        // Si ya existe, no duplica y retorna el nodo actual
    }

    // Busca una palabra en el ABB y devuelve su posición
    public int searchWord(String word) {
        WordNode current = root;            // Comienza desde la raíz
        while (current != null) {           // Recorre el árbol
            int comparison = word.toLowerCase().compareTo(current.word.toLowerCase()); // Compara ambas en minúsculas
            if (comparison == 0) {          // Si coincide, devuelve la posición
                return current.position;
            } else if (comparison < 0) {    // Si es menor, busca en el subárbol izquierdo
                current = current.left;
            } else {                        // Si es mayor, busca en el subárbol derecho
                current = current.right;
            }
        }
        return -1;                          // Devuelve -1 si no se encuentra la palabra
    }

    // Devuelve la raíz del ABB para dibujarla
    public WordNode getRoot() {
        return root;                        // Retorna la raíz del árbol para visualización
    }
}