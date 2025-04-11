package texteditor;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditorWindow().setVisible(true); // Inicia la ventana del editor
            }
        });
    }
}
