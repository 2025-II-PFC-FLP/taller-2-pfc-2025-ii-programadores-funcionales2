package taller

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConjuntosDifusosTest extends AnyFunSuite {

  val cj = new ConjuntosDifusos()
  test("grande con números negativos o cero debe retornar 0.0") {
    val conjunto = cj.grande(5, 2)
    assert(conjunto(-1) == 0.0)
    assert(conjunto(0) == 0.0)
    assert(conjunto(-100) == 0.0)
  }

  test("grande con números positivos debe calcular correctamente la pertenencia") {
    val conjunto = cj.grande(10, 2)
    // Para n=10, d=10: 10/(10+10) = 0.5, elevado a 2 = 0.25
    assert(conjunto(10) == 0.25)
  }

  test("grande con exponente 1 debe comportarse como función lineal") {
    val conjunto = cj.grande(10, 1)
    // Para n=10, d=10: 10/(10+10) = 0.5
    assert(conjunto(10) == 0.5)
  }
  test("complemento debe retornar 1 - valor original") {
    val conjunto = cj.grande(10, 2)
    val complemento = cj.complemento(conjunto)
    assert(complemento(10) == 0.75) // 1 - 0.25 = 0.75
  }

  test("complemento de 0 debe ser 1") {
    val conjunto = cj.grande(10, 2)
    val complemento = cj.complemento(conjunto)
    assert(complemento(0) == 1.0)
  }

  test("complemento de complemento debe devolver el conjunto original") {
    val conjunto = cj.grande(5, 2)
    val complemento1 = cj.complemento(conjunto)
    val complemento2 = cj.complemento(complemento1)
    assert(math.abs(complemento2(10) - conjunto(10)) < 0.0001)
  }

  test("complemento siempre retorna valores entre 0 y 1") {
    val conjunto = cj.grande(10, 2)
    val complemento = cj.complemento(conjunto)
    for (n <- -5 to 50) {
      val valor = complemento(n)
      assert(valor >= 0.0 && valor <= 1.0)
    }
  }

  test("unión debe retornar el máximo de los dos valores") {
    val conjunto1 = cj.grande(10, 1)
    val conjunto2 = cj.grande(20, 1)
    val union = cj.union(conjunto1, conjunto2)

    val valor1 = conjunto1(15)
    val valor2 = conjunto2(15)
    assert(union(15) == math.max(valor1, valor2))
  }

  test("unión con conjuntos idénticos devuelve el mismo conjunto") {
    val conjunto = cj.grande(10, 1)
    val union = cj.union(conjunto, conjunto)
    assert(union(10) == conjunto(10))
  }

  test("unión con conjunto nulo devuelve el conjunto original") {
    val conjunto = cj.grande(10, 1)
    val conjuntoNulo = (_: Int) => 0.0
    val union = cj.union(conjunto, conjuntoNulo)
    assert(union(10) == conjunto(10))
  }

  test("unión con conjunto universal devuelve siempre 1") {
    val conjunto = cj.grande(10, 1)
    val conjuntoTotal = (_: Int) => 1.0
    val union = cj.union(conjunto, conjuntoTotal)
    assert(union(10) == 1.0)
  }
  test("intersección debe retornar el mínimo de los dos valores") {
    val conjunto1 = cj.grande(10, 1)
    val conjunto2 = cj.grande(20, 1)
    val interseccion = cj.interseccion(conjunto1, conjunto2)

    val valor1 = conjunto1(15)
    val valor2 = conjunto2(15)
    assert(interseccion(15) == math.min(valor1, valor2))
  }

  test("intersección con conjuntos idénticos devuelve el mismo conjunto") {
    val conjunto = cj.grande(10, 1)
    val inter = cj.interseccion(conjunto, conjunto)
    assert(inter(10) == conjunto(10))
  }

  test("intersección con conjunto nulo devuelve 0 siempre") {
    val conjunto = cj.grande(10, 1)
    val conjuntoNulo = (_: Int) => 0.0
    val inter = cj.interseccion(conjunto, conjuntoNulo)
    assert(inter(10) == 0.0)
  }

  test("intersección con conjunto universal devuelve el conjunto original") {
    val conjunto = cj.grande(10, 1)
    val conjuntoTotal = (_: Int) => 1.0
    val inter = cj.interseccion(conjunto, conjuntoTotal)
    assert(inter(10) == conjunto(10))
  }


  test("grande con diferentes parámetros d y e") {
    val conjunto1 = cj.grande(5, 1)
    // Para n=10, d=5: 10/(10+5) = 10/15 ≈ 0.666
    assert(conjunto1(10) == 10.0/15.0)

    val conjunto2 = cj.grande(5, 2)
    // Para n=10, d=5, e=2: (10/15)² ≈ 0.444
    assert(conjunto2(10) == math.pow(10.0/15.0, 2))
  }
  test("complemento de complemento debe ser el conjunto original") {
    val conjuntoOriginal = cj.grande(10, 2)
    val complemento1 = cj.complemento(conjuntoOriginal)
    val complemento2 = cj.complemento(complemento1)

    assert(complemento2(10) == conjuntoOriginal(10))
  }

  test("unión e intersección con el mismo conjunto") {
    val conjunto = cj.grande(10, 2)
    val unionMismo = cj.union(conjunto, conjunto)
    val interseccionMismo = cj.interseccion(conjunto, conjunto)

    assert(unionMismo(10) == conjunto(10))
    assert(interseccionMismo(10) == conjunto(10))
  }

  test("grande con números muy grandes se acerca a 1") {
    val conjunto = cj.grande(10, 1)
    // Para n=1000: 1000/(1000+10) ≈ 0.990
    assert(conjunto(1000) > 0.99)
  }

  test("grande con números pequeños tiene valores bajos") {
    val conjunto = cj.grande(10, 1)
    // Para n=1: 1/(1+10) = 1/11 ≈ 0.0909
    assert(conjunto(1) == 1.0/11.0)
  }
  test("valores de pertenencia siempre entre 0 y 1") {
    val conjunto = cj.grande(10, 2)

    // Probar con varios valores
    val valores = List(-10, -1, 0, 1, 5, 10, 50, 100)

    valores.foreach { n =>
      val valor = conjunto(n)
      assert(valor >= 0.0 && valor <= 1.0)
    }
  }

}
