import module1.list.List
import module1.list.List.{incList, shoutString}
import module1.{hof, type_system}

object Main {

  def main(args: Array[String]): Unit = {
    println("Hello, World!")

    val r: String => Unit = hof.logRunningTime(hof.doomy)
    r("Doomy")

    val myList = List(1, 2, 3);
    Console.println("myList: \"" + myList.mkString(";") + "\"");

    val myReversedList = myList.reverse();

    Console.println("myReversedList: \"" + myReversedList.mkString(";") + "\"");

    val myMappedList = myList.map(_+1);
    Console.println("myMappedList: \"" + myMappedList.mkString(";") + "\"");

    val myFilteredList = myList.filter(x => x > 1);
    Console.println("myFilteredList: \"" + myFilteredList.mkString(";") + "\"");

    val myIncreasedList = incList(myList);
    Console.println("myIncreasedList: \"" + myIncreasedList.mkString(";") + "\"");

    val myStringList = List("Hello", "World");
    val myShoutList = shoutString(myStringList);
    Console.println("myShoutList: \"" + myShoutList.mkString(";") + "\"");
  }
}