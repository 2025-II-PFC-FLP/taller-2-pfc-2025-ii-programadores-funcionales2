package taller

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConjuntosDifusosTest extends AnyFunSuite {
  test("pertenece con conjunto constante 0.0 debe retornar 0.0 para cualquier número") {
    val conjuntoCero: conjuntos.ConjDifuso = (x: Int) => 0.0
    assert(conjuntos.pertenece(0, conjuntoCero) == 0.0)
    assert(conjuntos.pertenece(5, conjuntoCero) == 0.0)
    assert(conjuntos.pertenece(-10, conjuntoCero) == 0.0)
    assert(conjuntos.pertenece(100, conjuntoCero) == 0.0)
    assert(conjuntos.pertenece(Int.MaxValue, conjuntoCero) == 0.0)
  }

  test("pertenece con conjunto constante 1.0 debe retornar 1.0 para cualquier número") {
    val conjuntoUno: conjuntos.ConjDifuso = (x: Int) => 1.0
    assert(conjuntos.pertenece(0, conjuntoUno) == 1.0)
    assert(conjuntos.pertenece(-5, conjuntoUno) == 1.0)
    assert(conjuntos.pertenece(50, conjuntoUno) == 1.0)
    assert(conjuntos.pertenece(1000, conjuntoUno) == 1.0)
    assert(conjuntos.pertenece(Int.MinValue, conjuntoUno) == 1.0)
  }

  test("pertenece con función lineal debe retornar valores correctos") {
    val conjuntoLineal: conjuntos.ConjDifuso = (x: Int) => x.toDouble / 10.0
    assert(conjuntos.pertenece(0, conjuntoLineal) == 0.0)
    assert(conjuntos.pertenece(5, conjuntoLineal) == 0.5)
    assert(conjuntos.pertenece(10, conjuntoLineal) == 1.0)
    assert(conjuntos.pertenece(15, conjuntoLineal) == 1.5)
    assert(conjuntos.pertenece(-5, conjuntoLineal) == -0.5)
  }

  test("pertenece con función cuadrática debe retornar valores correctos") {
    val conjuntoCuadratico: conjuntos.ConjDifuso = (x: Int) => math.pow(x, 2) / 100.0
    assert(conjuntos.pertenece(0, conjuntoCuadratico) == 0.0)
    assert(conjuntos.pertenece(5, conjuntoCuadratico) == 0.25)
    assert(conjuntos.pertenece(10, conjuntoCuadratico) == 1.0)
    assert(conjuntos.pertenece(-5, conjuntoCuadratico) == 0.25)
    assert(conjuntos.pertenece(-10, conjuntoCuadratico) == 1.0)
  }

  test("pertenece con función por partes debe retornar valores correctos") {
    val conjuntoPartes: conjuntos.ConjDifuso = (x: Int) => {
      if (x < 0) 0.0
      else if (x < 5) 0.3
      else if (x < 10) 0.7
      else 1.0
    }
    assert(conjuntos.pertenece(-10, conjuntoPartes) == 0.0)
    assert(conjuntos.pertenece(0, conjuntoPartes) == 0.3)
    assert(conjuntos.pertenece(3, conjuntoPartes) == 0.3)
    assert(conjuntos.pertenece(7, conjuntoPartes) == 0.7)
    assert(conjuntos.pertenece(15, conjuntoPartes) == 1.0)
  }

  test("muchoMayorQue(1, 3) debe retornar valores correctos") {
    val mm1 = conjuntos.muchoMayorQue(1, 3)

    // Valores menores o iguales a 1
    assert(conjuntos.pertenece(0, mm1) == 0.0)
    assert(conjuntos.pertenece(1, mm1) == 0.0)

    // Valores entre 1 y 3 (crecimiento lineal)
    assert(conjuntos.pertenece(2, mm1) == 0.5)  // (2-1)/(3-1) = 0.5
    assert(conjuntos.pertenece(3, mm1) == 1.0)  // (3-1)/(3-1) = 1.0

    // Valores mayores a 3
    assert(conjuntos.pertenece(4, mm1) == 1.0)
    assert(conjuntos.pertenece(10, mm1) == 1.0)
    assert(conjuntos.pertenece(100, mm1) == 1.0)
  }

  test("muchoMayorQue(2, 6) debe retornar valores correctos") {
    val mm2 = conjuntos.muchoMayorQue(2, 6)

    // Valores menores o iguales a 2
    assert(conjuntos.pertenece(0, mm2) == 0.0)
    assert(conjuntos.pertenece(1, mm2) == 0.0)
    assert(conjuntos.pertenece(2, mm2) == 0.0)

    // Valores entre 2 y 6
    assert(conjuntos.pertenece(3, mm2) == 0.25)  // (3-2)/(6-2) = 0.25
    assert(conjuntos.pertenece(4, mm2) == 0.5)   // (4-2)/(6-2) = 0.5
    assert(conjuntos.pertenece(5, mm2) == 0.75)  // (5-2)/(6-2) = 0.75
    assert(conjuntos.pertenece(6, mm2) == 1.0)   // (6-2)/(6-2) = 1.0

    // Valores mayores a 6
    assert(conjuntos.pertenece(7, mm2) == 1.0)
    assert(conjuntos.pertenece(100, mm2) == 1.0)
  }

  test("muchoMayorQue(0, 5) debe retornar valores correctos") {
    val mm3 = conjuntos.muchoMayorQue(0, 5)

    assert(conjuntos.pertenece(-1, mm3) == 0.0)
    assert(conjuntos.pertenece(0, mm3) == 0.0)
    assert(conjuntos.pertenece(1, mm3) == 0.2)  // (1-0)/(5-0) = 0.2
    assert(conjuntos.pertenece(3, mm3) == 0.6)  // (3-0)/(5-0) = 0.6
    assert(conjuntos.pertenece(5, mm3) == 1.0)
    assert(conjuntos.pertenece(6, mm3) == 1.0)
  }

  test("muchoMayorQue(5, 10) con valores negativos") {
    val mm4 = conjuntos.muchoMayorQue(5, 10)

    // Valores negativos y menores a 5
    assert(conjuntos.pertenece(-10, mm4) == 0.0)
    assert(conjuntos.pertenece(0, mm4) == 0.0)
    assert(conjuntos.pertenece(4, mm4) == 0.0)

    // Valores entre 5 y 10
    assert(conjuntos.pertenece(5, mm4) == 0.0)
    assert(conjuntos.pertenece(7, mm4) == 0.4)  // (7-5)/(10-5) = 0.4
    assert(conjuntos.pertenece(10, mm4) == 1.0)

    // Valores mayores a 10
    assert(conjuntos.pertenece(11, mm4) == 1.0)
  }

  test("muchoMayorQue(10, 10) caso límite con a == m") {
    val mm5 = conjuntos.muchoMayorQue(10, 10)

    // Cuando a == m, cualquier x <= a es 0, cualquier x > a es 1
    assert(conjuntos.pertenece(9, mm5) == 0.0)
    assert(conjuntos.pertenece(10, mm5) == 0.0)
    assert(conjuntos.pertenece(11, mm5) == 1.0)
  }


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
  test("Inclusion: un conjunto debe estar incluido en si mismo") {
    val conjunto = cj.grande(10, 2)
    assert(cj.inclusion(conjunto, conjunto))
  }
  test("Inclusion: conjunto con parametros mas grandes, debe incluir al mas pequeño") {
    val conjuntoMenor = cj.grande(10, 2)
    val conjuntoMayor = cj.grande(5, 1)
    assert(cj.inclusion(conjuntoMenor, conjuntoMayor))
  }
  test("Inclusion: conjunto mas restrictivo no incluye al mas permisivo") {
    val conjunto1 = cj.grande(5, 1)
    val conjunto2 = cj.grande(10, 3)
    assert(!cj.inclusion(conjunto1, conjunto2))
  }
  test("Igualdad: conjuntos identicos deben ser iguales") {
    val c1 = cj.grande(10, 2)
    val c2 = cj.grande(10, 2)
    assert(cj.igualdad(c1, c2))
  }
  test("Igualdad: conjuntos diferentes no deben ser iguales") {
    val c1 = cj.grande(10, 2)
    val c2 = cj.grande(5, 1)
    assert(!cj.igualdad(c1, c2))
  }
  test("Igualdad: debe ser simetrica") {
    val c1 = cj.grande(10, 1)
    val c2 = cj.grande(10, 1)
    assert(cj.igualdad(c1, c2) == cj.igualdad(c2, c1))
  }
  test("Inclusion: conjunto complemento que no esta includio en el de origen") {
    val conjunto = cj.grande(10, 2)
    val complemento = cj.complemento(conjunto)
    assert(!cj.inclusion(complemento, conjunto))
  }
  test("Inclusion: conjunto vacio esta incluido en cualquier conjunto") {
    val conjuntoVacio: cj.ConjDifuso = _ => 0.0
    val conjunto = cj.grande(10, 2)
    assert(cj.inclusion(conjuntoVacio, conjunto))
  }
  test("Inclusion: cualquier conjunto esta incluido en el universal") {
    val conjunto = cj.grande(10, 2)
    val conjuntoUniversal: cj.ConjDifuso = _ => 1.0
    assert(cj.inclusion(conjunto, conjuntoUniversal))
  }
  test("Igualdad: complementos doble general una igualdad con el conjunto original") {
    val c1 = cj.grande(10, 2)
    val c2 = cj.complemento(cj.complemento(c1))
    assert(cj.igualdad(c1, c2))
  }


}
