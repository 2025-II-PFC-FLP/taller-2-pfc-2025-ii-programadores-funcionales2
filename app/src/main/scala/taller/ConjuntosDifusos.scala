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


}
