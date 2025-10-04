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

  test("unión debe retornar el máximo de los dos valores") {
    val conjunto1 = cj.grande(10, 1)
    val conjunto2 = cj.grande(20, 1)
    val union = cj.union(conjunto1, conjunto2)

    val valor1 = conjunto1(15)
    val valor2 = conjunto2(15)
    assert(union(15) == math.max(valor1, valor2))
  }
  test("intersección debe retornar el mínimo de los dos valores") {
    val conjunto1 = cj.grande(10, 1)
    val conjunto2 = cj.grande(20, 1)
    val interseccion = cj.interseccion(conjunto1, conjunto2)

    val valor1 = conjunto1(15)
    val valor2 = conjunto2(15)
    assert(interseccion(15) == math.min(valor1, valor2))
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
