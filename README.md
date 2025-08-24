Sistema de inventario implementado desde cero mediante un **árbol AVL** (árbol binario de búsqueda auto-balanceado).
Cumple con los requisitos de inserción ordenada, búsqueda eficiente y múltiples formas de recorrido.

## 4.1. Cómo ejecutar el programa

1. Clona el repositorio o descarga el archivo `InventorySystem.java`.
2. Compila el código con:

   ```bash
   javac InventorySystem.java
   ```
3. Ejecuta el programa con:

   ```bash
   java InventorySystem
   ```

---

## 4.2. Ejemplos de uso

### Ejemplo de ejecución con los datos de prueba:

Entrada (desde el `main`):

```java
int[] testData = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
for (int code : testData) {
    inventory.insert(code);
}
```

Salida esperada (fragmentos):

```
=== ESTADÍSTICAS DEL SISTEMA ===
Total de códigos: 11
Altura del árbol: 4
Árbol balanceado: true

=== ORDEN ASCENDENTE ===
10 20 25 30 35 40 45 50 60 70 80

=== ORDEN DESCENDENTE ===
80 70 60 50 45 40 35 30 25 20 10

=== RECORRIDO JERÁRQUICO (Padre->Hijos) ===
50 30 20 10 25 40 35 45 70 60 80

=== RECORRIDO POR NIVELES ===
50 30 70 20 40 60 80 10 25 35 45

=== PRUEBAS DE BÚSQUEDA ===
Código 50: ENCONTRADO
Código 25: ENCONTRADO
Código 100: NO ENCONTRADO
```

---

## 4.3. Breve explicación del enfoque

* **Estructura de datos elegida:** Árbol AVL (árbol binario de búsqueda balanceado).

  * Inserción y búsqueda en **O(log n)**.
  * Mantiene automáticamente el orden gracias a la propiedad BST (izquierda < raíz < derecha).
  * Usa rotaciones locales para equilibrar el árbol.

* **Recorridos implementados:**

  * In-order (ascendente).
  * Reverse in-order (descendente).
  * Pre-order (padres antes que hijos).
  * BFS (nivel por nivel), con **cola implementada manualmente** (sin `ArrayList` ni `Queue`).

* **Decisión clave:** no se permiten duplicados, dado que en un sistema de inventario cada código debe ser único.

---

## Estructura del repositorio

```
.
├── InventorySystem.java   # Código fuente principal
└── README.md              # Documentación del proyecto
```
