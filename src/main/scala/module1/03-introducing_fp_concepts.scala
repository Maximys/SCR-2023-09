package module1

import scala.annotation.tailrec
import scala.language.postfixOps



/**
 * referential transparency
 */



 // recursion

object recursion {

  /**
   * Реализовать метод вычисления n!
   * n! = 1 * 2 * ... n
   */

  def fact(n: Int): Int = {
    var _n = 1
    var i = 2
    while (i <= n){
      _n *= i
      i += 1
    }
    _n
  }

  def factRec(n: Int): Int =
    if( n <= 0) 1 else n * factRec(n - 1)

  def factRecTail(n: Int): Int = {

    @tailrec
    def loop(n: Int, accum: Int): Int =
      if( n <= 1) accum
      else loop(n - 1, n * accum)

    loop(n, 1)
  }



  /**
   * реализовать вычисление N числа Фибоначчи
   * F0 = 0, F1 = 1, Fn = Fn-1 + Fn - 2
   *
   */
  def Fibonacci(n: Int): Int =
    if (n < 0) {
      throw new Exception("n value should be greater or equal to 0");
    }
    else
    {
      n match {
        case 0 => 0
        case 1 => 1
        case _ => Fibonacci(n - 1) + Fibonacci(n - 2)
      }
    }
}

object hof{


  // обертки

  def logRunningTime[A, B](f: A => B): A => B = a => {
    val start = System.currentTimeMillis()
    val result: B = f(a)
    val end = System.currentTimeMillis()
    println(end - start)
    result
  }

  def doomy(str: String) = {
    Thread.sleep(1000)
    println(str)
  }



  // изменение поведения ф-ции

  val arr = Array(1, 2, 3, 4, 5)

  def isOdd(i: Int): Boolean = i % 2 > 0

  def not[A](f: A => Boolean): A => Boolean = a => !f(a)

  def isEven: Int => Boolean = not(isOdd)


  // (A, B, C) => D   curring A => B => C => D
  // изменение самой функции

  def curried[A, B, C](f: (A, B) => C): A => B => C = a => b => f(a, b)

  def partial[A, B, C](a: A, f: (A, B) => C): B => C = curried(f)(a)

  def sum(x: Int, y: Int): Int  = x + y

  val s0: Int => Int => Int = curried(sum)

  val s1: Function1[Int, Int] = s0(2)

  val s2: Int = s1(3) // 5

  val p0: Int => Int = partial(2, sum)

  p0(3) // 5


















  trait Consumer{
       def subscribe(topic: String): Stream[Record]
   }

   case class Record(value: String)

   case class Request()
   
   object Request {
       def parse(str: String): Request = ???
   }

  /**
   *
   * (Опционально) Реализовать ф-цию, которая будет читать записи Request из топика,
   * и сохранять их в базу
   */
   def createRequestSubscription() = ???



}






/**
 *  Реализуем тип Option
 */


 object opt {

  /**
   *
   * Реализовать структуру данных Option, который будет указывать на присутствие либо отсутсвие результата
   */

  sealed trait Option[+T]{

    def isEmpty: Boolean =  this match {
      case Some(v) => false
      case None => true
    }

    def get: T = this match {
      case Some(v) => v
      case None => throw new Exception("None get")
    }

    def map[B](f: T => B): Option[B] = flatMap(v => Some(f(v)))

    def flatMap[B](f: T => Option[B]): Option[B] = this match {
      case Some(v) => f(v)
      case None => None
    }

    /**
     *
     * Реализовать метод printIfAny, который будет печатать значение, если оно есть
     */
    def printIfAny(): Unit = this match {
      case Some(v) => println(v)
      case None =>
    }

    /**
     *
     * Реализовать метод zip, который будет создавать Option от пары значений из 2-х Option
     */
    def zip[B](secondOption: Option[B]): Option[(T, B)] = this match {
      case Some(v) => secondOption match {
        case Some(vSO) => new Option[(T, B)] {(v, vSO)}
        case None => None
      }
      case None => None
    }

    /**
     *
     * Реализовать метод filter, который будет возвращать не пустой Option
     * в случае если исходный не пуст и предикат от значения = true
     */
    def filter(): (Option[T], Boolean) = this match {
      case Some(v) => (this, true)
      case None => (this, false)
    }
  }
  case class Some[T](v: T) extends Option[T]
  case object None extends Option[Nothing]

  object Option{

  }




  // Animal -> Dog
  // Covariant + отношения переносятся на контейнер
  // Contravariant - отношения переносятся на контейнер наоборот
  // Invariant - нет отношений

 }

 object list {
   /**
    *
    * Реализовать односвязанный иммутабельный список List
    * Список имеет два случая:
    * Nil - пустой список
    * Cons - непустой, содержит первый элемент (голову) и хвост (оставшийся список)
    */

    sealed trait List[+T] {
     /**
      * Метод cons, добавляет элемент в голову списка, для этого метода можно воспользоваться названием `::`
      *
      */
     def ::[S >: T](newHead: S) : List[S] = this match {
       case list.::(_, _) => list.::(newHead, this)
       case list.Nil => list.::(newHead, list.Nil)
     }

     /**
      * Метод mkString возвращает строковое представление списка, с учетом переданного разделителя
      *
      */
     def mkString(delimetr: String): String = {
       this match {
         case list.::(head, tail) => head + delimetr + tail.mkString(delimetr);
         case list.Nil => ""
       }
     }

     /**
      *
      * Реализовать метод reverse который позволит заменить порядок элементов в списке на противоположный
      */
      def reverse(): List[T] = {
        def loop(currentList: List[T], acc: List[T]): List[T] = currentList match {
          case list.::(head, tail) => loop(tail, list.::(head, acc));
          case list.Nil => acc;
        }

        loop(this, List[T]());
      }

     /**
      *
      * Реализовать метод map для списка который будет применять некую ф-цию к элементам данного списка
      */
     def map[B](f: T => B): List[B] = {
       def loop(currentList: List[T], acc: List[B]): List[B] = currentList match {
         case list.::(head, tail) => loop(tail, list.::(f(head), acc));
         case list.Nil => acc.reverse()
       }

       loop(this, List[B]());
     }

     /**
      *
      * Реализовать метод filter для списка который будет фильтровать список по некому условию
      */
     def filter(f: T => Boolean): List[T] = {
       def loop(currentList: List[T], acc: List[T]): List[T] = currentList match {
         case list.::(head, tail) if (f(head)) => loop(tail, list.::(head, acc));
         case list.::(_, tail) => loop(tail, acc);
         case list.Nil => acc
       }

       loop(this, List[T]());
     }
   }
    case class ::[A](head: A, tail: List[A]) extends List[A]
    case object Nil extends List[Nothing]


    object List{
      /**
       * Конструктор, позволяющий создать список из N - го числа аргументов
       * Для этого можно воспользоваться *
       *
       * Например вот этот метод принимает некую последовательность аргументов с типом Int и выводит их на печать
       * def printArgs(args: Int*) = args.foreach(println(_))
       */
      def apply[A](v: A*): List[A] = if(v.isEmpty) Nil
      else ::(v.head, apply(v.tail:_*))

      /**
       *
       * Написать функцию incList котрая будет принимать список Int и возвращать список,
       * где каждый элемент будет увеличен на 1
       */
      def incList(args: list.List[Int]) : list.List[Int] =
        args.map(_+1);
    }

    // Пример создания экземпляра с помощью конструктора apply

    List(1, 2, 3)



    /**
      *
      * Написать функцию shoutString котрая будет принимать список String и возвращать список,
      * где к каждому элементу будет добавлен префикс в виде '!'
      */

 }