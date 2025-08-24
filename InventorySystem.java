/**
 * Sistema de Inventario vía Árbol AVL (Auto-balanceado)
 * 
 * DECISIONES DE DISEÑO:
 * 
 * 1. AVL Tree: Garantiza O(log n) en todas las operaciones
 * 2. Auto-balanceado: Evita degradación a O(n) del BST básico
 * 3. Implementación desde cero: Cumple la restricción de no usar estructuras predefinidas
 * 
 */
public class InventorySystem {
    
    /**
     * Nodo interno del árbol AVL
     * 
     * DECISIÓN: Clase interna para encapsulación
     * - Solo el InventorySystem puede crear/modificar nodos
     * - Evita exposición innecesaria de la estructura interna
     */
    private class AVLNode {
        int code;           // Código del producto
        AVLNode left;       // Hijo izquierdo (menores)
        AVLNode right;      // Hijo derecho (mayores)
        int height;         // Altura para balanceado AVL
        
        public AVLNode(int code) {
            this.code = code;
            this.left = null;
            this.right = null;
            this.height = 1;    // Nodo hoja tiene altura 1
        }
    }
    
    private AVLNode root;       // Raíz del árbol
    private int size;           // Contador de elementos (para estadísticas)
    
    /**
     * Constructor
     * DECISIÓN: Inicialización simple y clara
     */
    public InventorySystem() {
        this.root = null;
        this.size = 0;
    }
    
    // MÉTODOS DE UTILIDAD AVL
    
    /**
     * Obtiene la altura de un nodo
     * DECISIÓN: Método auxiliar para manejar nulos de forma segura
     */
    private int getHeight(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }
    
    /**
     * Calcula el factor de balance de un nodo
     * DECISIÓN: Factor de balance = altura(izquierdo) - altura(derecho)
     * - Positivo: árbol inclinado hacia la izquierda
     * - Negativo: árbol inclinado hacia la derecha
     * - AVL requiere que esté en el intervalo [-1, 1]
     */
    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : (getHeight(node.left) - getHeight(node.right));
    }
    
    /**
     * Actualiza la altura de un nodo basado en sus hijos
     * DECISIÓN: Altura = 1 + máximo(altura_izquierdo, altura_derecho)
     */
    private void updateHeight(AVLNode node) {
        if (node != null) {
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
    }
    
    // ======================== ROTACIONES AVL ========================
    
    /**
     * Rotación simple a la derecha
     * DECISIÓN: Corrige desequilibrio izquierdo-izquierdo
     * 
     * Antes:     y          Después:    x
     *           / \                    / \
     *          x   C                  A   y
     *         / \                        / \
     *        A   B                      B   C
     */
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode B = x.right;
        
        // Realizar rotación
        x.right = y;
        y.left = B;
        
        // Actualizar alturas (orden importante: primero y, luego x)
        updateHeight(y);
        updateHeight(x);
        
        return x;   // Nueva raíz del subárbol
    }
    
    /**
     * Rotación simple a la izquierda
     * DECISIÓN: Corrige desbalance derecho-derecho
     */
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode B = y.left;
        
        // Realizar rotación
        y.left = x;
        x.right = B;
        
        // Actualizar alturas
        updateHeight(x);
        updateHeight(y);
        
        return y;   // Nueva raíz del subárbol
    }
    
    // ======================== INSERCIÓN ========================
    
    /**
     * Inserta un código en el sistema
     * DECISIÓN: Método público basado en el método recursivo privado "insertAVL"
     * 
     * - Separación de responsabilidades
     * - Control de acceso a la estructura interna
     * 
     */
    public void insert(int code) {
        root = insertAVL(root, code);
    }
    
    /**
     * Inserción recursiva con balanceado AVL
     * DECISIÓN: Recursividad para simplicidad y elegancia del código
     * 
     * COMPLEJIDAD: O(log n) garantizado por el balanceado
     */
    private AVLNode insertAVL(AVLNode node, int code) {
        // PASO 1: Inserción BST normal
        if (node == null) {
            size++;
            return new AVLNode(code);
        }
        
        if (code < node.code) {
            node.left = insertAVL(node.left, code);
        } 
        else if (code > node.code) {
            node.right = insertAVL(node.right, code);
        } 
        else {
            // DECISIÓN: No permitir duplicados (común en sistemas de inventario)
            return node;
        }
        
        // PASO 2: Actualizar altura
        updateHeight(node);
        
        // PASO 3: Obtener factor de balance
        int balance = getBalance(node);
        
        // PASO 4: Realizar rotaciones si es necesario
        
        // Caso Izquierdo-Izquierdo
        if (balance > 1 && code < node.left.code) {
            return rotateRight(node);
        }
        
        // Caso Derecho-Derecho
        if (balance < -1 && code > node.right.code) {
            return rotateLeft(node);
        }
        
        // Caso Izquierdo-Derecho
        if (balance > 1 && code > node.left.code) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // Caso Derecho-Izquierdo
        if (balance < -1 && code < node.right.code) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;    // Retornar nodo sin cambios si está balanceado
    }
    
    // ======================== BÚSQUEDA ========================
    
    /**
     * Busca un código específico en el sistema
     * DECISIÓN: BST permite búsqueda eficiente comparando valores
     * 
     * COMPLEJIDAD: O(log n) por el balanceado AVL
     */
    public boolean search(int code) {
        return searchRecursive(root, code);
    }
    
    /**
     * Búsqueda recursiva
     * DECISIÓN: Recursividad es más intuitiva para árboles
     */
    private boolean searchRecursive(AVLNode node, int code) {
        // Caso base: nodo vacío
        if (node == null) {
            return false;
        }
        
        // Caso base: encontrado
        if (code == node.code) {
            return true;
        }
        
        // Búsqueda recursiva
        if (code < node.code) {
            return searchRecursive(node.left, code);
        } else {
            return searchRecursive(node.right, code);
        }
    }
    
    // ======================== RECORRIDOS ========================
    
    /**
     * Muestra elementos en orden ascendente (In-Order Traversal)
     * DECISIÓN: In-order en BST produce secuencia ordenada automáticamente
     */
    public void showAscending() {
        System.out.println("=== ORDEN ASCENDENTE ===");
        inOrderTraversal(root);
        System.out.println();
    }
    
    private void inOrderTraversal(AVLNode node) {
        if (node != null) {
            inOrderTraversal(node.left);    // Primero los menores
            System.out.print(node.code + " ");
            inOrderTraversal(node.right);   // Luego los mayores
        }
    }
    
    /**
     * Muestra elementos nivel por nivel (Breadth-First)
     * DECISIÓN: Usar cola implementada manualmente para cumplir restricciones
     */
    public void showByLevels() {
        System.out.println("=== RECORRIDO POR NIVELES ===");
        if (root == null) {
            System.out.println("Árbol vacío");
            return;
        }
        
        // Implementación manual de cola usando array circular
        AVLNode[] queue = new AVLNode[size * 2]; // Tamaño suficiente
        int front = 0, rear = 0;
        
        // Añadir raíz a la cola
        queue[rear++] = root;
        
        while (front < rear) {
            AVLNode current = queue[front++];
            System.out.print(current.code + " ");
            
            // Añadir hijos a la cola
            if (current.left != null) {
                queue[rear++] = current.left;
            }
            if (current.right != null) {
                queue[rear++] = current.right;
            }
        }
        System.out.println();
    }
    
    public void showDescending() {
        System.out.println("=== ORDEN DESCENDENTE ===");
        reverseInOrder(root);
        System.out.println();
    }

    private void reverseInOrder(AVLNode node) {
        if (node != null) {
            reverseInOrder(node.right);     // primero los mayores
            System.out.print(node.code + " ");
            reverseInOrder(node.left);      // luego los menores
        }
    }

    
    /**
     * Muestra elementos visitando primero padres, luego hijos (Pre-Order)
     * DECISIÓN: Pre-order muestra jerarquía natural del árbol
     */
    public void showHierarchical() {
        System.out.println("=== RECORRIDO JERÁRQUICO (Padre->Hijos) ===");
        preOrderTraversal(root);
        System.out.println();
    }
    
    private void preOrderTraversal(AVLNode node) {
        if (node != null) {
            System.out.print(node.code + " ");     // Primero el padre
            preOrderTraversal(node.left);          // Luego hijo izquierdo
            preOrderTraversal(node.right);         // Finalmente hijo derecho
        }
    }
    
    // ======================== MÉTODOS AUXILIARES ========================
    
    /**
     * Retorna el número de elementos en el sistema
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Verifica si el sistema está vacío
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * Muestra estadísticas del árbol
     * DECISIÓN: Útil para debugging y análisis de rendimiento
     */
    public void showStats() {
        System.out.println("=== ESTADÍSTICAS DEL SISTEMA ===");
        System.out.println("Total de códigos: " + size);
        System.out.println("Altura del árbol: " + getHeight(root));
        System.out.println("Árbol balanceado: " + isBalanced(root));
        System.out.println();
    }
    
    /**
     * Verifica si el árbol está balanceado (para testing)
     */
    private boolean isBalanced(AVLNode node) {
        if (node == null) return true;
        
        int balance = getBalance(node);
        return (Math.abs(balance) <= 1 && isBalanced(node.left) && isBalanced(node.right));
    }
    
    // ======================== CLASE DE PRUEBA ========================
    
    /**
     * Método main para demostración
     * DECISIÓN: Incluir ejemplos de uso para facilitar testing
     */
    public static void main(String[] args) {
        InventorySystem inventory = new InventorySystem();
        
        // Datos de prueba del enunciado
        int[] testData = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
        
        System.out.println("=== DEMO DEL SISTEMA DE INVENTARIO ===\n");
        
        // Insertar datos
        System.out.println("Insertando códigos: ");
        for (int code : testData) {
            System.out.print(code + " ");
            inventory.insert(code);
        }
        System.out.println("\n");
        
        // Mostrar estadísticas
        inventory.showStats();
        
        // Demostrar diferentes recorridos
        inventory.showAscending();
        inventory.showDescending();  
        inventory.showHierarchical();
        inventory.showByLevels();
        
        // Demostrar búsquedas
        System.out.println("=== PRUEBAS DE BÚSQUEDA ===");
        int[] searchCodes = {50, 25, 100, 80, 15};
        for (int code : searchCodes) {
            boolean found = inventory.search(code);
            System.out.println("Código " + code + ": " + (found ? "ENCONTRADO" : "NO ENCONTRADO"));
        }
    }
}
