import module1.list.List
import module1.{hof, type_system}

object Main {

  def main(args: Array[String]): Unit = {
    println("Hello, World!")

    val r: String => Unit = hof.logRunningTime(hof.doomy)
    r("Doomy")

    val myList = List(1, 2, 3);
    val myReversedList = myList.reverse();

    Console.println(myReversedList.mkString(";"));

    val myMappedList = myList.map(_+1);
    Console.println(myMappedList.mkString(";"));

    val myFilteredList = myList.filter(x => x > 1);
    Console.println(myFilteredList.mkString(";"));
  }
}