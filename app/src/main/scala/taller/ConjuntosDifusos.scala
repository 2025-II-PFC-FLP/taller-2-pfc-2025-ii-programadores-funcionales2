package taller

class ConjuntosDifusos {
  type ConjDifuso = Int => Double

  def grande(d: Int, e: Int): ConjDifuso = {
    (n: Int)=> {
      //si el numero es negativo o cero, no puede ser grande
      if (n <= 0) 0.0
      else {
        val pertenencia =n.toDouble / (n + d)
        math.pow(pertenencia , e)

      }

    }
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    (n : Int) => 1.0 - c(n)
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (n : Int) => math.max(cd1(n), cd2(n))

  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (n : Int) => math.min(cd1(n), cd2(n))

  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    @annotation.tailrec
    def aux (n: Int): Boolean = {
      if (n > 1000) true
      else if (cd1(n) > cd2(n)) false
      else aux(n+1)
    }
    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    val epsilon = 1e-6 //Agregamos una tolerancia numerica para dos valores iguales si su diferencia es pequeña, debido a que usamos valores float, genera errores a la hora de realizar los test
    @annotation.tailrec
    def aux(n: Int): Boolean = {
      if(n > 1000) true
      else if (math.abs(cd1(n) - cd2(n)) > epsilon) false
      else aux(n + 1)
    }
    aux(0)
  }



}
