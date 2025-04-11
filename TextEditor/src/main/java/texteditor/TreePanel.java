package texteditor;

import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {
    private WordNode root; // Raíz del árbol binario de búsqueda a dibujar

    // Constructor: configura el panel gráfico
    public TreePanel() {
        setPreferredSize(new Dimension(400, 300)); // Define el tamaño del panel (400x300 píxeles)
        setBackground(Color.WHITE);                // Establece el fondo blanco
    }

    // Establece la raíz del árbol y actualiza la visualización
    public void setRoot(WordNode root) {
        this.root = root;       // Asigna la nueva raíz
        repaint();              // Redibuja el panel con el nuevo árbol
    }

    // Método que dibuja el árbol en el panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Limpia el panel antes de dibujar
        if (root != null) {      // Si hay un árbol, lo dibuja
            drawTree(g, root, getWidth() / 2, 30, getWidth() / 4, 0); // Comienza desde el centro superior
        }
    }

    // Método recursivo para dibujar el árbol
    private void drawTree(Graphics g, WordNode node, int x, int y, int xOffset, int level) {
        if (node == null) return; // Si el nodo es nulo, termina la recursión

        // Dibuja el nodo como un círculo con la palabra dentro
        g.setColor(Color.BLUE);           // Color del círculo
        g.fillOval(x - 20, y - 20, 40, 40); // Dibuja un círculo de 40x40 píxeles
        g.setColor(Color.WHITE);          // Color del texto
        g.drawString(node.word, x - 10, y + 5); // Escribe la palabra en el centro del círculo

        // Dibuja líneas a los hijos
        g.setColor(Color.BLACK);          // Color de las líneas
        int yNext = y + 60;               // Distancia vertical al siguiente nivel
        if (node.left != null) {          // Si hay un hijo izquierdo
            int xLeft = x - xOffset;      // Posición horizontal del hijo izquierdo
            g.drawLine(x, y, xLeft, yNext); // Dibuja la línea al hijo izquierdo
            drawTree(g, node.left, xLeft, yNext, xOffset / 2, level + 1); // Recursión para el subárbol izquierdo
        }
        if (node.right != null) {         // Si hay un hijo derecho
            int xRight = x + xOffset;     // Posición horizontal del hijo derecho
            g.drawLine(x, y, xRight, yNext); // Dibuja la línea al hijo derecho
            drawTree(g, node.right, xRight, yNext, xOffset / 2, level + 1); // Recursión para el subárbol derecho
        }
    }
}