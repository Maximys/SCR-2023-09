package module1.homework.collections.laboratoryWork3

import module1.homework.collections.laboratoryWork3.ballColor.ballColor

import scala.util.Random

class bucket {
  private val BoxSize : Int = 6;

  private var box: List[ballColor] = List[ballColor](
    ballColor.Black,
    ballColor.White,
    ballColor.Black,
    ballColor.Black,
    ballColor.White,
    ballColor.White)

  def isWhite: Boolean = {
    val r = new Random
    val randomIndex = r.nextInt(BoxSize);
    var selectedBall : ballColor = box(randomIndex);

    selectedBall == ballColor.White;
  }
}
