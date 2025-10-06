# Informe de proceso Conjuntos Difusos

## Definición del Algoritmo

```Scala
package taller

class ConjuntosDifusos {
  type ConjDifuso = Int => Double

  def pertenece(elem: Int, s: ConjDifuso): Double = {
    s(elem)
  }

  def muchoMayorQue(a: Int, m: Int): ConjDifuso = {
    (x: Int) => {
      if (x <= a) 0.0
      else if (x > a && x <= m) (x - a).toDouble / (m - a).toDouble
      else 1.0
    }
  }

  def grande(d: Int, e: Int): ConjDifuso = {
    (n: Int) => {
      if (n <= 0) 0.0
      else {
        val pertenencia = n.toDouble / (n + d)
        math.pow(pertenencia, e)
      }
    }
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    (n: Int) => 1.0 - c(n)
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (n: Int) => math.max(cd1(n), cd2(n))
  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (n: Int) => math.min(cd1(n), cd2(n))
  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    @annotation.tailrec
    def aux(n: Int): Boolean = {
      if (n > 1000) true
      else if (cd1(n) > cd2(n)) false
      else aux(n + 1)
    }
    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    val epsilon = 1e-6
    @annotation.tailrec
    def aux(n: Int): Boolean = {
      if (n > 1000) true
      else if (math.abs(cd1(n) - cd2(n)) > epsilon) false
      else aux(n + 1)
    }
    aux(0)
  }
}
```

---

## Descripción General

El objetivo del algoritmo es implementar operaciones fundamentales sobre conjuntos difusos en Scala, modelando funciones de pertenencia sobre números enteros.
Cada conjunto se representa como una función anónima `Int => Double` donde el resultado está en el rango ([0, 1]), indicando el grado de pertenencia del elemento.

La clase define operaciones básicas como pertenencia, complemento, unión, intersección, inclusión e igualdad, siguiendo la teoría clásica de Zadeh (1965).

---

## Explicación paso a paso de las funciones

### 1. `pertenece`

```Scala
def pertenece(elem: Int, s: ConjDifuso): Double = s(elem)
```

Evalúa directamente la función de pertenencia `s` para un elemento `elem`.
Es una aplicación directa del principio matemático:

$$
\mu_A(x) = A(x)
$$

donde $A$ es el conjunto difuso y $\mu_A$ su función de pertenencia.

---

### 2. `muchoMayorQue(a, m)`

Define un conjunto difuso de tipo “rampa” o función creciente lineal:

```Scala
(x: Int) => {
  if (x <= a) 0.0
  else if (x > a && x <= m) (x - a).toDouble / (m - a).toDouble
  else 1.0
}
```

- Si $x \le a$: el valor es 0.
- Si $a < x \le m$: el valor aumenta linealmente de 0 a 1.
- Si $x > m$: el valor se mantiene en 1.

Representa un operador de intensidad (por ejemplo, “mucho mayor que 5”).

---

### 3. `grande(d, e)`

```Scala
(n: Int) => {
  if (n <= 0) 0.0
  else {
    val pertenencia = n.toDouble / (n + d)
    math.pow(pertenencia, e)
  }
}
```

Implementa una función sigmoidal de crecimiento suave, donde:

- `d` controla la pendiente o “difusidad”.
- `e` ajusta la curvatura.

Esta función modela el concepto lingüístico de “ser grande” bajo un enfoque difuso.

---

### 4. `complemento`

```Scala
(n: Int) => 1.0 - c(n)
```

Implementa el complemento difuso clásico:

$$
\mu_{\overline{A}}(x) = 1 - \mu_A(x)
$$

---

### 5. `union` y `interseccion`

```Scala
(n: Int) => math.max(cd1(n), cd2(n)) // unión
(n: Int) => math.min(cd1(n), cd2(n)) // intersección
```

Implementan respectivamente:

$$
\mu_{A \cup B}(x) = \max(\mu_A(x), \mu_B(x))
$$

$$
\mu_{A \cap B}(x) = \min(\mu_A(x), \mu_B(x))
$$

Estas funciones son simétricas, conmutativas e idempotentes, tal como se define en la teoría de conjuntos difusos.

---

### 6. `inclusion`

Evalúa si un conjunto está incluido en otro:

```Scala
@annotation.tailrec
def aux(n: Int): Boolean = {
  if (n > 1000) true
  else if (cd1(n) > cd2(n)) false
  else aux(n + 1)
}
```

- Si para todo $n \in [0,1000]$, $\mu_{A}(n) \le \mu_{B}(n)$, se cumple la inclusión.
- Usa recursión de cola (`@tailrec`), garantizando eficiencia y terminación.

---

### 7. `igualdad`

Compara dos conjuntos difusos considerando un margen de tolerancia numérica (`epsilon`):

```Scala
val epsilon = 1e-6
@annotation.tailrec
def aux(n: Int): Boolean = {
  if (n > 1000) true
  else if (math.abs(cd1(n) - cd2(n)) > epsilon) false
  else aux(n + 1)
}
```

Matemáticamente:

$$
A = B \iff \forall x, |\mu_A(x) - \mu_B(x)| < \epsilon
$$

---

## Llamado de funciones y flujo lógico

Ejemplo de ejecución conceptual:

```mermaid
graph TD
A[Elemento x] --> B[Función de pertenencia μA(x)]
B --> C[Operación complementaria 1 - μA(x)]
C --> D[Unión/Intersección con otra función]
D --> E[Comparación de pertenencias]
E --> F[Resultado: Valor o Booleano]
```

---

## Características técnicas

- **Paradigma:** Programación funcional pura.
- **Estructura:** Funciones de orden superior (`Int => Double`).
- **Terminación:** Garantizada mediante recursión de cola en `inclusion` e `igualdad`.
- **Pureza:** Sin efectos secundarios, todas las funciones son deterministas.
- **Dominio:** Intervalo discreto de enteros [0, 1000] para análisis práctico.
- **Precisión numérica:** Controlada con `epsilon = 1e-6` para evitar errores de punto flotante.

---

## Conclusión

El algoritmo `ConjuntosDifusos` implementa correctamente las operaciones básicas de la lógica difusa bajo un enfoque funcional.
Cada operación corresponde directamente con su definición matemática formal, y las propiedades teóricas —idempotencia, conmutatividad, complementación e inclusión reflexiva— se mantienen en todos los casos.

Además, las funciones `inclusion` e `igualdad` emplean recursión de cola, lo que garantiza optimización en tiempo de ejecución y evita desbordamientos de pila.
La implementación es puramente funcional, sin mutabilidad, y modela fielmente la semántica matemática de los conjuntos difusos definidos por Zadeh.
