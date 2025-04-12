# PROYECTO II

![enter image description here](https://i.postimg.cc/CMQfySYd/image.png)

# EDITOR DE TEXTO

El presente proyecto consiste en un editor de texto avanzado desarrollado en Java, utilizando Swing para la construcción de su interfaz gráfica. Está enfocado en la implementación de estructuras de datos como pilas y árboles binarios de búsqueda (ABB). Su propósito principal es ofrecer una herramienta práctica, funcional y fácil de usar, que permita no solo la edición de texto, sino también realizar acciones como deshacer y rehacer cambios, buscar palabras de manera eficiente y representar gráficamente la estructura del contenido mediante un árbol binario.




## **Funciones Principales**

### 1. **Edición de Texto**

-   Área de texto donde el usuario puede escribir, borrar o modificar libremente.
    
-   Guarda cada cambio para permitir su seguimiento (Deshacer/Rehacer).
    

### 2. **Deshacer y Rehacer**

-   Utiliza **pilas (Stacks)** para almacenar los estados anteriores y siguientes del texto.
    
-   Permite al usuario regresar a versiones anteriores del texto o rehacer cambios que había deshecho.
    

### 3. **Búsqueda Rápida de Palabras**

-   Al actualizar el texto, el editor construye automáticamente un **Árbol Binario de Búsqueda (ABB)** con cada palabra.
    
-   La búsqueda se realiza de forma eficiente, devolviendo la posición donde aparece la palabra en el texto.
    

### 4. **Visualización del Árbol Binario**

-   Un panel gráfico muestra el árbol binario que representa la estructura alfabética del texto.
    
-   Cada palabra se coloca en el ABB según su orden, y se dibujan visualmente los nodos con conexiones entre padres e hijos.
    

### 5. **Guardado Automático**

-   El texto se guarda automáticamente en una carpeta específica (`MisTextos` en el Escritorio), evitando la pérdida de información.
    

### 6. **Interfaz Gráfica (Swing)**

-   Interfaz amigable con botones para cada función (Deshacer, Rehacer, Buscar, Reemplazar, Ver árbol, etc.).
    
-   Se puede ampliar con funciones adicionales como cambio de estilo de texto (fuente, color, tamaño).

## Estructura del Proyecto

El proyecto está compuesto por varias clases, cada una con una responsabilidad clara:

-   `Main`: inicia la aplicación.
    
-   `EditorWindow`: contiene la interfaz gráfica y gestiona eventos.
    
-   `TextEditor`: lógica del editor, gestiona texto, pilas y árbol.
    
-   `WordNode`: modelo de cada nodo del árbol.
    
-   `TreePanel`: componente gráfico que dibuja el árbol binario.

# CODIGO:

## EditorWindow.java
    
    package texteditor;
    
    // Importaciones necesarias para la interfaz gráfica, manejo de documentos, eventos y archivos
    import javax.swing.*;
    import javax.swing.event.DocumentEvent;
    import javax.swing.event.DocumentListener;
    import javax.swing.text.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.io.*;
    import java.nio.file.*;
    import java.util.regex.*;
    
    // Clase principal que representa la ventana del editor de texto, hereda de JFrame
    public class EditorWindow extends javax.swing.JFrame {
        // Instancia del editor de texto lógico que maneja el texto y el árbol binario de búsqueda
        private TextEditor editor;
        // Temporizador para retrasar la actualización del texto y el árbol tras cambios
        private Timer timer;
        // Panel para visualizar el árbol binario de búsqueda
        private TreePanel treePanel;
        // Archivo actual que se está editando (puede ser null si no se ha guardado/abierto)
        private File currentFile = null;
    
        // Constructor de la ventana del editor
        public EditorWindow() {
            // Inicializa los componentes de la interfaz gráfica
            initComponents();
            // Crea una nueva instancia del editor lógico
            editor = new TextEditor();
            // Establece el texto inicial del JTextPane desde el editor
            jTextPane1.setText(editor.getCurrentText());
    
            // Configura un temporizador que se ejecuta después de 500ms para actualizar el texto y el árbol
            timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Actualiza el texto en el editor lógico con el contenido del JTextPane
                    editor.updateText(jTextPane1.getText());
                    // Actualiza el árbol binario de búsqueda en el panel gráfico
                    treePanel.setRoot(editor.getRoot());
                    // Detiene el temporizador tras ejecutarse
                    timer.stop();
                }
            });
            // El temporizador no se repite automáticamente
            timer.setRepeats(false);
    
            // Añade un listener al documento del JTextPane para detectar cambios en el texto
            jTextPane1.getDocument().addDocumentListener(new DocumentListener() {
                // Se ejecuta cuando se inserta texto
                public void insertUpdate(DocumentEvent e) { timer.restart(); }
                // Se ejecuta cuando se elimina texto
                public void removeUpdate(DocumentEvent e) { timer.restart(); }
                // Se ejecuta cuando se cambian atributos del texto (no usado en este caso)
                public void changedUpdate(DocumentEvent e) { timer.restart(); }
            });
    
            // Configura los atajos de teclado (Ctrl+Z y Ctrl+Y)
            setupKeyBindings();
        }
    
        // Método para configurar los atajos de teclado para deshacer y rehacer
        private void setupKeyBindings() {
            // Asocia Ctrl+Z con la acción "undo"
            jTextPane1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
            jTextPane1.getActionMap().put("undo", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // Aplica la operación de deshacer y actualiza el JTextPane
                    jTextPane1.setText(editor.undo());
                    // Actualiza el árbol binario de búsqueda en el panel gráfico
                    treePanel.setRoot(editor.getRoot());
                }
            });
    
            // Asocia Ctrl+Y con la acción "redo"
            jTextPane1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
            jTextPane1.getActionMap().put("redo", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // Aplica la operación de rehacer y actualiza el JTextPane
                    jTextPane1.setText(editor.redo());
                    // Actualiza el árbol binario de búsqueda en el panel gráfico
                    treePanel.setRoot(editor.getRoot());
                }
            });
        }
    
        // Método para inicializar los componentes de la interfaz gráfica
        private void initComponents() {
            // Crea un JScrollPane para el área de texto
            jScrollPane1 = new JScrollPane();
            // Crea un JTextPane para editar texto con soporte de estilos
            jTextPane1 = new JTextPane();
            // Botón para deshacer cambios
            jButton1 = new JButton("Deshacer");
            // Botón para rehacer cambios
            jButton2 = new JButton("Rehacer");
            // Botón para buscar palabras
            jButton3 = new JButton("Buscar");
            // Botón para guardar el archivo
            jButton4 = new JButton("Guardar");
            // Botón para abrir un archivo
            jButton5 = new JButton("Abrir");
            // Botón para reemplazar palabras
            jButton6 = new JButton("Reemplazar");
            // Botón para cambiar el estilo del texto seleccionado
            jButton7 = new JButton("Cambiar Estilo");
            // Campo de texto para ingresar la palabra a buscar
            jTextField1 = new JTextField(10);
            // Campo de texto para ingresar la palabra de reemplazo
            jTextField2 = new JTextField(10);
            // Panel para mostrar el árbol binario de búsqueda
            treePanel = new TreePanel();
    
            // Configura el cierre de la ventana para terminar la aplicación
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            // Establece el título de la ventana
            setTitle("Editor de Texto");
    
            // Configura la fuente inicial del JTextPane
            jTextPane1.setFont(new Font("Arial", Font.PLAIN, 14));
            // Añade el JTextPane al JScrollPane para permitir desplazamiento
            jScrollPane1.setViewportView(jTextPane1);
    
            // Asocia los botones con sus respectivos métodos de acción
            jButton1.addActionListener(e -> jButton1ActionPerformed());
            jButton2.addActionListener(e -> jButton2ActionPerformed());
            jButton3.addActionListener(e -> jButton3ActionPerformed());
            jButton4.addActionListener(e -> guardarArchivo());
            jButton5.addActionListener(e -> abrirArchivo());
            jButton6.addActionListener(e -> reemplazarPalabra());
            jButton7.addActionListener(e -> cambiarEstilo());
    
            // Crea un panel superior para los botones y campos de texto
            JPanel topPanel = new JPanel();
            // Añade los componentes al panel superior en orden
            topPanel.add(jButton1);
            topPanel.add(jButton2);
            topPanel.add(new JLabel("Buscar:"));
            topPanel.add(jTextField1);
            topPanel.add(jButton3);
            topPanel.add(new JLabel("Reemplazar con:"));
            topPanel.add(jTextField2);
            topPanel.add(jButton6);
            topPanel.add(jButton4);
            topPanel.add(jButton5);
            topPanel.add(jButton7);
    
            // Crea un panel dividido para mostrar el editor y el árbol lado a lado
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jScrollPane1, treePanel);
            // Establece el peso de división para que ambos lados tengan el mismo tamaño inicial
            splitPane.setResizeWeight(0.5);
    
            // Configura el layout de la ventana como BorderLayout
            getContentPane().setLayout(new BorderLayout());
            // Añade el panel superior en la parte norte
            getContentPane().add(topPanel, BorderLayout.NORTH);
            // Añade el panel dividido en el centro
            getContentPane().add(splitPane, BorderLayout.CENTER);
    
            // Establece el tamaño preferido de la ventana
            setPreferredSize(new Dimension(1200, 600));
            // Ajusta la ventana al tamaño de sus componentes
            pack();
        }
    
        // Método que se ejecuta al presionar el botón "Deshacer"
        private void jButton1ActionPerformed() {
            // Aplica la operación de deshacer y actualiza el JTextPane
            jTextPane1.setText(editor.undo());
            // Actualiza el árbol binario de búsqueda en el panel gráfico
            treePanel.setRoot(editor.getRoot());
        }
    
        // Método que se ejecuta al presionar el botón "Rehacer"
        private void jButton2ActionPerformed() {
            // Aplica la operación de rehacer y actualiza el JTextPane
            jTextPane1.setText(editor.redo());
            // Actualiza el árbol binario de búsqueda en el panel gráfico
            treePanel.setRoot(editor.getRoot());
        }
    
        // Método que se ejecuta al presionar el botón "Buscar"
        private void jButton3ActionPerformed() {
            // Obtiene la palabra a buscar desde el campo de texto
            String searchWord = jTextField1.getText().trim();
            // Verifica que la palabra no esté vacía
            if (!searchWord.isEmpty()) {
                // Busca la palabra en el editor lógico y obtiene su posición
                int position = editor.searchWord(searchWord);
                // Si la palabra se encuentra (posición >= 0)
                if (position >= 0) {
                    // Mueve el cursor al final de la palabra encontrada
                    jTextPane1.setCaretPosition(position + searchWord.length());
                    // Selecciona la palabra en el JTextPane
                    jTextPane1.select(position, position + searchWord.length());
                    // Enfoca el JTextPane para mostrar la selección
                    jTextPane1.requestFocusInWindow();
                } else {
                    // Muestra un mensaje si la palabra no se encuentra
                    JOptionPane.showMessageDialog(this, "Palabra no encontrada: " + searchWord, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    
        // Método para guardar el contenido del editor en un archivo
        private void guardarArchivo() {
            try {
                // Obtiene el texto actual del JTextPane
                String texto = jTextPane1.getText();
                // Define la ruta de la carpeta donde se guardarán los archivos
                Path carpeta = Paths.get(System.getProperty("user.home"), "Desktop", "MisTextos");
                // Crea la carpeta si no existe
                Files.createDirectories(carpeta);
    
                // Si no hay un archivo actual asociado
                if (currentFile == null) {
                    // Solicita al usuario un nombre para el archivo
                    String nombreArchivo = JOptionPane.showInputDialog(this, "Nombre del archivo:", "Guardar como", JOptionPane.PLAIN_MESSAGE);
                    // Si el usuario cancela o no ingresa un nombre, termina
                    if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) return;
                    // Crea el archivo con extensión .txt en la carpeta especificada
                    currentFile = carpeta.resolve(nombreArchivo.trim() + ".txt").toFile();
                }
    
                // Escribe el texto en el archivo
                Files.write(currentFile.toPath(), texto.getBytes());
                // Muestra un mensaje de confirmación con la ruta del archivo
                JOptionPane.showMessageDialog(this, "Archivo guardado correctamente en:\n" + currentFile.getAbsolutePath());
            } catch (IOException ex) {
                // Muestra un mensaje de error si falla el guardado
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage());
            }
        }
    
        // Método para abrir un archivo y cargar su contenido en el editor
        private void abrirArchivo() {
            try {
                // Define la ruta de la carpeta donde se buscan los archivos
                Path carpeta = Paths.get(System.getProperty("user.home"), "Desktop", "MisTextos");
                // Crea la carpeta si no existe
                Files.createDirectories(carpeta);
    
                // Crea un selector de archivos que inicia en la carpeta especificada
                JFileChooser chooser = new JFileChooser(carpeta.toFile());
                // Muestra el diálogo para abrir un archivo
                int resultado = chooser.showOpenDialog(this);
                // Si el usuario selecciona un archivo
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    // Obtiene el archivo seleccionado
                    currentFile = chooser.getSelectedFile();
                    // Lee el contenido del archivo como una cadena
                    String contenido = new String(Files.readAllBytes(currentFile.toPath()));
                    // Establece el contenido en el JTextPane
                    jTextPane1.setText(contenido);
                    // Actualiza el editor lógico con el contenido cargado
                    editor.updateText(contenido);
                    // Actualiza el árbol binario de búsqueda en el panel gráfico
                    treePanel.setRoot(editor.getRoot());
                }
            } catch (IOException ex) {
                // Muestra un mensaje de error si falla la apertura
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + ex.getMessage());
            }
        }
    
        // Método para reemplazar palabras en el texto manteniendo los estilos
        private void reemplazarPalabra() {
            // Obtiene la palabra a buscar y la palabra de reemplazo desde los campos de texto
            String palabraBuscar = jTextField1.getText().trim();
            String palabraNueva = jTextField2.getText().trim();
    
            // Verifica que ambos campos no estén vacíos
            if (!palabraBuscar.isEmpty() && !palabraNueva.isEmpty()) {
                // Obtiene el documento estilizado del JTextPane
                StyledDocument doc = jTextPane1.getStyledDocument();
                // Obtiene el texto actual del JTextPane
                String texto = jTextPane1.getText();
                
                // Usa una expresión regular para encontrar coincidencias de palabras completas
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(palabraBuscar) + "\\b");
                Matcher matcher = pattern.matcher(texto);
                
                // Crea un StringBuilder para construir el texto actualizado
                StringBuilder nuevoTexto = new StringBuilder(texto);
                // Variable para compensar los cambios en las posiciones debido a reemplazos
                int offset = 0;
                
                try {
                    // Itera sobre todas las coincidencias de la palabra a buscar
                    while (matcher.find()) {
                        // Calcula las posiciones ajustadas de inicio y fin
                        int start = matcher.start() + offset;
                        int end = matcher.end() + offset;
                        
                        // Obtiene los atributos de estilo del texto en la posición inicial
                        AttributeSet attrs = doc.getCharacterElement(start).getAttributes();
                        
                        // Elimina la palabra original del documento
                        doc.remove(start, end - start);
                        // Inserta la nueva palabra con los mismos atributos de estilo
                        doc.insertString(start, palabraNueva, attrs);
                        
                        // Actualiza el StringBuilder con el nuevo texto
                        nuevoTexto.replace(start, end, palabraNueva);
                        // Ajusta el offset según la diferencia de longitud
                        offset += palabraNueva.length() - (end - start);
                    }
                    
                    // Actualiza el editor lógico con el texto final
                    editor.updateText(nuevoTexto.toString());
                    // Actualiza el árbol binario de búsqueda en el panel gráfico
                    treePanel.setRoot(editor.getRoot());
                } catch (BadLocationException ex) {
                    // Muestra un mensaje de error si falla el reemplazo
                    JOptionPane.showMessageDialog(this, "Error al reemplazar: " + ex.getMessage());
                }
            } else {
                // Muestra un mensaje si los campos están vacíos
                JOptionPane.showMessageDialog(this, "Ingrese palabra a buscar y su reemplazo.");
            }
        }
    
        // Método para cambiar el estilo (fuente, tamaño, color) del texto seleccionado
        private void cambiarEstilo() {
            // Obtiene las posiciones de inicio y fin del texto seleccionado
            int start = jTextPane1.getSelectionStart();
            int end = jTextPane1.getSelectionEnd();
    
            // Verifica que haya texto seleccionado
            if (start == end) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione texto para cambiar el estilo.");
                return;
            }
    
            // Crea un diálogo modal para seleccionar los estilos
            JDialog dialog = new JDialog(this, "Cambiar Estilo", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 500);
            dialog.setLocationRelativeTo(this);
    
            // Crea un panel para las opciones de estilo (fuente, tamaño, color)
            JPanel optionsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
    
            // Etiqueta y selector de fuente
            JLabel fontLabel = new JLabel("Fuente:");
            String[] fonts = {"Arial", "Times New Roman", "Courier New", "Verdana", "Helvetica"};
            JComboBox<String> fontCombo = new JComboBox<>(fonts);
            fontCombo.setSelectedItem("Arial");
    
            // Etiqueta y selector de tamaño
            JLabel sizeLabel = new JLabel("Tamaño:");
            Integer[] sizes = {12, 14, 16, 18, 20, 24, 28, 32};
            JComboBox<Integer> sizeCombo = new JComboBox<>(sizes);
            sizeCombo.setSelectedItem(14);
    
            // Etiqueta y botón para seleccionar color
            JLabel colorLabel = new JLabel("Color:");
            JButton colorButton = new JButton("Elegir Color");
            Color selectedColor = Color.BLACK;
            colorButton.setBackground(selectedColor);
            final Color[] chosenColor = {selectedColor};
    
            // Acción del botón de color: abre un selector de color
            colorButton.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(dialog, "Seleccionar Color", chosenColor[0]);
                if (newColor != null) {
                    chosenColor[0] = newColor;
                    colorButton.setBackground(newColor);
                }
            });
    
            // Añade los componentes al panel de opciones
            optionsPanel.add(fontLabel);
            optionsPanel.add(fontCombo);
            optionsPanel.add(sizeLabel);
            optionsPanel.add(sizeCombo);
            optionsPanel.add(colorLabel);
            optionsPanel.add(colorButton);
    
            // Crea un panel para los botones de confirmación
            JPanel buttonPanel = new JPanel();
            JButton okButton = new JButton("Aceptar");
            JButton cancelButton = new JButton("Cancelar");
    
            // Acción del botón "Aceptar": aplica los estilos seleccionados
            okButton.addActionListener(e -> {
                String selectedFont = (String) fontCombo.getSelectedItem();
                int selectedSize = (Integer) sizeCombo.getSelectedItem();
    
                // Crea un conjunto de atributos para los estilos
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setFontFamily(attrs, selectedFont);
                StyleConstants.setFontSize(attrs, selectedSize);
                StyleConstants.setForeground(attrs, chosenColor[0]);
    
                // Aplica los atributos al texto seleccionado
                StyledDocument doc = jTextPane1.getStyledDocument();
                doc.setCharacterAttributes(start, end - start, attrs, false);
    
                // Cierra el diálogo
                dialog.dispose();
            });
    
            // Acción del botón "Cancelar": cierra el diálogo sin cambios
            cancelButton.addActionListener(e -> dialog.dispose());
    
            // Añade los botones al panel de botones
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
    
            // Añade los paneles al diálogo
            dialog.add(optionsPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            // Muestra el diálogo
            dialog.setVisible(true);
        }
    
        // Método principal para iniciar la aplicación
        public static void main(String args[]) {
            // Ejecuta la creación de la ventana en el hilo de despacho de eventos de Swing
            java.awt.EventQueue.invokeLater(() -> new EditorWindow().setVisible(true));
        }
    
        // Declaración de los componentes de la interfaz
        private JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7;
        private JScrollPane jScrollPane1;
        private JTextPane jTextPane1;
        private JTextField jTextField1, jTextField2;
    }

Es la ventana principal de la aplicación que contiene la interfaz gráfica con todos los botones, área de texto, árbol visual, funciones de estilizado, guardar, abrir, etc.

### Función principal:
Permitir al usuario editar texto, estilizarlo, guardar/abrir archivos, y visualizar el árbol binario de palabras.

## Main.java

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

Es la clase que inicia la aplicación. Contiene el método `main`, punto de entrada del programa, y se encarga de lanzar la ventana principal (`EditorWindow`) en el hilo de eventos de Swing (GUI).

### Función principal:
Arrancar la interfaz gráfica del editor cuando el programa se ejecuta.

## TextEditor.java

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

Es la clase de lógica del editor: administra el texto, operaciones de deshacer/rehacer, búsqueda, reemplazo y genera el árbol de palabras.

### Función principal:
Manejar el contenido del editor y las estructuras necesarias.

## TreePanel.java

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

Es un componente gráfico (`JPanel`) que dibuja visualmente el árbol binario de búsqueda de palabras. Muestra los nodos y sus relaciones de forma jerárquica.

### Función principal:
Permitir ver de manera gráfica cómo se ordenan las palabras del texto en un árbol binario.

## WordNode.java

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

Representa un nodo de un árbol binario de búsqueda que contiene una palabra del texto y su posición. Se usa para construir el árbol que visualiza cómo están organizadas alfabéticamente las palabras del editor.

### Función principal:
Estructura básica del árbol binario para buscar y organizar palabras.
