package module1.homework

object LinearAlgebraOps{
  def sum(v1: Array[Int], v2: Array[Int]): Array[Int] = {
    if (v1.length != v2.length) {
      throw new Exception("Operation is not supported");
    } else {
      var returnValue = Array.ofDim[Int](v1.length);
      for (i <- 1 to v1.length) {
        returnValue(i) = v1(i) + v2(i);
      }
      returnValue;
    }
  }

  def scale(a: Int, v: Array[Int]): Array[Int] = {
    var returnValue = Array.ofDim[Int](v.length);
    for (i <- 1 to v.length) {
      returnValue(i) = a * v(i);
    }
    returnValue;
  }

  def axpy(a: Int, v1: Array[Int], v2: Array[Int]): Array[Int] = {
    if (v1.length != v2.length) {
      throw new Exception("Operation is not supported");
    } else {
      var returnValue = Array.ofDim[Int](v1.length);
      for (i <- 1 to v1.length) {
        returnValue(i) = a * v1(i) + v2(i);
      }
      returnValue;
    }
  }
}