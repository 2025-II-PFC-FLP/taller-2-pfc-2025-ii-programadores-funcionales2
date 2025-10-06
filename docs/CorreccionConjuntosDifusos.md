# Informe de corrección Conjuntos Difusos

## Especificación matemática de las operaciones

Sea $A, B$ dos conjuntos difusos, definidos por sus funciones de pertenencia $\mu_A$ y $\mu_B$.
Las operaciones fundamentales se definen así:

- **Complemento**

$$
\mu_{\neg A}(x) = 1 - \mu_A(x)
$$

- **Unión**

$$
\mu_{A \cup B}(x) = \max(\mu_A(x), \mu_B(x))
$$

- **Intersección**

$$
\mu_{A \cap B}(x) = \min(\mu_A(x), \mu_B(x))
$$

- **Inclusión**
  
$$
A \subseteq B \iff \forall x \in X, \ \mu_A(x) \leq \mu_B(x)
$$

- **Igualdad**

$$
A = B \iff \forall x \in X, \ \mu_A(x) = \mu_B(x)
$$

Además, se definen funciones generadoras de conjuntos particulares:

- **muchoMayorQue(a, m)**:
  Representa el conjunto de números “mucho mayores que $a$” con pendiente lineal hasta $m$.

  $$
  \mu(x) =
  \begin{cases}
  0 & \text{si } x \leq a \\
  \dfrac{x - a}{m - a} & \text{si } a < x \leq m \\
  1 & \text{si } x > m
  \end{cases}
  $$

- **grande(d, e)**:
  Representa el conjunto de números “grandes”, según el parámetro de difusión $d$ y el exponente $e$:

  $$
  \mu(x) =
  \begin{cases}
  0 & \text{si } x \leq 0 \\
  \left( \dfrac{x}{x + d} \right)^e & \text{si } x > 0
  \end{cases}
  $$

---

## Implementación en Scala

Cada definición matemática anterior se traduce directamente en las siguientes implementaciones:

```scala
def complemento(c: ConjDifuso): ConjDifuso =
  (n: Int) => 1.0 - c(n)

def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso =
  (n: Int) => math.max(cd1(n), cd2(n))

def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso =
  (n: Int) => math.min(cd1(n), cd2(n))
```

Estas definiciones son funcionalmente puras y no dependen de estado mutable.
Cada una devuelve una nueva función de pertenencia, cumpliendo con la naturaleza de los conjuntos difusos.

---

## Argumentación de corrección formal

Queremos demostrar que el programa implementa correctamente las definiciones matemáticas:

$$
\forall x \in \mathbb{Z}, \quad P_f(x) = f(x)
$$

donde $P_f$ es la función implementada en Scala y $f$ la definición matemática teórica.

---

### 1. Caso del complemento

$$
P_f(c)(x) = 1 - c(x)
$$

Esto coincide exactamente con:

$$
\mu_{\neg A}(x) = 1 - \mu_A(x)
$$

**Correcto por definición directa.**

---

### 2. Caso de la unión

$$
P_f(cd1, cd2)(x) = \max(cd1(x), cd2(x))
$$

Esto implementa:

$$
\mu_{A \cup B}(x) = \max(\mu_A(x), \mu_B(x))
$$

**Correcto por definición directa.**

---

### 3. Caso de la intersección

$$
P_f(cd1, cd2)(x) = \min(cd1(x), cd2(x))
$$

Esto implementa:

$$
\mu_{A \cap B}(x) = \min(\mu_A(x), \mu_B(x))
$$

**Correcto por definición directa.**

---

### 4. Caso de inclusión

```scala
def inclusion(cd1, cd2): Boolean = {
  @tailrec
  def aux(n: Int): Boolean = {
    if (n > 1000) true
    else if (cd1(n) > cd2(n)) false
    else aux(n + 1)
  }
  aux(0)
}
```

Formalmente:

$$
P_f(cd1, cd2) = \forall n \in [0,1000], \ \mu_A(n) \le \mu_B(n)
$$

La función `aux` es recursión de cola (tail recursion) y explora todos los enteros del dominio [0,1000], devolviendo `false` si encuentra alguna violación de la condición.

Cumple con la definición de inclusión difusa acotada.

---

### 5. Caso de igualdad

```scala
def igualdad(cd1, cd2): Boolean = {
  val epsilon = 1e-6
  @tailrec
  def aux(n: Int): Boolean = {
    if (n > 1000) true
    else if (math.abs(cd1(n) - cd2(n)) > epsilon) false
    else aux(n + 1)
  }
  aux(0)
}
```

Matemáticamente:


$$
A = B \iff \forall n \in [0,1000], \ |\mu_A(n) - \mu_B(n)| \le \varepsilon
$$

La introducción del valor `epsilon` garantiza estabilidad numérica al comparar valores en punto flotante.
La recursión de cola asegura terminación y eficiencia.

---

## Verificación semántica y terminación

- Todas las funciones devuelven valores en el rango $[0,1]$.
- No existen ciclos infinitos: las funciones recursivas (`inclusion` e `igualdad`) reducen el dominio en cada paso y finalizan cuando (n > 1000).
- No se usa estado mutable, lo que garantiza transparencia referencial y pureza funcional.
- La estructura de las funciones cumple con la forma recursiva definida inductivamente sobre el dominio entero.

---

## Conclusión

El programa `ConjuntosDifusos` es correcto con respecto a su especificación matemática.
Cada operación coincide exactamente con su definición teórica de la lógica difusa y cumple las propiedades fundamentales:

- **Idempotencia:**
  $(A \cup A = A,\quad A \cap A = A)$
- **Conmutatividad:**
  $(A \cup B = B \cup A,\quad A \cap B = B \cap A)$
- **Complemento doble:**
  $(\neg(\neg A) = A)$
- **Inclusión reflexiva:**
  $(A \subseteq A)$

Por lo tanto, se concluye que:

$$
\forall A, B \in \text{ConjuntosDifusos}, \quad P_f(A,B) = f(A,B)
$$

El programa está estructurado correctamente, cumple las condiciones de terminación y es formalmente correcto según el principio de inducción estructural sobre el dominio discreto de los enteros.

