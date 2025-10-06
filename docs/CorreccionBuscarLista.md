# Informe de corrección BuscarLista

## Especificación matemática

Sea
$$
f : \text{List}[\mathbb{Z}] \times \mathbb{Z} \to \text{Bool}
$$

La función que determina si un elemento pertenece a una lista de enteros:

$$
f(\text{lista}, e) =
\begin{cases}
\text{false}, & \text{si } \text{lista} = [] \\
\text{true}, & \text{si } \text{head(lista)} = e \\
f(\text{tail(lista)}, e), & \text{en otro caso}
\end{cases}
$$

---

## Implementación en Scala

```scala
def buscarElemento(lista: List[Int], elemento: Int): Boolean = {
  lista match {
    case Nil => false
    case x :: xs => if (x == elemento) true else buscarElemento(xs, elemento)
  }
}
```

La función `buscarElemento` implementa la definición recursiva anterior.
Queremos demostrar que, para toda lista finita de enteros y cualquier elemento `e`:

$$
\forall \text{lista} \in \text{List}[\mathbb{Z}] :
buscarElemento(\text{lista}, e) = f(\text{lista}, e)
$$

---

## Demostración de corrección

Como el dominio de la función son **listas**, las cuales se definen recursivamente, la corrección se demuestra por **inducción estructural** sobre la lista `lista`.

---

### Caso base:

$$
\text{lista} = [ ]
$$

En el programa:

```scala
case Nil => false
```

Se tiene:

$$
buscarElemento([ ], e) \to false
$$

Por definición matemática:

$$
f([ ], e) = false
$$

Por lo tanto:

$$
buscarElemento([], e) = f([], e)
$$

**El caso base se cumple.**

---

### Caso inductivo:

Supongamos que la lista tiene la forma

$$
\text{lista} = x :: xs
$$

donde `x` es la cabeza y `xs` la cola de la lista.

Hipótesis de inducción (H.I.):

$$
buscarElemento(xs, e) = f(xs, e)
$$

Queremos demostrar que:

$$
buscarElemento(x :: xs, e) = f(x :: xs, e)
$$

#### En el programa:

```scala
case x :: xs => if (x == elemento) true else buscarElemento(xs, elemento)
```

Existen dos subcasos:

---

#### Subcaso 1: `x == e`

$$
buscarElemento(x :: xs, e) = \text{true}
$$

Por definición matemática:

$$
f(x :: xs, e) = \text{true}
$$

Entonces:

$$
buscarElemento(x :: xs, e) = f(x :: xs, e)
$$

---

#### Subcaso 2: `x ≠ e`

$$
buscarElemento(x :: xs, e) = buscarElemento(xs, e)
$$

Por hipótesis de inducción:

$$
buscarElemento(xs, e) = f(xs, e)
$$

Y según la definición matemática:

$$
f(x :: xs, e) = f(xs, e)
$$

Por lo tanto:

$$
buscarElemento(x :: xs, e) = f(x :: xs, e)
$$

**El caso inductivo se cumple.**

---

### Conclusión general

Por **inducción estructural** sobre la lista `lista`, se cumple que:

$$
\forall \text{lista} \in \text{List}[\mathbb{Z}] : buscarElemento(\text{lista}, e) = f(\text{lista}, e)
$$

---

## Verificación semántica y terminación

* **Terminación garantizada:** En cada paso, el tamaño de la lista se reduce (`xs` es más corta que `lista`), y la recursión llega inevitablemente al caso base `Nil`.

* **Ausencia de efectos colaterales:** La función no modifica la lista original ni usa variables mutables, cumpliendo con los principios de la programación funcional pura.

* **Dominio completo:** El patrón de coincidencia (`Nil` y `x :: xs`) cubre todas las posibles formas de una lista, asegurando exhaustividad.

* **Correctitud semántica:** La implementación en Scala preserva exactamente la semántica matemática de la relación de pertenencia en una lista.

---

## Conclusión

El algoritmo `buscarElemento` es correcto respecto a su especificación matemática.
La demostración por inducción estructural confirma que el programa calcula precisamente la función de pertenencia sobre listas de enteros.

El diseño asegura **terminación, pureza funcional y corrección estructural, cumpliendo los principios del paradigma funcional y los criterios formales de corrección de programas recursivos.


